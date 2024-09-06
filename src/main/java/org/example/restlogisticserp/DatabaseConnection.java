package org.example.restlogisticserp;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws Exception {


        String dbURL = "jdbc:mysql://m7nj9dclezfq7ax1.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/jtgzcb7cdfi5qvtx?useSSL=false";
        String dbUsername = "i41s2x37chsw1lk4";
        String dbPassword = "mxxnh5bt8ay4ok0l";
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


