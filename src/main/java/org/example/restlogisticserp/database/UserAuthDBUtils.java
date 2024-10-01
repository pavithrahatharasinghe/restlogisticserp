package org.example.restlogisticserp.database;

import org.example.restlogisticserp.DatabaseConnection;
import org.example.restlogisticserp.models.User;
import org.example.restlogisticserp.models.UserAuth;
import org.example.restlogisticserp.models.UserAuthResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.UUID; // Importing UUID for sessionId generation
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAuthDBUtils {
    private static final Logger logger = Logger.getLogger(UserAuthDBUtils.class.getName());
    private static Connection connection;

    // Static initialization block for connection setup
    static {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            logger.info("Database connection established successfully in UserAuthDBUtils.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error establishing database connection", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during database connection setup", e);
            throw new RuntimeException(e);
        }
    }

    // Ensure connection is valid
    private static void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
            logger.info("Re-established the database connection.");
        }
    }

    // Method to generate a random token of 200 characters
    private static String generateTokenPlaceholder() {
        StringBuilder token = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 200; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            token.append(characters.charAt(randomIndex));
        }
        return token.toString();
    }

    // Example method to authenticate the user using session token
    public static UserAuthResponse getUserAuthBySessionToken(String email, String plainPassword, String userAgent, String ipAddress) throws SQLException {
        checkConnection();
        String userQuery = "SELECT * FROM users WHERE email = ?";
        String sessionInsertQuery = "INSERT INTO userauth (sessionId, userId, lastActivity, userAgent, ipAddress, sessionExpired, sessionToken, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
            userStmt.setString(1, email);
            ResultSet userResultSet = userStmt.executeQuery();

            if (userResultSet.next()) {
                String hashedPassword = userResultSet.getString("password");
                if (BCrypt.checkpw(plainPassword, hashedPassword)) {
                    // Populate User model
                    User user = new User(
                            userResultSet.getInt("user_id"),
                            userResultSet.getString("email"),
                            userResultSet.getString("role"),
                            userResultSet.getString("first_name"),
                            userResultSet.getString("last_name"),
                            userResultSet.getString("title"),
                            userResultSet.getString("about_me"),
                            userResultSet.getString("phone_number"),
                            userResultSet.getString("profile_pic"),
                            userResultSet.getBoolean("email_verified"),
                            userResultSet.getInt("company_id")
                    );

                    // Create a new session for the authenticated user
                    try (PreparedStatement sessionStmt = connection.prepareStatement(sessionInsertQuery)) {
                        String generatedSessionId = UUID.randomUUID().toString(); // Generate sessionId
                        sessionStmt.setString(1, generatedSessionId); // Set sessionId
                        sessionStmt.setInt(2, user.getUserId()); // userId
                        sessionStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // lastActivity
                        sessionStmt.setString(4, userAgent); // userAgent
                        sessionStmt.setString(5, ipAddress); // ipAddress
                        sessionStmt.setBoolean(6, false); // sessionExpired
                        sessionStmt.setString(7, generateTokenPlaceholder()); // sessionToken
                        sessionStmt.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // createdAt

                        int rowsAffected = sessionStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            // Populate UserAuth model with session data
                            UserAuth userAuth = new UserAuth(
                                    generatedSessionId,  // Session ID
                                    user.getUserId(),
                                    new Timestamp(System.currentTimeMillis()), // Last activity as current time
                                    userAgent,
                                    ipAddress,
                                    false,  // sessionExpired (false by default for a new session)
                                    generateTokenPlaceholder(),  // Placeholder for session token (could be JWT or similar)
                                    new Timestamp(System.currentTimeMillis()) // CreatedAt as current time
                            );

                            logger.info("User authenticated and session created successfully.");
                            return new UserAuthResponse(user, userAuth); // Returning both User and UserAuth
                        } else {
                            logger.warning("Failed to create new session.");
                            return null;
                        }
                    }
                } else {
                    logger.warning("Invalid password for email: " + email);
                    return null;
                }
            } else {
                logger.warning("No user found with email: " + email);
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred while authenticating user", e);
            throw e;
        }
    }
}

