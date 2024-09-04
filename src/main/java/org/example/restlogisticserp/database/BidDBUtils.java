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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error establishing database connection", e);
            throw new RuntimeException(e);
        }
    }

    public static String placeBid(Bid bid) {
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
                        bid.setBidId((int) generatedKeys.getInt(1)); // Use getInt(1) if bidId is a Long
                    } else {
                        throw new SQLException("Creating bid failed, no ID obtained.");
                    }
                }
                return "Bid placed successfully.";
            } else {
                return "Placing bid failed, no rows affected.";
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            return "Error placing bid: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            return "Unexpected error placing bid: " + e.getMessage();
        }
    }


    public static Bid fetchBidByCarrierForInquiry(long companyId, long inquiryId) {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        return null;
    }

    public static String updateBidAmount(long bidId, BigDecimal amount) {
        String query = "UPDATE bids SET amount = ? WHERE bid_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, amount);
            statement.setLong(2, bidId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return "Bid amount updated successfully.";
            } else {
                return "Updating bid amount failed, no rows affected.";
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            return "Error updating bid amount: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            return "Unexpected error updating bid amount: " + e.getMessage();
        }
    }

    public static List<Bid> fetchBidsForCompany(long companyId) {
        List<Bid> bids = new ArrayList<>();
        //get inquiry reference as inquiry_reference
        String query = "SELECT b.*, i.inquiry_reference FROM bids b INNER JOIN inquiries i ON b.inquiry_id = i.inquiry_id WHERE b.created_company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, companyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bids.add(mapResultSetToBid(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        return bids;
    }

    public static BidSummary fetchBidsSummaryByShippingCompany(long companyId) {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        return null;
    }

    public static BidSummary fetchBidsSummaryCustomerCompany(long companyId) {
        String query = "SELECT sum(case when status = 'ongoing' then 1 else 0 end) as ongoing_count, sum(case when status = 'expired' then 1 else 0 end) as expired_count, sum(case when status = 'accepted' then 1 else 0 end) as accepted_count, sum(case when status = 'rejected' then 1 else 0 end) as rejected_count, count(*) as total_count FROM bids WHERE inquiry_owner_company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, companyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBidSummary(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        return null;
    }

    public static List<Bid> fetchBidsForInquiry(long inquiryId) {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        return bids;
    }

    public static Bid fetchBidById(long bidId) {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        return null;
    }

    public static String closeBid(long bidId) {
        Bid bid = fetchBidById(bidId);
        if (bid == null) {
            return "Bid not found.";
        }

        long inquiryId = bid.getInquiryId();

        String updateBidQuery = "UPDATE bids SET status = 'accepted' WHERE bid_id = ?";
        String updateInquiryQuery = "UPDATE inquiries SET status = 'closed' WHERE inquiry_id = ?";

        try (PreparedStatement updateBidStatement = connection.prepareStatement(updateBidQuery);
             PreparedStatement updateInquiryStatement = connection.prepareStatement(updateInquiryQuery)) {

            connection.setAutoCommit(false);

            updateBidStatement.setLong(1, bidId);
            updateBidStatement.executeUpdate();

            updateInquiryStatement.setLong(1, inquiryId);
            updateInquiryStatement.executeUpdate();

            connection.commit();
            return "Bid closed successfully.";
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error rolling back transaction", rollbackEx);
            }
            return "Error closing bid: " + e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            return "Unexpected error closing bid: " + e.getMessage();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error resetting auto-commit", e);
            }
        }
    }

    private static Bid mapResultSetToBid(ResultSet resultSet) throws SQLException {
        Bid bid = new Bid();
        bid.setBidId(resultSet.getInt("bid_id"));  // Changed to int
        bid.setInquiryId(resultSet.getInt("inquiry_id"));
        bid.setCreatedCompanyId(resultSet.getInt("created_company_id"));
        bid.setAmount(resultSet.getBigDecimal("amount"));
        bid.setBidDate(resultSet.getTimestamp("bid_date"));  // Use bidDate here
        bid.setCutOffDate(resultSet.getTimestamp("cut_off_date"));  // Use cutOffDate here
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

    public static List<Bid> fetchBidsForCompanyAndInquiry(Integer companyId, Integer inquiryId) {
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }

        return bids;
    }
}
