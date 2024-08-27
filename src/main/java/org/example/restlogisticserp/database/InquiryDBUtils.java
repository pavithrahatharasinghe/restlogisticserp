package org.example.restlogisticserp.database;

import org.example.restlogisticserp.DatabaseConnection;
import org.example.restlogisticserp.models.Inquiry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InquiryDBUtils {
    private static final Logger logger = Logger.getLogger(CompanyDBUtils.class.getName());
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error establishing database connection", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error", e);
            throw new RuntimeException(e);
        }
    }

    // Create a new inquiry
    public static Inquiry createInquiry(Inquiry inquiry) {
        String query = "INSERT INTO Inquiries (company_id, inquiry_reference, shipment_mode, shipment_type, fcl_type, cbm_volume, dimensions, gross_weight, port_of_origin, port_of_discharge, commodity, cargo_description, dangerous_goods, freight_term, inco_term, preferred_date_of_arrival, preferred_date_of_departure, preferred_transit_period, cut_off_time, status, PublishedStatus, publish_date, bid_accepted, accepted_bid_id, created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            statement.setInt(1, inquiry.getCompanyId());
            statement.setString(2, inquiry.getInquiryReference());
            statement.setString(3, inquiry.getShipmentMode());
            statement.setString(4, inquiry.getShipmentType());
            statement.setString(5, inquiry.getFclType());
            statement.setDouble(6, inquiry.getCbmVolume());
            statement.setString(7, inquiry.getDimensions());
            statement.setDouble(8, inquiry.getGrossWeight());
            statement.setString(9, inquiry.getPortOfOrigin());
            statement.setString(10, inquiry.getPortOfDischarge());
            statement.setString(11, inquiry.getCommodity());
            statement.setString(12, inquiry.getCargoDescription());
            statement.setBoolean(13, inquiry.getDangerousGoods());
            statement.setString(14, inquiry.getFreightTerm());
            statement.setString(15, inquiry.getIncoTerm());
            statement.setDate(16, inquiry.getPreferredDateOfArrival());
            statement.setDate(17, inquiry.getPreferredDateOfDeparture());
            statement.setString(18, inquiry.getPreferredTransitPeriod());
            statement.setTimestamp(19, inquiry.getCutOffTime());
            statement.setString(20, inquiry.getStatus());
            statement.setString(21, inquiry.getPublishedStatus());
            statement.setTimestamp(22, inquiry.getPublishDate());
            statement.setBoolean(23, inquiry.getBidAccepted());
            statement.setObject(24, inquiry.getAcceptedBidId(), Types.INTEGER);
            statement.setInt(25, inquiry.getCreatedBy());

            // Execute and retrieve generated keys
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                inquiry.setInquiryId(resultSet.getInt(1));
            }
            return inquiry;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating inquiry", e);
            throw new RuntimeException(e);
        }
    }

    // Fetch draft inquiries by customer
    public static List<Inquiry> fetchDraftInquiriesByCustomer(int companyId) {
        String query = "SELECT * FROM Inquiries WHERE company_id = ? AND PublishedStatus = 'Draft'";
        List<Inquiry> inquiries = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Inquiry inquiry = mapResultSetToInquiry(resultSet);
                inquiries.add(inquiry);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching draft inquiries by customer", e);
            throw new RuntimeException(e);
        }
        return inquiries;
    }

    // Fetch inquiry by ID
    public static Inquiry fetchInquiryById(int companyId, int inquiryId) {
        String query = "SELECT * FROM Inquiries WHERE company_id = ? AND inquiry_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, companyId);
            statement.setInt(2, inquiryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToInquiry(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching inquiry by ID", e);
            throw new RuntimeException(e);
        }
    }

    // Update an inquiry
    public static boolean updateInquiry(int inquiryId, Inquiry inquiry) {
        String query = "UPDATE Inquiries SET inquiry_reference = ?, shipment_mode = ?, shipment_type = ?, fcl_type = ?, cbm_volume = ?, dimensions = ?, gross_weight = ?, port_of_origin = ?, port_of_discharge = ?, commodity = ?, cargo_description = ?, dangerous_goods = ?, freight_term = ?, inco_term = ?, preferred_date_of_arrival = ?, preferred_date_of_departure = ?, preferred_transit_period = ?, cut_off_time = ?, status = ?, PublishedStatus = ?, publish_date = ? WHERE inquiry_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Bind parameters
            statement.setString(1, inquiry.getInquiryReference());
            statement.setString(2, inquiry.getShipmentMode());
            statement.setString(3, inquiry.getShipmentType());
            statement.setString(4, inquiry.getFclType());
            statement.setDouble(5, inquiry.getCbmVolume());
            statement.setString(6, inquiry.getDimensions());
            statement.setDouble(7, inquiry.getGrossWeight());
            statement.setString(8, inquiry.getPortOfOrigin());
            statement.setString(9, inquiry.getPortOfDischarge());
            statement.setString(10, inquiry.getCommodity());
            statement.setString(11, inquiry.getCargoDescription());
            statement.setBoolean(12, inquiry.getDangerousGoods());
            statement.setString(13, inquiry.getFreightTerm());
            statement.setString(14, inquiry.getIncoTerm());
            statement.setDate(15, inquiry.getPreferredDateOfArrival());
            statement.setDate(16, inquiry.getPreferredDateOfDeparture());
            statement.setString(17, inquiry.getPreferredTransitPeriod());
            statement.setTimestamp(18, inquiry.getCutOffTime());
            statement.setString(19, inquiry.getStatus());
            statement.setString(20, inquiry.getPublishedStatus());
            statement.setTimestamp(21, inquiry.getPublishDate());
            statement.setInt(22, inquiryId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating inquiry", e);
            throw new RuntimeException(e);
        }
    }

    // Fetch published inquiries by customer
    public static List<Inquiry> fetchInquiriesByCustomer(int companyId) {
        String query = "SELECT Inquiries.*, COALESCE((SELECT MIN(amount) FROM bids WHERE bids.inquiry_id = Inquiries.inquiry_id), 0) AS lowest_bid_amount FROM Inquiries WHERE company_id = ? AND PublishedStatus = 'Published' ORDER BY Inquiries.created_at DESC";
        List<Inquiry> inquiries = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Inquiry inquiry = mapResultSetToInquiry(resultSet);
                inquiries.add(inquiry);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching inquiries by customer", e);
            throw new RuntimeException(e);
        }
        return inquiries;
    }

    // Fetch published inquiries by company
    public static List<Inquiry> fetchInquiriesByCompany() {
        String query = "SELECT Inquiries.*, COALESCE((SELECT MIN(amount) FROM bids WHERE bids.inquiry_id = Inquiries.inquiry_id), 0) AS lowest_bid_amount FROM Inquiries WHERE PublishedStatus = 'Published' ORDER BY Inquiries.created_at DESC";
        List<Inquiry> inquiries = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Inquiry inquiry = mapResultSetToInquiry(resultSet);
                inquiries.add(inquiry);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching inquiries by company", e);
            throw new RuntimeException(e);
        }
        return inquiries;
    }

    // Fetch inquiry by ID for company
    public static Inquiry fetchInquiryByIdByCompany(int inquiryId) {
        String query = "SELECT Inquiries.*, COALESCE((SELECT MIN(amount) FROM bids WHERE bids.inquiry_id = Inquiries.inquiry_id), 0) AS lowest_bid_amount FROM Inquiries WHERE inquiry_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inquiryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToInquiry(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching inquiry by ID by company", e);
            throw new RuntimeException(e);
        }
    }

    // Helper method to map a ResultSet to an Inquiry object
    private static Inquiry mapResultSetToInquiry(ResultSet resultSet) throws SQLException {
        Inquiry inquiry = new Inquiry();
        inquiry.setInquiryId(resultSet.getInt("inquiry_id"));
        inquiry.setCompanyId(resultSet.getInt("company_id"));
        inquiry.setInquiryReference(resultSet.getString("inquiry_reference"));
        inquiry.setShipmentMode(resultSet.getString("shipment_mode"));
        inquiry.setShipmentType(resultSet.getString("shipment_type"));
        inquiry.setFclType(resultSet.getString("fcl_type"));
        inquiry.setCbmVolume(resultSet.getDouble("cbm_volume"));
        inquiry.setDimensions(resultSet.getString("dimensions"));
        inquiry.setGrossWeight(resultSet.getDouble("gross_weight"));
        inquiry.setPortOfOrigin(resultSet.getString("port_of_origin"));
        inquiry.setPortOfDischarge(resultSet.getString("port_of_discharge"));
        inquiry.setCommodity(resultSet.getString("commodity"));
        inquiry.setCargoDescription(resultSet.getString("cargo_description"));
        inquiry.setDangerousGoods(resultSet.getBoolean("dangerous_goods"));
        inquiry.setFreightTerm(resultSet.getString("freight_term"));
        inquiry.setIncoTerm(resultSet.getString("inco_term"));
        inquiry.setPreferredDateOfArrival(resultSet.getDate("preferred_date_of_arrival"));
        inquiry.setPreferredDateOfDeparture(resultSet.getDate("preferred_date_of_departure"));
        inquiry.setPreferredTransitPeriod(resultSet.getString("preferred_transit_period"));
        inquiry.setCutOffTime(resultSet.getTimestamp("cut_off_time"));
        inquiry.setStatus(resultSet.getString("status"));
        inquiry.setPublishedStatus(resultSet.getString("PublishedStatus"));
        inquiry.setPublishDate(resultSet.getTimestamp("publish_date"));
        inquiry.setBidAccepted(resultSet.getBoolean("bid_accepted"));
        inquiry.setAcceptedBidId((Integer) resultSet.getObject("accepted_bid_id"));
        inquiry.setCreatedBy(resultSet.getInt("created_by"));
        return inquiry;
    }
}

