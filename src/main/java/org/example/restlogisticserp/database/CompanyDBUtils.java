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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error establishing database connection", e);
            throw new RuntimeException("Database connection initialization failed.", e);
        }
    }

    private static void checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getInstance().getConnection();
        }
    }

    public static String addCompany(Company company) throws SQLException {
        checkConnection();
        String query = "INSERT INTO companies (name, email, phone, website, industry_type, address_line1, address_line2, city, state_or_province, postal_code, country, manager_name, manager_email, manager_phone, fax, alternate_phone, annual_revenue, tax_id, company_size, linkedin_profile, twitter_profile, facebook_profile, company_logo, company_description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setCompanyParameters(statement, company);

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
        }
    }

    public static List<Company> getCompanies() throws SQLException {
        checkConnection();
        List<Company> companies = new ArrayList<>();
        String query = "SELECT * FROM companies";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                companies.add(mapResultSetToCompany(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error retrieving companies: " + e.getMessage(), e);
        }
        return companies;
    }

    public static Company getCompanyById(long id) throws SQLException {
        checkConnection();
        String query = "SELECT * FROM companies WHERE company_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCompany(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error retrieving company by ID: " + e.getMessage(), e);
        }
        return null;
    }

    public static String updateCompanyBasicDetails(long id, String name, String email, String phone, String website, String industryType) throws SQLException {
        checkConnection();
        String query = "UPDATE companies SET name = ?, email = ?, phone = ?, website = ?, industry_type = ? WHERE company_id = ?";
        return updateCompanyDetails(id, query, name, email, phone, website, industryType);
    }

    public static String updateCompanyAddress(long id, String addressLine1, String addressLine2, String city, String stateOrProvince, String postalCode, String country) throws SQLException {
        checkConnection();
        String query = "UPDATE companies SET address_line1 = ?, address_line2 = ?, city = ?, state_or_province = ?, postal_code = ?, country = ? WHERE company_id = ?";
        return updateCompanyDetails(id, query, addressLine1, addressLine2, city, stateOrProvince, postalCode, country);
    }

    private static String updateCompanyDetails(long id, String query, Object... params) throws SQLException {
        checkConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.setLong(params.length + 1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return "Company details updated successfully.";
            } else {
                throw new SQLException("Updating company details failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating company details: " + e.getMessage(), e);
        }
    }

    private static void setCompanyParameters(PreparedStatement statement, Company company) throws SQLException {
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
    }

    private static Company mapResultSetToCompany(ResultSet resultSet) throws SQLException {
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

    public static List<Company> getCompaniesBasicInfo() throws SQLException {
        checkConnection();
        List<Company> companies = new ArrayList<>();
        String query = "SELECT company_id, name, email, phone, website, industry_type FROM companies";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

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
            throw new RuntimeException("Error retrieving companies basic info: " + e.getMessage(), e);
        }
        return companies;
    }

    public static String updateCompany(Company company) throws SQLException {
        checkConnection();
        String query = "UPDATE companies SET name = ?, email = ?, phone = ?, website = ?, industry_type = ?, address_line1 = ?, address_line2 = ?, city = ?, state_or_province = ?, postal_code = ?, country = ?, manager_name = ?, manager_email = ?, manager_phone = ?, fax = ?, alternate_phone = ?, annual_revenue = ?, tax_id = ?, company_size = ?, linkedin_profile = ?, twitter_profile = ?, facebook_profile = ?, company_logo = ?, company_description = ? WHERE company_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setCompanyParameters(statement, company);
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
        }
    }

    public static String deleteCompany(long id) throws SQLException {
        checkConnection();
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
        }
    }

    public static String updateCompanyOtherDetails(long id, String fax, String alternatePhone, double annualRevenue, String taxId, int companySize) throws SQLException {
        checkConnection();
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
        }
    }

    public static String updateSocialMediaProfiles(long id, String linkedinProfile, String twitterProfile, String facebookProfile) throws SQLException {
        checkConnection();
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
                throw new SQLException("Updating company social media profiles failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Error: " + e.getMessage(), e);
            throw new RuntimeException("Error updating company social media profiles: " + e.getMessage(), e);
        }
    }

    public static String updateCompanyLogo(long id, String logoPath) throws SQLException {
        checkConnection();
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
        }
    }




}
