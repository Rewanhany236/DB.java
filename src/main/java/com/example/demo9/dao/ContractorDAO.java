package com.example.demo9.dao;

import com.example.demo9.database.DBConnection;
import com.example.demo9.model.Contractor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractorDAO {

    // 1. INSERT Statement
    public void insertContractor(Contractor c) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO dbo.CONTRACTOR (CONTRACTOR_ID_, COMPANY_NAME, CREDIT_LIMIT) VALUES (?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, c.getContractorId());
        pstmt.setString(2, c.getCompanyName());
        pstmt.setDouble(3, c.getCreditLimit());
        pstmt.executeUpdate();
        con.close();
    }

    // 2. UPDATE Statement (With Condition)
    public void updateCreditLimit(int id, double newLimit) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE dbo.CONTRACTOR SET CREDIT_LIMIT = ? WHERE CONTRACTOR_ID_ = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setDouble(1, newLimit);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        con.close();
    }

    // 3. DELETE Statement (With Condition)
    public void deleteContractor(int id) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "DELETE FROM dbo.CONTRACTOR WHERE CONTRACTOR_ID_ = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        con.close();
    }

    public List<Contractor> getAllContractors() throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "SELECT * FROM dbo.CONTRACTOR";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Contractor> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Contractor(
                    rs.getInt("CONTRACTOR_ID_"),
                    rs.getString("COMPANY_NAME"),
                    rs.getDouble("CREDIT_LIMIT"),
                    rs.getString("ADDRESS"),
                    rs.getString("EMAIL"),
                    rs.getString("PHONE")
            ));
        }
        con.close();
        return list;
    }

    public void getContractorData(boolean fetchAll) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql;

        if (fetchAll) {
            sql = "SELECT * FROM dbo.CONTRACTOR";
        } else {
            // Specific columns for a summary view
            sql = "SELECT COMPANY_NAME, CREDIT_LIMIT FROM dbo.CONTRACTOR";
        }

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("--- Contractor Data (FetchAll: " + fetchAll + ") ---");

        while (rs.next()) {
            if (fetchAll) {
                System.out.println("ID: " + rs.getInt("CONTRACTOR_ID_") +
                        " | Name: " + rs.getString("COMPANY_NAME") +
                        " | Credit: " + rs.getDouble("CREDIT_LIMIT"));
            } else {
                System.out.println("Name: " + rs.getString("COMPANY_NAME") +
                        " | Credit: " + rs.getDouble("CREDIT_LIMIT"));
            }
        }
        con.close();
    }
}