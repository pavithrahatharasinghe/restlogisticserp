package org.example.restlogisticserp.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.database.InquiryDBUtils;
import org.example.restlogisticserp.models.Inquiry;
import org.example.restlogisticserp.models.InquirySearchParams;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/inquiries")
public class InquiryServices {
    private static final Logger logger = Logger.getLogger(InquiryServices.class.getName());




    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInquiry(Inquiry inquiry) {
        try {
            Inquiry createdInquiry = InquiryDBUtils.createInquiry(inquiry);
            return Response.ok(createdInquiry).build();
        } catch (RuntimeException e) {  // Catch RuntimeException since InquiryDBUtils throws it
            logger.log(Level.SEVERE, "Error creating inquiry", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/drafts/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDraftInquiries(@PathParam("companyId") int companyId) {
        try {
            var inquiries = InquiryDBUtils.fetchDraftInquiriesByCustomer(companyId);
            return Response.ok(inquiries).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching draft inquiries", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{companyId}/{inquiryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInquiryById(@PathParam("companyId") int companyId, @PathParam("inquiryId") int inquiryId) {
        try {
            Inquiry inquiry = InquiryDBUtils.fetchInquiryById(companyId, inquiryId);
            if (inquiry != null) {
                return Response.ok(inquiry).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Inquiry not found").build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching inquiry by ID", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{inquiryId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateInquiry(@PathParam("inquiryId") int inquiryId, Inquiry inquiry) {
        try {
            boolean updated = InquiryDBUtils.updateInquiry(inquiryId, inquiry);
            if (updated) {
                return Response.ok(inquiry).build();  // Returns the updated inquiry object
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Inquiry not found").build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating inquiry", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Server error: " + e.getMessage()).build();
        }
    }



    @GET
    @Path("/published/customer/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublishedInquiriesByCustomer(@PathParam("companyId") int companyId) {
        try {
            var inquiries = InquiryDBUtils.fetchInquiriesByCustomer(companyId);
            return Response.ok(inquiries).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching published inquiries by customer", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/published")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublishedInquiriesByCompany() {
        try {
            var inquiries = InquiryDBUtils.fetchInquiriesByCompany();
            return Response.ok(inquiries).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching published inquiries by company", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/published/{inquiryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublishedInquiryById(@PathParam("inquiryId") int inquiryId) {
        try {
            Inquiry inquiry = InquiryDBUtils.fetchInquiryByIdByCompany(inquiryId);
            if (inquiry != null) {
                return Response.ok(inquiry).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Inquiry not found").build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching published inquiry by ID", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchInquiries(InquirySearchParams searchParams) {
        try {
            List<Inquiry> inquiries = InquiryDBUtils.searchInquiries(searchParams);
            return Response.ok(inquiries).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error searching inquiries", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }




}
