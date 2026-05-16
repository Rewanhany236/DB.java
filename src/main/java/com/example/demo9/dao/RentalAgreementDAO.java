package com.example.demo9.dao;

import com.example.demo9.database.DBConnection;
import com.example.demo9.model.RentalAgreement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalAgreementDAO {

    // ================= GET ALL WITH JOIN  =================
    public List<RentalAgreement> getAllWithContractor() throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = """
            SELECT 
                R.RENTAL_ID_,
                R.CONTRACTOR_ID_,
                C.COMPANY_NAME AS Company_NAME,
                R.START_DATE,
                R.END_DATE,
                R.TOTAL_COST,
                R.TOTAL_HOURS,
                R.RENTAL_STATUS
            FROM RENTAL_AGREEMENT R
            JOIN CONTRACTOR C ON R.CONTRACTOR_ID_ = C.CONTRACTOR_ID_
            ORDER BY R.RENTAL_ID_
        """;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<RentalAgreement> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new RentalAgreement(
                    rs.getInt("RENTAL_ID_"),
                    rs.getInt("CONTRACTOR_ID_"),
                    rs.getString("Company_NAME"),
                    rs.getString("START_DATE"),
                    rs.getString("END_DATE"),
                    rs.getDouble("TOTAL_COST"),
                    rs.getInt("TOTAL_HOURS"),
                    rs.getString("RENTAL_STATUS")
            ));
        }

        con.close();
        return list;
    }

    // ================= GET ALL WITHOUT JOIN =================
    public List<RentalAgreement> getAllSimple() throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM RENTAL_AGREEMENT ORDER BY RENTAL_ID_";

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<RentalAgreement> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new RentalAgreement(
                    rs.getInt("RENTAL_ID_"),
                    rs.getInt("CONTRACTOR_ID_"),
                    "",
                    rs.getString("START_DATE"),
                    rs.getString("END_DATE"),
                    rs.getDouble("TOTAL_COST"),
                    rs.getInt("TOTAL_HOURS"),
                    rs.getString("RENTAL_STATUS")
            ));
        }

        con.close();
        return list;
    }

    // ================= SEARCH BY ID =================
    public RentalAgreement searchById(int id) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = """
            SELECT 
                R.RENTAL_ID_,
                R.CONTRACTOR_ID_,
                C.COMPANY_NAME AS CONTRACTOR_NAME,
                R.START_DATE,
                R.END_DATE,
                R.TOTAL_COST,
                R.TOTAL_HOURS,
                R.RENTAL_STATUS
            FROM RENTAL_AGREEMENT R
            JOIN CONTRACTOR C ON R.CONTRACTOR_ID_ = C.CONTRACTOR_ID_
            WHERE R.RENTAL_ID_ = ?
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        RentalAgreement rental = null;

        if (rs.next()) {
            rental = new RentalAgreement(
                    rs.getInt("RENTAL_ID_"),
                    rs.getInt("CONTRACTOR_ID_"),
                    rs.getString("Company_NAME"),
                    rs.getString("START_DATE"),
                    rs.getString("END_DATE"),
                    rs.getDouble("TOTAL_COST"),
                    rs.getInt("TOTAL_HOURS"),
                    rs.getString("RENTAL_STATUS")
            );
        }

        con.close();
        return rental;
    }

    // ================= INSERT =================
    public void insertRental(RentalAgreement r) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = """
            INSERT INTO RENTAL_AGREEMENT 
            (RENTAL_ID_, CONTRACTOR_ID_, START_DATE, END_DATE, TOTAL_COST, TOTAL_HOURS, RENTAL_STATUS)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, r.getRentalId());
        ps.setInt(2, r.getContractorId());
        ps.setString(3, r.getStartDate());
        ps.setString(4, r.getEndDate());
        ps.setDouble(5, r.getTotalCost());
        ps.setInt(6, r.getTotalHours());
        ps.setString(7, r.getRentalStatus());

        ps.executeUpdate();
        con.close();
    }

    // ================= UPDATE =================
    public void updateRental(RentalAgreement r) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = """
            UPDATE RENTAL_AGREEMENT 
            SET CONTRACTOR_ID_=?, START_DATE=?, END_DATE=?, TOTAL_COST=?, TOTAL_HOURS=?, RENTAL_STATUS=?
            WHERE RENTAL_ID_=?
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, r.getContractorId());
        ps.setString(2, r.getStartDate());
        ps.setString(3, r.getEndDate());
        ps.setDouble(4, r.getTotalCost());
        ps.setInt(5, r.getTotalHours());
        ps.setString(6, r.getRentalStatus());
        ps.setInt(7, r.getRentalId());

        ps.executeUpdate();
        con.close();
    }

    // ================= UPDATE STATUS =================
    public void updateRentalStatus(int rentalId, String newStatus) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "UPDATE RENTAL_AGREEMENT SET RENTAL_STATUS = ? WHERE RENTAL_ID_ = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, newStatus);
        ps.setInt(2, rentalId);

        ps.executeUpdate();
        con.close();
    }

    // ================= DELETE =================
    public void deleteRental(int rentalId) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "DELETE FROM RENTAL_AGREEMENT WHERE RENTAL_ID_ = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, rentalId);

        ps.executeUpdate();
        con.close();
    }

    // ================= SEARCH BY CONTRACTOR NAME =================
    public List<RentalAgreement> searchByContractorName(String contractorName) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = """
            SELECT 
                R.RENTAL_ID_,
                R.CONTRACTOR_ID_,
                C.COMPANY_NAME AS CONTRACTOR_NAME,
                R.START_DATE,
                R.END_DATE,
                R.TOTAL_COST,
                R.TOTAL_HOURS,
                R.RENTAL_STATUS
            FROM RENTAL_AGREEMENT R
            JOIN CONTRACTOR C ON R.CONTRACTOR_ID_ = C.CONTRACTOR_ID_
            WHERE C.COMPANY_NAME LIKE ?
            ORDER BY R.RENTAL_ID_
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + contractorName + "%");
        ResultSet rs = ps.executeQuery();

        List<RentalAgreement> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new RentalAgreement(
                    rs.getInt("RENTAL_ID_"),
                    rs.getInt("CONTRACTOR_ID_"),
                    rs.getString("Company_NAME"),
                    rs.getString("START_DATE"),
                    rs.getString("END_DATE"),
                    rs.getDouble("TOTAL_COST"),
                    rs.getInt("TOTAL_HOURS"),
                    rs.getString("RENTAL_STATUS")
            ));
        }

        con.close();
        return list;
    }
}