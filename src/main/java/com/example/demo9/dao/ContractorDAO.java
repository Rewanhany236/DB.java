package com.example.demo9.dao;

import com.example.demo9.database.DBConnection;
import com.example.demo9.model.Contractor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractorDAO {

    // ================= INSERT =================
    public void insertContractor(Contractor c) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = """
            INSERT INTO CONTRACTOR 
            (CONTRACTOR_ID_, COMPANY_NAME, CREDIT_LIMIT, ADDRESS, EMAIL, PHONE)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, c.getContractorId());
        ps.setString(2, c.getCompanyName());
        ps.setDouble(3, c.getCreditLimit());
        ps.setString(4, c.getAddress());
        ps.setString(5, c.getEmail());
        ps.setString(6, c.getPhone());

        ps.executeUpdate();
        con.close();
    }

    // ================= UPDATE =================
    public void updateContractor(Contractor c) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = """
            UPDATE CONTRACTOR
            SET COMPANY_NAME=?, CREDIT_LIMIT=?, ADDRESS=?, EMAIL=?, PHONE=?
            WHERE CONTRACTOR_ID_=?
        """;

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, c.getCompanyName());
        ps.setDouble(2, c.getCreditLimit());
        ps.setString(3, c.getAddress());
        ps.setString(4, c.getEmail());
        ps.setString(5, c.getPhone());
        ps.setInt(6, c.getContractorId());

        ps.executeUpdate();
        con.close();
    }

    // ================= DELETE =================
    public void deleteContractor(int id) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "DELETE FROM CONTRACTOR WHERE CONTRACTOR_ID_=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);

        ps.executeUpdate();
        con.close();
    }

    // ================= SELECT ALL =================
    public List<Contractor> getAllContractors() throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM CONTRACTOR";

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(sql);

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

    // ================= SELECT (FILTER) =================
    public Contractor searchById(int id) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM CONTRACTOR WHERE CONTRACTOR_ID_ = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        Contractor contractor = null;

        if (rs.next()) {
            contractor = new Contractor(
                    rs.getInt("CONTRACTOR_ID_"),
                    rs.getString("COMPANY_NAME"),
                    rs.getDouble("CREDIT_LIMIT"),
                    rs.getString("ADDRESS"),
                    rs.getString("EMAIL"),
                    rs.getString("PHONE")
            );
        }

        con.close();
        return contractor;
    }
}