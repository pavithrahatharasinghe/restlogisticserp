package org.example.restlogisticserp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    static {
        try {
            // HikariCP Configuration
            HikariConfig config = new HikariConfig();

//            config.setJdbcUrl("jdbc:mysql://m7nj9dclezfq7ax1.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/jtgzcb7cdfi5qvtx?useSSL=false");
//            config.setUsername("i41s2x37chsw1lk4");
//            config.setPassword("mxxnh5bt8ay4ok0l");
//            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            config.setJdbcUrl("jdbc:mysql://localhost:3306/erp_db");
            config.setUsername("root");
            config.setPassword("123456");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");


            // Recommended HikariCP settings
            config.setMaximumPoolSize(10);  // Maximum number of connections in the pool
            config.setMinimumIdle(5);  // Minimum number of idle connections in the pool
            config.setConnectionTimeout(30000);  // Max wait time for a connection (30 seconds)
            config.setIdleTimeout(600000);  // Max idle time for a connection (10 minutes)
            config.setMaxLifetime(1800000);  // Max lifetime for a connection (30 minutes)
            config.setConnectionTestQuery("SELECT 1");  // Query to validate connections

            // Create the HikariDataSource
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing DataSource: " + e.getMessage());
        }
    }

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    // Method to get a connection from the pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Close the DataSource when the application stops
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public static DatabaseConnection getInstance() {
        return new DatabaseConnection();
    }
}
