package org.example.restlogisticserp.database;

import org.example.restlogisticserp.DatabaseConnection;
import org.example.restlogisticserp.models.Bid;
import org.example.restlogisticserp.models.BidSummary;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BidDBUtils {

    private static final Logger logger = Logger.getLogger(BidDBUtils.class.getName());
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error establishing database connection", e);
            throw new RuntimeException("Database connection initialization failed.", e);
        }
    }

    private static void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
        }
    }

    public static String placeBid(Bid bid) throws SQLException {
        checkConnection();
        String query = "INSERT INTO bids (inquiry_id, created_company_id, amount, cut_off_date, status, is_accepted, is_finalized, created_by, inquiry_owner_company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, bid.getInquiryId());
            statement.setLong(2, bid.getCreatedCompanyId());
            statement.setBigDecimal(3, bid.getAmount());
            statement.setTimestamp(4, bid.getCutOffDate());
            statement.setString(5, bid.getStatus());
            statement.setBoolean(6, bid.getIsAccepted());
            statement.setBoolean(7, bid.getIsFinalized());
            statement.setLong(8, bid.getCreatedBy());
            statement.setLong(9, bid.getInquiryOwnerCompanyId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bid.setBidId(generatedKeys.getInt(1));
                        return "Bid placed successfully.";
                    } else {
                        throw new SQLException("Creating bid failed, no ID obtained.");
                    }
                }
            } else {
                return "Placing bid failed, no rows affected.";
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            return "Error placing bid: " + e.getMessage();
        }
    }

    public static Bid fetchBidByCarrierForInquiry(long companyId, long inquiryId) throws SQLException {
        checkConnection();
        String query = "SELECT * FROM bids WHERE created_company_id = ? AND inquiry_id = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, companyId);
            statement.setLong(2, inquiryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBid(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        }

        return null;
    }

    public static String updateBidAmount(long bidId, BigDecimal amount) throws SQLException {
        checkConnection();
        String query = "UPDATE bids SET amount = ? WHERE bid_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, amount);
            statement.setLong(2, bidId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0 ? "Bid amount updated successfully." : "Updating bid amount failed, no rows affected.";
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            return "Error updating bid amount: " + e.getMessage();
        }
    }

    public static List<Bid> fetchBidsForCompany(long companyId) throws SQLException {
        checkConnection();
        List<Bid> bids = new ArrayList<>();
        String query = "SELECT b.*, i.inquiry_reference FROM bids b INNER JOIN Inquiries i ON b.inquiry_id = i.inquiry_id WHERE b.created_company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, companyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bids.add(mapResultSetToBid(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        }

        return bids;
    }

    public static BidSummary fetchBidsSummaryByShippingCompany(long companyId) throws SQLException {
        checkConnection();
        String query = "SELECT sum(case when status = 'ongoing' then 1 else 0 end) as ongoing_count, sum(case when status = 'expired' then 1 else 0 end) as expired_count, sum(case when status = 'accepted' then 1 else 0 end) as accepted_count, sum(case when status = 'rejected' then 1 else 0 end) as rejected_count, count(*) as total_count FROM bids WHERE created_company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, companyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBidSummary(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        }

        return null;
    }

    public static List<Bid> fetchBidsForInquiry(long inquiryId) throws SQLException {
        checkConnection();
        List<Bid> bids = new ArrayList<>();
        String query = "SELECT b.*, c.name as company_name FROM bids b INNER JOIN companies c ON b.created_company_id = c.company_id WHERE b.inquiry_id = ? ORDER BY b.bid_date DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, inquiryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bids.add(mapResultSetToBid(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        }

        return bids;
    }

    public static String closeBid(long bidId) throws SQLException {
        checkConnection();

        // Update the accepted bid status
        String updateBidQuery = "UPDATE bids SET status = 'accepted' WHERE bid_id = ?";

        // Update the inquiry status to closed
        String updateInquiryQuery = "UPDATE inquiries SET status = 'closed' WHERE inquiry_id = ?";

        // Update other bids to closed
        String closeOtherBidsQuery = "UPDATE bids SET status = 'rejected' WHERE inquiry_id = ? AND bid_id != ?";

        try (PreparedStatement updateBidStatement = connection.prepareStatement(updateBidQuery);
             PreparedStatement updateInquiryStatement = connection.prepareStatement(updateInquiryQuery);
             PreparedStatement closeOtherBidsStatement = connection.prepareStatement(closeOtherBidsQuery)) {

            connection.setAutoCommit(false);

            // Step 1: Update the accepted bid
            updateBidStatement.setLong(1, bidId);
            updateBidStatement.executeUpdate();

            // Step 2: Fetch the bid details
            Bid bid = fetchBidById(bidId);
            if (bid == null) {
                connection.rollback();
                return "Bid not found.";
            }
            long inquiryId = bid.getInquiryId();

            // Step 3: Update the inquiry status
            updateInquiryStatement.setLong(1, inquiryId);
            updateInquiryStatement.executeUpdate();

            // Step 4: Close all other bids for the same inquiry
            closeOtherBidsStatement.setLong(1, inquiryId);
            closeOtherBidsStatement.setLong(2, bidId);
            closeOtherBidsStatement.executeUpdate();

            // Step 5: Commit the transaction
            connection.commit();
            return "Bid closed successfully.";
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Rollback Error: " + rollbackEx.getMessage(), rollbackEx);
            }
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            return "Error closing bid: " + e.getMessage();
        }
    }


    public static Bid fetchBidById(long bidId) throws SQLException {
        checkConnection();
        String query = "SELECT * FROM bids WHERE bid_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, bidId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBid(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        }

        return null;
    }

    private static Bid mapResultSetToBid(ResultSet resultSet) throws SQLException {
        Bid bid = new Bid();
        bid.setBidId(resultSet.getInt("bid_id"));
        bid.setInquiryId(resultSet.getInt("inquiry_id"));
        bid.setCreatedCompanyId(resultSet.getInt("created_company_id"));
        bid.setAmount(resultSet.getBigDecimal("amount"));
        bid.setBidDate(resultSet.getTimestamp("bid_date"));
        bid.setCutOffDate(resultSet.getTimestamp("cut_off_date"));
        bid.setStatus(resultSet.getString("status"));
        bid.setIsAccepted(resultSet.getBoolean("is_accepted"));
        bid.setIsFinalized(resultSet.getBoolean("is_finalized"));
        bid.setCreatedBy(resultSet.getInt("created_by"));
        bid.setInquiryOwnerCompanyId(resultSet.getInt("inquiry_owner_company_id"));
        bid.setCreatedAt(resultSet.getTimestamp("created_at"));
        bid.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return bid;
    }

    private static BidSummary mapResultSetToBidSummary(ResultSet resultSet) throws SQLException {
        BidSummary summary = new BidSummary();
        summary.setOngoingCount(resultSet.getInt("ongoing_count"));
        summary.setExpiredCount(resultSet.getInt("expired_count"));
        summary.setAcceptedCount(resultSet.getInt("accepted_count"));
        summary.setRejectedCount(resultSet.getInt("rejected_count"));
        summary.setTotalCount(resultSet.getInt("total_count"));
        return summary;
    }

    public static BidSummary fetchBidsSummaryCustomerCompany(Long companyId) throws SQLException {
        checkConnection();

        // Define the base query
        String query;

        // Check if companyId is null
        if (companyId == null) {
            // Modify the query to fetch totals across all bids (if that's the intended behavior)
            query = "SELECT sum(case when status = 'ongoing' then 1 else 0 end) as ongoing_count, "
                    + "sum(case when status = 'expired' then 1 else 0 end) as expired_count, "
                    + "sum(case when status = 'accepted' then 1 else 0 end) as accepted_count, "
                    + "sum(case when status = 'rejected' then 1 else 0 end) as rejected_count, "
                    + "count(*) as total_count FROM bids"; // Removed WHERE clause
        } else {
            // Original query to filter by companyId
            query = "SELECT sum(case when status = 'ongoing' then 1 else 0 end) as ongoing_count, "
                    + "sum(case when status = 'expired' then 1 else 0 end) as expired_count, "
                    + "sum(case when status = 'accepted' then 1 else 0 end) as accepted_count, "
                    + "sum(case when status = 'rejected' then 1 else 0 end) as rejected_count, "
                    + "count(*) as total_count FROM bids WHERE inquiry_owner_company_id = ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the parameter only if companyId is not null
            if (companyId != null) {
                statement.setLong(1, companyId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBidSummary(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        }

        return null;
    }


    public static List<Bid> fetchBidsForCompanyAndInquiry(Integer companyId, Integer inquiryId) throws SQLException {
        checkConnection();
        List<Bid> bids = new ArrayList<>();
        String query = "SELECT * FROM bids WHERE created_company_id = ? AND inquiry_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, companyId);
            statement.setInt(2, inquiryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bids.add(mapResultSetToBid(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        }

        return bids;
    }
}