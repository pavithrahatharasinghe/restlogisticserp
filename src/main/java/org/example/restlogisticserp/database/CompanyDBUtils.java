package org.example.restlogisticserp.database;

import org.example.restlogisticserp.DatabaseConnection;
import org.example.restlogisticserp.models.Company;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompanyDBUtils {

    private static final Logger logger = Logger.getLogger(CompanyDBUtils.class.getName());
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

    public static String addCompany(Company company) {
        String query = "INSERT INTO companies (name, email, phone, website, industry_type, address_line1, address_line2, city, state_or_province, postal_code, country, manager_name, manager_email, manager_phone, fax, alternate_phone, annual_revenue, tax_id, company_size, linkedin_profile, twitter_profile, facebook_profile, company_logo, company_description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, company.getName());
            statement.setString(2, company.getEmail());
            statement.setString(3, company.getPhone());
            statement.setString(4, company.getWebsite());
            statement.setString(5, company.getIndustryType());
            statement.setString(6, company.getAddressLine1());
            statement.setString(7, company.getAddressLine2());
            statement.setString(8, company.getCity());
            statement.setString(9, company.getStateOrProvince());
            statement.setString(10, company.getPostalCode());
            statement.setString(11, company.getCountry());
            statement.setString(12, company.getManagerName());
            statement.setString(13, company.getManagerEmail());
            statement.setString(14, company.getManagerPhone());
            statement.setString(15, company.getFax());
            statement.setString(16, company.getAlternatePhone());
            statement.setBigDecimal(17, company.getAnnualRevenue());
            statement.setString(18, company.getTaxId());
            statement.setInt(19, company.getCompanySize());
            statement.setString(20, company.getLinkedinProfile());
            statement.setString(21, company.getTwitterProfile());
            statement.setString(22, company.getFacebookProfile());
            statement.setString(23, company.getCompanyLogo());
            statement.setString(24, company.getCompanyDescription());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return "Company created with ID: " + generatedKeys.getLong(1);
                    }
                }
            }

            throw new SQLException("Creating company failed, no rows affected.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error creating company: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error creating company: " + e.getMessage(), e);
        }
    }

    public static List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT * FROM companies";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Company company = new Company();
                company.setCompanyId(resultSet.getLong("company_id"));
                company.setName(resultSet.getString("name"));
                company.setEmail(resultSet.getString("email"));
                company.setPhone(resultSet.getString("phone"));
                company.setWebsite(resultSet.getString("website"));
                company.setIndustryType(resultSet.getString("industry_type"));
                company.setAddressLine1(resultSet.getString("address_line1"));
                company.setAddressLine2(resultSet.getString("address_line2"));
                company.setCity(resultSet.getString("city"));
                company.setStateOrProvince(resultSet.getString("state_or_province"));
                company.setPostalCode(resultSet.getString("postal_code"));
                company.setCountry(resultSet.getString("country"));
                company.setManagerName(resultSet.getString("manager_name"));
                company.setManagerEmail(resultSet.getString("manager_email"));
                company.setManagerPhone(resultSet.getString("manager_phone"));
                company.setFax(resultSet.getString("fax"));
                company.setAlternatePhone(resultSet.getString("alternate_phone"));
                company.setAnnualRevenue(resultSet.getBigDecimal("annual_revenue"));
                company.setTaxId(resultSet.getString("tax_id"));
                company.setCompanySize(resultSet.getInt("company_size"));
                company.setLinkedinProfile(resultSet.getString("linkedin_profile"));
                company.setTwitterProfile(resultSet.getString("twitter_profile"));
                company.setFacebookProfile(resultSet.getString("facebook_profile"));
                company.setCompanyLogo(resultSet.getString("company_logo"));
                company.setCompanyDescription(resultSet.getString("company_description"));
                companies.add(company);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error retrieving companies: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error retrieving companies: " + e.getMessage(), e);
        }

        return companies;
    }

    public static Company getCompanyById(long id) {
        String query = "SELECT * FROM companies WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Company company = new Company();
                    company.setCompanyId(resultSet.getLong("company_id"));
                    company.setName(resultSet.getString("name"));
                    company.setEmail(resultSet.getString("email"));
                    company.setPhone(resultSet.getString("phone"));
                    company.setWebsite(resultSet.getString("website"));
                    company.setIndustryType(resultSet.getString("industry_type"));
                    company.setAddressLine1(resultSet.getString("address_line1"));
                    company.setAddressLine2(resultSet.getString("address_line2"));
                    company.setCity(resultSet.getString("city"));
                    company.setStateOrProvince(resultSet.getString("state_or_province"));
                    company.setPostalCode(resultSet.getString("postal_code"));
                    company.setCountry(resultSet.getString("country"));
                    company.setManagerName(resultSet.getString("manager_name"));
                    company.setManagerEmail(resultSet.getString("manager_email"));
                    company.setManagerPhone(resultSet.getString("manager_phone"));
                    company.setFax(resultSet.getString("fax"));
                    company.setAlternatePhone(resultSet.getString("alternate_phone"));
                    company.setAnnualRevenue(resultSet.getBigDecimal("annual_revenue"));
                    company.setTaxId(resultSet.getString("tax_id"));
                    company.setCompanySize(resultSet.getInt("company_size"));
                    company.setLinkedinProfile(resultSet.getString("linkedin_profile"));
                    company.setTwitterProfile(resultSet.getString("twitter_profile"));
                    company.setFacebookProfile(resultSet.getString("facebook_profile"));
                    company.setCompanyLogo(resultSet.getString("company_logo"));
                    company.setCompanyDescription(resultSet.getString("company_description"));
                    return company;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error retrieving company by ID: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error retrieving company by ID: " + e.getMessage(), e);
        }

        return null;
    }

    public static String updateCompanyBasicDetails(long id, String name, String email, String phone, String website, String industryType) {
        String query = "UPDATE companies SET name = ?, email = ?, phone = ?, website = ?, industry_type = ? WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, website);
            statement.setString(5, industryType);
            statement.setLong(6, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company details updated successfully.";
            } else {
                throw new SQLException("Updating company details failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating company basic details: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error updating company basic details: " + e.getMessage(), e);
        }
    }

    public static String updateCompanyAddress(long id, String addressLine1, String addressLine2, String city, String stateOrProvince, String postalCode, String country) {
        String query = "UPDATE companies SET address_line1 = ?, address_line2 = ?, city = ?, state_or_province = ?, postal_code = ?, country = ? WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, addressLine1);
            statement.setString(2, addressLine2);
            statement.setString(3, city);
            statement.setString(4, stateOrProvince);
            statement.setString(5, postalCode);
            statement.setString(6, country);
            statement.setLong(7, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company address updated successfully.";
            } else {
                throw new SQLException("Updating company address failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating company address: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error updating company address: " + e.getMessage(), e);
        }
    }

    public static String updateCompanyOtherDetails(long id, String fax, String alternatePhone, double annualRevenue, String taxId, int companySize) {
        String query = "UPDATE companies SET fax = ?, alternate_phone = ?, annual_revenue = ?, tax_id = ?, company_size = ? WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fax);
            statement.setString(2, alternatePhone);
            statement.setBigDecimal(3, BigDecimal.valueOf(annualRevenue));
            statement.setString(4, taxId);
            statement.setInt(5, companySize);
            statement.setLong(6, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company other details updated successfully.";
            } else {
                throw new SQLException("Updating company other details failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating company other details: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error updating company other details: " + e.getMessage(), e);
        }
    }

    public static String updateSocialMediaProfiles(long id, String linkedinProfile, String twitterProfile, String facebookProfile) {
        String query = "UPDATE companies SET linkedin_profile = ?, twitter_profile = ?, facebook_profile = ? WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, linkedinProfile);
            statement.setString(2, twitterProfile);
            statement.setString(3, facebookProfile);
            statement.setLong(4, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company social media profiles updated successfully.";
            } else {
                throw new SQLException("Updating social media profiles failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating social media profiles: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error updating social media profiles: " + e.getMessage(), e);
        }
    }

    public static String updateCompanyLogo(long id, String logoPath) {
        String query = "UPDATE companies SET company_logo = ? WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, logoPath);
            statement.setLong(2, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company logo updated successfully.";
            } else {
                throw new SQLException("Updating company logo failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating company logo: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error updating company logo: " + e.getMessage(), e);
        }
    }

    public static String deleteCompany(long id) {
        String query = "DELETE FROM companies WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company deleted successfully.";
            } else {
                throw new SQLException("Deleting company failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error deleting company: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error deleting company: " + e.getMessage(), e);
        }
    }

    public static List<Company> getCompaniesBasicInfo() {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT company_id, name, email, phone, website, industry_type FROM companies";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Company company = new Company();
                company.setCompanyId(resultSet.getLong("company_id"));
                company.setName(resultSet.getString("name"));
                company.setEmail(resultSet.getString("email"));
                company.setPhone(resultSet.getString("phone"));
                company.setWebsite(resultSet.getString("website"));
                company.setIndustryType(resultSet.getString("industry_type"));
                companies.add(company);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error retrieving basic info of companies: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error retrieving basic info of companies: " + e.getMessage(), e);
        }

        return companies;
    }

    public static String updateCompany(Company company) {
        String query = "UPDATE companies SET name = ?, email = ?, phone = ?, website = ?, industry_type = ?, address_line1 = ?, address_line2 = ?, city = ?, state_or_province = ?, postal_code = ?, country = ?, manager_name = ?, manager_email = ?, manager_phone = ?, fax = ?, alternate_phone = ?, annual_revenue = ?, tax_id = ?, company_size = ?, linkedin_profile = ?, twitter_profile = ?, facebook_profile = ?, company_logo = ?, company_description = ? WHERE company_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, company.getName());
            statement.setString(2, company.getEmail());
            statement.setString(3, company.getPhone());
            statement.setString(4, company.getWebsite());
            statement.setString(5, company.getIndustryType());
            statement.setString(6, company.getAddressLine1());
            statement.setString(7, company.getAddressLine2());
            statement.setString(8, company.getCity());
            statement.setString(9, company.getStateOrProvince());
            statement.setString(10, company.getPostalCode());
            statement.setString(11, company.getCountry());
            statement.setString(12, company.getManagerName());
            statement.setString(13, company.getManagerEmail());
            statement.setString(14, company.getManagerPhone());
            statement.setString(15, company.getFax());
            statement.setString(16, company.getAlternatePhone());
            statement.setBigDecimal(17, company.getAnnualRevenue());
            statement.setString(18, company.getTaxId());
            statement.setInt(19, company.getCompanySize());
            statement.setString(20, company.getLinkedinProfile());
            statement.setString(21, company.getTwitterProfile());
            statement.setString(22, company.getFacebookProfile());
            statement.setString(23, company.getCompanyLogo());
            statement.setString(24, company.getCompanyDescription());
            statement.setLong(25, company.getCompanyId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company updated successfully.";
            } else {
                throw new SQLException("Updating company failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating company: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error updating company: " + e.getMessage(), e);
        }
    }
}
