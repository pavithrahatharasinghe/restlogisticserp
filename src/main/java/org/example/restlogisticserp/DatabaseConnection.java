package org.example.restlogisticserp;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws Exception {


        String dbURL = "jdbc:mysql://localhost:3306/erp_db";
        String dbUsername = "root";
        String dbPassword = "123456";
        String dbDriver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(dbDriver);
            this.connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Database Connection Creation Failed: " + ex.getMessage());
            throw ex;
        }
    }

    public static DatabaseConnection getInstance() throws Exception {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}


