package org.example.restlogisticserp.database;

import org.example.restlogisticserp.DatabaseConnection;
import org.example.restlogisticserp.models.InquiryRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InquiryRequestDBUtils {
    private static final Logger logger = Logger.getLogger(InquiryRequestDBUtils.class.getName());
    private static Connection connection;

    // Static block to establish the initial database connection
    static {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error", e);
            throw new RuntimeException(e);
        }
    }

    // Method to check if the connection is alive and re-establish if necessary
    private static void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
        }
    }

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    public static InquiryRequest insertInquiryRequest(InquiryRequest inquiryRequest) throws SQLException {
        checkConnection(); // Ensure the connection is valid

        String insertQuery = "INSERT INTO inquiryRequests (company_id, inquiry_id, accepted, created_by, updated_by) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, inquiryRequest.getCompanyId());
            preparedStatement.setLong(2, inquiryRequest.getInquiryId());
            preparedStatement.setBoolean(3, inquiryRequest.isAccepted());
            preparedStatement.setLong(4, inquiryRequest.getCreatedBy());
            preparedStatement.setLong(5, inquiryRequest.getUpdatedBy());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating inquiry request failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inquiryRequest.setId(generatedKeys.getInt(1)); // Set the generated ID
                    return inquiryRequest;
                } else {
                    throw new SQLException("Creating inquiry request failed, no ID obtained.");
                }
            }
        }
    }

    public static List<InquiryRequest> fetchAllInquiryRequests() throws SQLException {
        checkConnection(); // Ensure the connection is valid

        List<InquiryRequest> inquiryRequests = new ArrayList<>();
        String selectQuery = "SELECT * FROM inquiryRequests";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                InquiryRequest inquiryRequest = new InquiryRequest();
                inquiryRequest.setId(resultSet.getInt("id"));
                inquiryRequest.setCompanyId(resultSet.getLong("company_id"));
                inquiryRequest.setInquiryId(resultSet.getLong("inquiry_id"));
                inquiryRequest.setAccepted(resultSet.getBoolean("accepted"));
                inquiryRequest.setCreatedBy(resultSet.getLong("created_by"));
                inquiryRequest.setUpdatedBy(resultSet.getLong("updated_by"));
                inquiryRequest.setCreatedAt(resultSet.getTimestamp("created_at"));
                inquiryRequest.setUpdatedAt(resultSet.getTimestamp("updated_at"));

                inquiryRequests.add(inquiryRequest);
            }
        }

        return inquiryRequests;
    }
}
