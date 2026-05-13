package com.example.demo9.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=DB_Assignment;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String pass = "12345678";

        return DriverManager.getConnection(url, user, pass);
    }
    public static void main(String[] args) {
        try {
            System.out.println("Checking connection...");
            Connection con = getConnection();
            if (con != null) {
                System.out.println("✅ Connection Successful! The database is ready.");
                con.close();
            }
        } catch (Exception e) {
            System.out.println("❌ Connection Failed!");
            e.printStackTrace();
        }
    }
}