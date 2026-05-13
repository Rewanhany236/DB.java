package com.example.demo9.dao;

import com.example.demo9.database.DBConnection;
import com.example.demo9.model.RentalAgreement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalAgreementDAO {

    // 1. INSERT Statement
    public void insertRental(RentalAgreement r) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO dbo.RENTAL_AGREEMENT (RENTAL_ID_, CONTRACTOR_ID_, START_DATE, END_DATE, TOTAL_COST, TOTAL_HOURS, RENTAL_STATUS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, r.getRentalId());
        pstmt.setInt(2, r.getContractorId());
        pstmt.setString(3, r.getStartDate());
        pstmt.setString(4, r.getEndDate());
        pstmt.setDouble(5, r.getTotalCost());
        pstmt.setInt(6, r.getTotalHours());
        pstmt.setString(7, r.getRentalStatus());

        pstmt.executeUpdate();
        con.close();
    }

    // 2. UPDATE Statement (With Condition)
    public void updateRentalStatus(int rentalId, String newStatus) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE dbo.RENTAL_AGREEMENT SET RENTAL_STATUS = ? WHERE RENTAL_ID_ = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, newStatus);
        pstmt.setInt(2, rentalId);

        pstmt.executeUpdate();
        con.close();
    }

    // 3. DELETE Statement (With Condition)
    public void deleteRental(int rentalId) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "DELETE FROM dbo.RENTAL_AGREEMENT WHERE RENTAL_ID_ = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, rentalId);

        pstmt.executeUpdate();
        con.close();
    }

    public void getRentalData(boolean fetchAll) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql;

        if (fetchAll) {
            sql = "SELECT * FROM dbo.RENTAL_AGREEMENT";
        } else {
            // Specific columns (ID, Cost, and Status)
            sql = "SELECT RENTAL_ID_, TOTAL_COST, RENTAL_STATUS FROM dbo.RENTAL_AGREEMENT";
        }

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("--- Rental Agreement Data (FetchAll: " + fetchAll + ") ---");

        while (rs.next()) {
            if (fetchAll) {
                System.out.println("ID: " + rs.getInt("RENTAL_ID_") +
                        " | Contractor ID: " + rs.getInt("CONTRACTOR_ID_") +
                        " | Cost: " + rs.getDouble("TOTAL_COST") +
                        " | Status: " + rs.getString("RENTAL_STATUS"));
            } else {
                System.out.println("ID: " + rs.getInt("RENTAL_ID_") +
                        " | Cost: " + rs.getDouble("TOTAL_COST") +
                        " | Status: " + rs.getString("RENTAL_STATUS"));
            }
        }
        con.close();
    }
    // 5. SELECT with JOIN (Between Equipment and Rental Agreement)
    public void getEquipmentRentalJoin() throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "SELECT E.MODEL, E.EQUIPMENT_ID_, R.RENTAL_ID_, R.TOTAL_COST, R.RENTAL_STATUS " +
                "FROM dbo.EQUIPMENT E " +
                "JOIN dbo.INCLUDES I ON E.EQUIPMENT_ID_ = I.EQUIPMENT_ID_ " +
                "JOIN dbo.RENTAL_AGREEMENT R ON I.RENTAL_ID_ = R.RENTAL_ID_";

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("--- Equipment Rental Join Report ---");
        while (rs.next()) {
            System.out.println("Model: " + rs.getString("MODEL") +
                    " | EquipmentID: " + rs.getInt("EQUIPMENT_ID_") +
                    " | RentalID: " + rs.getInt("RENTAL_ID_") +
                    " | Cost: " + rs.getDouble("TOTAL_COST") +
                    " | Status: " + rs.getString("RENTAL_STATUS"));
        }
        con.close();
    }
}