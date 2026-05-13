package com.example.demo9.dao;

import com.example.demo9.database.DBConnection;
import com.example.demo9.model.Equipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    // 1. INSERT Statement
    public void insertEquipment(Equipment e) throws Exception {
        Connection con = DBConnection.getConnection();

        // Fix: Added the 6th question mark here ------------------------------------v
        String sql = "INSERT INTO dbo.EQUIPMENT (EQUIPMENT_ID_, MODEL, ENGINE_POWER, HOURLY_RATE, STATUS_, EQUIPMENT_TYPE) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, e.getEquipmentId());
        pstmt.setString(2, e.getModel());
        pstmt.setInt(3, e.getEnginePower());
        pstmt.setDouble(4, e.getHourlyRate());
        pstmt.setString(5, e.getStatus());
        pstmt.setString(6, e.getEquipmentType());

        pstmt.executeUpdate();
        con.close();
    }

    // 2. UPDATE Statement (With Condition)
    public void updateStatus(int id, String newStatus) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE dbo.EQUIPMENT SET STATUS_ = ? WHERE EQUIPMENT_ID_ = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, newStatus);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        con.close();
    }

    // 3. DELETE Statement (With Condition)
    public void deleteEquipment(int id) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "DELETE FROM dbo.EQUIPMENT WHERE EQUIPMENT_ID_ = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        con.close();
    }

    // 4. SELECT Statement (From one table)


    // Dynamic Select Method
    public void getEquipmentData(boolean fetchAll) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql;

        if (fetchAll) {
            // Option 1: Select everything
            sql = "SELECT * FROM dbo.EQUIPMENT";
        } else {
            // Option 2: Select specific columns only (Model and Status)
            sql = "SELECT EQUIPMENT_ID_, MODEL, ENGINE_POWER, HOURLY_RATE, STATUS_  FROM dbo.EQUIPMENT";
        }

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("--- Equipment Data (FetchAll: " + fetchAll + ") ---");

        while (rs.next()) {
            if (fetchAll) {
                // If fetching all, we can access all columns
                System.out.println("ID: " + rs.getInt("EQUIPMENT_ID_") +
                        " | Model: " + rs.getString("MODEL") +
                        " | Power: " + rs.getInt("ENGINE_POWER") +
                        " | Status: " + rs.getString("STATUS_"));
            } else {
                // If fetching specific columns, we ONLY access those
                System.out.println("Model: " + rs.getString("MODEL") +
                        " | Status: " + rs.getString("STATUS_"));
            }
        }
        con.close();
    }

}