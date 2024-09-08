package org.example.restlogisticserp.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.database.BidDBUtils;
import org.example.restlogisticserp.models.Bid;
import org.example.restlogisticserp.models.BidSummary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/bids")
public class BidServices {
    private static final Logger logger = Logger.getLogger(BidServices.class.getName());

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBid(Bid bid) {
        try {
            String result = BidDBUtils.placeBid(bid);
            if (result.equals("Bid placed successfully.")) {
                return Response.ok(bid).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error creating bid", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/{bidId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBid(@PathParam("bidId") Long bidId) {
        try {
            Bid bid = BidDBUtils.fetchBidById(bidId);
            if (bid != null) {
                return Response.ok(bid).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Bid not found").build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching bid", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PUT
    @Path("/update/{bidId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBidAmount(@PathParam("bidId") Long bidId, BigDecimal amount) {
        try {
            // Process the bid update with the provided amount
            String result = BidDBUtils.updateBidAmount(bidId, amount);
            if (result.equals("Bid amount updated successfully.")) {
                return Response.ok().entity(result).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating bid amount", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @GET
    @Path("/company/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBidsForCompany(@PathParam("companyId") Long companyId) {
        try {
            List<Bid> bids = BidDBUtils.fetchBidsForCompany(companyId);
            return Response.ok(bids).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching bids for company", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/summary/shipping-company/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBidsSummaryByShippingCompany(@PathParam("companyId") Long companyId) {
        try {
            BidSummary summary = BidDBUtils.fetchBidsSummaryByShippingCompany(companyId);
            return Response.ok(summary).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching bids summary for shipping company", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/summary/customer-company/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBidsSummaryCustomerCompany(@PathParam("companyId") Long companyId) {
        try {
            BidSummary summary = BidDBUtils.fetchBidsSummaryCustomerCompany(companyId);
            return Response.ok(summary).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching bids summary for customer company", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/inquiry/{inquiryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBidsForInquiry(@PathParam("inquiryId") Long inquiryId) {
        try {
            List<Bid> bids = BidDBUtils.fetchBidsForInquiry(inquiryId);
            return Response.ok(bids).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching bids for inquiry", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/close/{bidId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response closeBid(@PathParam("bidId") Long bidId) {
        try {
            String result = BidDBUtils.closeBid(bidId);
            if (result.equals("Bid closed successfully.")) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error closing bid", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //check places bid by company and inquiry
    @GET
    @Path("/company/{companyId}/inquiry/{inquiryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBidsForCompanyAndInquiry(@PathParam("companyId") Integer companyId, @PathParam("inquiryId") Integer inquiryId) {
        try {
            List<Bid> bids = BidDBUtils.fetchBidsForCompanyAndInquiry(companyId, inquiryId);
            return Response.ok(bids).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error fetching bids for company and inquiry", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
