package org.example.restlogisticserp.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.database.UserDBUtils;
import org.example.restlogisticserp.models.User;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(
            @PathParam("userId") int userId,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        String fileExtension = fileDetail.getFileName().substring(fileDetail.getFileName().lastIndexOf("."));
        String fileName = "user-" + userId + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + fileExtension;
        String uploadedFileLocation = UPLOAD_DIR + fileName;

        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);

            String picturePath = "/uploads/userProfile/" + fileName;
            User updatedUser = UserDBUtils.updateProfilePicture(userId, picturePath);

            if (updatedUser != null) {
                return Response.ok(updatedUser).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error uploading profile picture", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{userId}/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserProfile(
            @PathParam("userId") int userId,
            User user) {

        UserDBUtils.updateUserProfile(userId, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getAboutMe());
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
            @QueryParam("password") String newPassword) {

        // Hash the password as per your security practices
        String hashedPassword = hashPassword(newPassword); // Implement hashPassword as needed
        UserDBUtils.updateUserPassword(userId, hashedPassword);
        return Response.ok("Password updated successfully").build();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userId") int userId) {
        User user = UserDBUtils.getUserById(userId);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }


    private void saveToFile(InputStream uploadedInputStream, String serverLocation) throws Exception {
        try (FileOutputStream out = new FileOutputStream(new File(serverLocation))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        }
    }

    private String hashPassword(String password) {
        // Implement your password hashing logic here
        return password; // Replace with actual hashed password
    }
}
