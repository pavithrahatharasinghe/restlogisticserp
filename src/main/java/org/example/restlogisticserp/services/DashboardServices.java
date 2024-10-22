package org.example.restlogisticserp.services;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.database.BidDBUtils;
import org.example.restlogisticserp.database.InquiryDBUtils; // Import InquiryDBUtils
import org.example.restlogisticserp.database.UserDBUtils;
import org.example.restlogisticserp.database.CompanyDBUtils; // Import CompanyDBUtils
import org.example.restlogisticserp.models.BidSummary;
import org.example.restlogisticserp.models.Company;
import org.example.restlogisticserp.models.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/dashboard")
public class DashboardServices {
    private static final Logger logger = Logger.getLogger(DashboardServices.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashboardData() {
        try {
            List<User> users = UserDBUtils.getAllUsers();
            int totalUsers = users.size(); // Total Users

            // Fetch total companies directly using getCompanies
            List<Company> companies = CompanyDBUtils.getCompanies(); // Get all companies
            int totalCompanies = companies.size(); // Calculate total companies

            // Fetch total inquiries
            int totalInquiries = InquiryDBUtils.getTotalInquiries(); // New method to fetch total inquiries

            // Fetch total bids
            BidSummary bidSummary = BidDBUtils.fetchBidsSummaryCustomerCompany(null);
            int totalBids = bidSummary != null ? bidSummary.getTotalCount() : 0; // Use the total count from BidSummary

            // Create a response object
            Map<String, Integer> dashboardData = new HashMap<>();
            dashboardData.put("totalUsers", totalUsers);
            dashboardData.put("totalCompanies", totalCompanies); // Include total companies
            dashboardData.put("totalInquiries", totalInquiries); // Include total inquiries
            dashboardData.put("totalBids", totalBids); // Include total bids

            return Response.ok(dashboardData).build();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching dashboard data", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }
}
