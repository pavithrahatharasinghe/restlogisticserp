package org.example.restlogisticserp.database;

import org.example.restlogisticserp.DatabaseConnection;
import org.example.restlogisticserp.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.*;

public class UserDBUtils {
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

    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getInt("user_id"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("title"),
                    resultSet.getString("about_me"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("profile_pic"),
                    resultSet.getBoolean("email_verified"),
                    resultSet.getInt("company_id")
            );
            users.add(user);
        }
        return users;
    }



    public static User authenticateUser(String email, String plainPassword) throws SQLException {
        // SQL query to select user details based on the email
        String query = "SELECT * FROM Users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the hashed password from the database
                String hashedPassword = resultSet.getString("password");

                // Verify the provided plain password against the hashed password
                if (BCrypt.checkpw(plainPassword, hashedPassword)) {
                    // Password matches, create and return the User object
                    return new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("email"),
                            resultSet.getString("role"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("title"),
                            resultSet.getString("about_me"),
                            resultSet.getString("phone_number"),
                            resultSet.getString("profile_pic"),
                            resultSet.getBoolean("email_verified"),
                            resultSet.getInt("company_id")
                    );
                } else {
                    // Password does not match
                    logger.warning("Invalid password for email: " + email);
                    return null; // Invalid credentials
                }
            } else {
                // No user found with the given email
                logger.warning("No user found with email: " + email);
                return null; // User not found
            }
        }
    }

    public static User getUserById(int userId) {
        // Implement this method to fetch a user by ID
        String query = "SELECT * FROM Users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("title"),
                        resultSet.getString("about_me"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("profile_pic"),
                        resultSet.getBoolean("email_verified"),
                        resultSet.getInt("company_id")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching user by ID", e);
            throw new RuntimeException("Error fetching user by ID", e);
        }
    }

    public static User updateProfilePicture(int userId, String picturePath) {
        String updateQuery = "UPDATE Users SET profile_pic = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setString(1, picturePath);
            ps.setInt(2, userId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                // Fetch and return the updated user
                return getUserById(userId);
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating profile picture", e);
            throw new RuntimeException("Error updating profile picture", e);
        }
    }

    public static void updateUserProfile(int userId, String firstName, String lastName, String email, String phoneNumber, String aboutMe) {
        String updateQuery = "UPDATE Users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, about_me = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setString(5, aboutMe);
            ps.setInt(6, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user profile", e);
            throw new RuntimeException("Error updating user profile", e);
        }
    }

    public static void updateUserPassword(int userId, String hashedPassword) {
        String updateQuery = "UPDATE Users SET password = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user password", e);
            throw new RuntimeException("Error updating user password", e);
        }
    }

    //create a new user
    public static User createUser(User user) {
        String insertQuery = "INSERT INTO Users (email, password, role, first_name, last_name, title, about_me, phone_number, profile_pic, email_verified, company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());

            // Hash the password before storing it in the database
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            ps.setString(2, hashedPassword);

            ps.setString(3, user.getRole());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getTitle());
            ps.setString(7, user.getAboutMe());
            ps.setString(8, user.getPhoneNumber());
            ps.setString(9, user.getProfilePic());
            ps.setBoolean(10, user.isEmailVerified());
            ps.setInt(11, user.getCompanyId());

            ps.executeUpdate();

            // Retrieve the generated user ID
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                return getUserById(userId);
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating user", e);
            throw new RuntimeException("Error creating user", e);
        }
    }





}
