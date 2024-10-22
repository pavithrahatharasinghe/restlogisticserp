package org.example.restlogisticserp.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.database.UserDBUtils;
import org.example.restlogisticserp.models.User;
import org.example.restlogisticserp.models.UserPasswordUpdateRequest;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.mindrot.jbcrypt.BCrypt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Base64;

@Path("/users")
public class UserServices {
    private static final Logger logger = Logger.getLogger(UserServices.class.getName());
    private static final String UPLOAD_DIR = "D:/uploads/";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        try {
            List<User> users = UserDBUtils.getAllUsers();
            return Response.ok(users).build();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching users", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) throws SQLException {
        User newUser = UserDBUtils.createUser(user);
        if (newUser != null) {
            return Response.ok(newUser).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User creation failed").build();
        }
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(User loginRequest) {
        try {
            logger.info("Received authentication request for email: " + loginRequest.getEmail());

            User authenticatedUser = UserDBUtils.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (authenticatedUser != null) {
                logger.info("User authenticated successfully");
                return Response.ok(authenticatedUser).build();
            } else {
                logger.warning("Authentication failed for email: " + loginRequest.getEmail());
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Server error during authentication", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/{userId}/profile-picture")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(
            @PathParam("userId") int userId,
            String base64Image) {

        if (base64Image == null || base64Image.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid image input").build();
        }

        try {
            // Handle Base64 string, stripping off any Data URI prefix if present
            String[] parts = base64Image.split(",");
            String base64String = parts.length > 1 ? parts[1] : parts[0];

            // Update the profile picture in the database
            User updatedUser = UserDBUtils.updateProfilePictureBase64(userId, base64String);

            if (updatedUser != null) {
                return Response.ok("Profile picture updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error uploading profile picture", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    //update with  profile-picture path user id and profile picture path form root don ot upload comes only id and path as string
    @PUT
    @Path("/{userId}/profile-picture-file-path")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserProfile(
            @PathParam("userId") int userId,
            String profilePicturePath) throws SQLException {

        UserDBUtils.updateUserProfilePicturePath(userId, profilePicturePath);
        User updatedUser = UserDBUtils.getUserById(userId);
        if (updatedUser != null) {
            return Response.ok(updatedUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @PUT
    @Path("/{userId}/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserProfile(
            @PathParam("userId") int userId,
            User user) throws SQLException {

        // Include title when calling the database method
        UserDBUtils.updateUserProfile(userId, user.getTitle(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(),user.getAboutMe());
        User updatedUser = UserDBUtils.getUserById(userId);
        if (updatedUser != null) {
            return Response.ok(updatedUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }


    @PUT
    @Path("/{userId}/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserPassword(
            @PathParam("userId") int userId,
            UserPasswordUpdateRequest passwordUpdateRequest) {  // Accept a custom object for JSON payload

        try {
            String currentPassword = passwordUpdateRequest.getCurrentPassword();
            String newPassword = passwordUpdateRequest.getNewPassword();
            String confirmPassword = passwordUpdateRequest.getConfirmPassword();

            // Step 1: Check if any password field is null
            if (currentPassword == null || newPassword == null || confirmPassword == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("All password fields must be provided.").build();
            }

            // Step 2: Check if new password and confirm password match
            if (!newPassword.equals(confirmPassword)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("New password and confirm password do not match.").build();
            }

            // Step 3: Retrieve the current stored password hash from the database
            String storedHashedPassword = UserDBUtils.getUserPassword(userId);

            // Step 4: Verify that the current password provided matches the stored password
            if (!verifyPassword(currentPassword, storedHashedPassword)) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Current password is incorrect.").build();
            }

            // Step 5: Hash the new password
            String hashedNewPassword = hashPassword(newPassword);

            // Step 6: Update the user's password in the database
            UserDBUtils.updateUserPassword(userId, hashedNewPassword);

            return Response.ok("Password updated successfully").build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating password.").build();
        }
    }


    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Implement your password verification logic here
        // For example, using BCrypt:
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }




    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userId") int userId) throws SQLException {
        User user = UserDBUtils.getUserById(userId);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }



    //add new user
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(User user) throws SQLException {
        User newUser = UserDBUtils.createUser(user);
        if (newUser != null) {
            return Response.ok(newUser).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User creation failed").build();
        }
    }

    //update user
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User user) throws SQLException {
        User updatedUser = UserDBUtils.updateUser(user);
        if (updatedUser != null) {
            return Response.ok(updatedUser).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User update failed").build();
        }
    }
    @PUT
    @Path("/{userId}/status")
    public Response updateUserStatus(@PathParam("userId") int userId, @QueryParam("status") String status) {
        // Validate the status
        if (!"active".equals(status) && !"inactive".equals(status)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid status value. Use 'active' or 'inactive'.\"}")
                    .build();
        }

        try {
            UserDBUtils.changeUserStatus(userId, status); // Call your existing method
            return Response.ok("{\"message\": \"User status updated successfully\"}").build();
        } catch (SQLException e) {
            // Handle SQL exception
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to update user status\"}")
                    .build();
        } catch (RuntimeException e) {
            // Handle runtime exception
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
