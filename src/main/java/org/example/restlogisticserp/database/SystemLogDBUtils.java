package org.example.restlogisticserp.database;

import org.example.restlogisticserp.DatabaseConnection;
import org.example.restlogisticserp.models.SystemLog;
import org.example.restlogisticserp.models.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemLogDBUtils {
    private static final Logger logger = Logger.getLogger(UserDBUtils.class.getName());
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

    //create a new system log
    public static void createSystemLog(String severity, String source, String ipAddress, String message, int userId) {
        try {
            String query = "INSERT INTO system_logs (log_timestamp, severity, source, ip_address, message, user_id) VALUES (NOW(), ?, ?, ?, ?, ?)";
            var statement = connection.prepareStatement(query);
            statement.setString(1, severity);
            statement.setString(2, source);
            statement.setString(3, ipAddress);
            statement.setString(4, message);
            statement.setLong(5, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating system log", e);
            throw new RuntimeException(e);
        }
    }

    //fetch all system logs
    public static List<SystemLog> fetchAllSystemLogs() {
        List<SystemLog> logs = new ArrayList<>();
        try {
            String query = "SELECT * FROM system_logs";
            var statement = connection.prepareStatement(query);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int logId = resultSet.getInt("log_id");
                Timestamp logTimestamp = resultSet.getTimestamp("log_timestamp");
                String severity = resultSet.getString("severity");
                String source = resultSet.getString("source");
                String ipAddress = resultSet.getString("ip_address");
                String message = resultSet.getString("message");
                int userId = resultSet.getInt("user_id");

                SystemLog log = new SystemLog(logId, logTimestamp, severity, source, ipAddress, message, userId);
                logs.add(log);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching system logs", e);
            throw new RuntimeException(e);
        }
        return logs;
    }

}

/*
CREATE TABLE IF NOT EXISTS system_logs (
    log_id SERIAL AUTO_INCREMENT PRIMARY KEY,
    log_timestamp TIMESTAMP NOT NULL,
    severity VARCHAR(20) NOT NULL, -- Changed from TEXT to VARCHAR with a specified length
    source TEXT NOT NULL,
    ip_address VARCHAR(45), -- Additional: IP Address
    message TEXT NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    INDEX idx_log_timestamp (log_timestamp), -- Index: Log Timestamp
    INDEX idx_severity (severity(10)), -- Index: Severity with a specified key length
    INDEX idx_user_id (user_id) -- Index: User ID
);
 */