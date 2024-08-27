package org.example.restlogisticserp.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.database.CompanyDBUtils;
import org.example.restlogisticserp.models.Company;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/companies")
public class CompanyServices {
    private static final Logger logger = Logger.getLogger(CompanyServices.class.getName());

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompany(Company company) {
        try {
            String result = CompanyDBUtils.addCompany(company);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error creating company", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCompanies() {
        try {
            List<Company> companies = CompanyDBUtils.getCompanies();
            return Response.ok(companies).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error retrieving companies", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/basic-info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompaniesBasicInfo() {
        try {
            List<Company> companies = CompanyDBUtils.getCompaniesBasicInfo();
            return Response.ok(companies).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error retrieving basic info of companies", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompanyById(@PathParam("id") long id) {
        try {
            Company company = CompanyDBUtils.getCompanyById(id);
            if (company != null) {
                return Response.ok(company).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Company not found").build();
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error retrieving company by ID", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompany(Company company) {
        try {
            String result = CompanyDBUtils.updateCompany(company);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating company", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCompany(@PathParam("id") long id) {
        try {
            String result = CompanyDBUtils.deleteCompany(id);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error deleting company", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update-basic-details/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompanyBasicDetails(@PathParam("id") long id, @QueryParam("name") String name, @QueryParam("email") String email, @QueryParam("phone") String phone, @QueryParam("website") String website, @QueryParam("industryType") String industryType) {
        try {
            String result = CompanyDBUtils.updateCompanyBasicDetails(id, name, email, phone, website, industryType);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating company basic details", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update-address/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompanyAddress(@PathParam("id") long id, @QueryParam("addressLine1") String addressLine1, @QueryParam("addressLine2") String addressLine2, @QueryParam("city") String city, @QueryParam("stateOrProvince") String stateOrProvince, @QueryParam("postalCode") String postalCode, @QueryParam("country") String country) {
        try {
            String result = CompanyDBUtils.updateCompanyAddress(id, addressLine1, addressLine2, city, stateOrProvince, postalCode, country);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating company address", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update-other-details/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompanyOtherDetails(@PathParam("id") long id, @QueryParam("fax") String fax, @QueryParam("alternatePhone") String alternatePhone, @QueryParam("annualRevenue") double annualRevenue, @QueryParam("taxId") String taxId, @QueryParam("companySize") int companySize) {
        try {
            String result = CompanyDBUtils.updateCompanyOtherDetails(id, fax, alternatePhone, annualRevenue, taxId, companySize);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating company other details", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update-social-media/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSocialMediaProfiles(@PathParam("id") long id, @QueryParam("linkedinProfile") String linkedinProfile, @QueryParam("twitterProfile") String twitterProfile, @QueryParam("facebookProfile") String facebookProfile) {
        try {
            String result = CompanyDBUtils.updateSocialMediaProfiles(id, linkedinProfile, twitterProfile, facebookProfile);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating social media profiles", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update-logo/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompanyLogo(@PathParam("id") long id, @QueryParam("logoPath") String logoPath) {
        try {
            String result = CompanyDBUtils.updateCompanyLogo(id, logoPath);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error updating company logo", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error: " + e.getMessage()).build();
        }
    }
}
