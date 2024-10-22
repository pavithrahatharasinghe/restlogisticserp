package org.example.restlogisticserp.services;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.servlet.http.HttpServletRequest; // Import HttpServletRequest
import org.example.restlogisticserp.database.UserAuthDBUtils;
import org.example.restlogisticserp.models.UserAuthLoginRequest;
import org.example.restlogisticserp.models.UserAuth;
import org.example.restlogisticserp.models.UserAuthResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/auth")
public class UserAuthServices {

    private static final Logger logger = Logger.getLogger(UserAuthServices.class.getName());

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(UserAuthLoginRequest loginRequest, @Context HttpServletRequest request) {
        try {
            logger.info("Received authentication request for email: " + loginRequest.getEmail());

            // Obtain user agent and IP address from request headers
            String userAgent = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();

            UserAuthResponse userAuth = UserAuthDBUtils.getUserAuthBySessionToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword(),
                    userAgent,
                    ipAddress
            );

            if (userAuth != null) {
                logger.info("User authenticated successfully for email: " + loginRequest.getEmail());
                return Response.ok(userAuth).build();
            } else {
                logger.warning("Authentication failed for email: " + loginRequest.getEmail());
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Server error during authentication", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during authentication", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unexpected error: " + e.getMessage()).build();
        }
    }
}
