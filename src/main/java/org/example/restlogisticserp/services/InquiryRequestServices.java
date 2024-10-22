package org.example.restlogisticserp.services;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.models.InquiryRequest;
import org.example.restlogisticserp.database.InquiryRequestDBUtils;
import jakarta.ws.rs.*;


import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/inquiryRequests")
public class InquiryRequestServices {
    private static final Logger logger = Logger.getLogger(InquiryRequestServices.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInquiryRequest(InquiryRequest inquiryRequest) {
        try {
            InquiryRequest createdRequest = InquiryRequestDBUtils.insertInquiryRequest(inquiryRequest);
            return Response.status(Response.Status.CREATED).entity(createdRequest).build();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating inquiry request", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllInquiryRequests() {
        try {
            List<InquiryRequest> inquiryRequests = InquiryRequestDBUtils.fetchAllInquiryRequests();
            return Response.ok(inquiryRequests).build();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching inquiry requests", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Server error: " + e.getMessage()).build();
        }
    }
}
