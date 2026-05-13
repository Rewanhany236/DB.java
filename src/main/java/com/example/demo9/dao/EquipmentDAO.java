package com.example.demo9.dao;

import com.example.demo9.database.DBConnection;
import com.example.demo9.model.Equipment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    // ================= INSERT =================
    public void insertEquipment(Equipment e) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = """
            INSERT INTO EQUIPMENT 
            (EQUIPMENT_ID_, MODEL, ENGINE_POWER, HOURLY_RATE, STATUS_, EQUIPMENT_TYPE)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, e.getEquipmentId());
        ps.setString(2, e.getModel());
        ps.setInt(3, e.getEnginePower());
        ps.setDouble(4, e.getHourlyRate());
        ps.setString(5, e.getStatus());
        ps.setString(6, e.getEquipmentType());

        ps.executeUpdate();
        con.close();
    }

    // ================= UPDATE =================
    public void updateEquipment(Equipment e) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = """
            UPDATE EQUIPMENT
            SET MODEL=?, ENGINE_POWER=?, HOURLY_RATE=?, STATUS_=?, EQUIPMENT_TYPE=?
            WHERE EQUIPMENT_ID_=?
        """;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, e.getModel());
        ps.setInt(2, e.getEnginePower());
        ps.setDouble(3, e.getHourlyRate());
        ps.setString(4, e.getStatus());
        ps.setString(5, e.getEquipmentType());
        ps.setInt(6, e.getEquipmentId());

        ps.executeUpdate();
        con.close();
    }

    // ================= UPDATE STATUS ONLY =================
    public void updateStatus(int id, String newStatus) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "UPDATE EQUIPMENT SET STATUS_ = ? WHERE EQUIPMENT_ID_ = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, newStatus);
        ps.setInt(2, id);

        ps.executeUpdate();
        con.close();
    }

    // ================= DELETE =================
    public void deleteEquipment(int id) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "DELETE FROM EQUIPMENT WHERE EQUIPMENT_ID_ = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ps.executeUpdate();
        con.close();
    }

    // ================= SELECT ALL =================
    public List<Equipment> getAllEquipment() throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM EQUIPMENT";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        List<Equipment> list = new ArrayList<>();

        while (rs.next()) {
            Equipment e = new Equipment(
                    rs.getInt("EQUIPMENT_ID_"),
                    null,
                    rs.getString("MODEL"),
                    rs.getInt("ENGINE_POWER"),
                    rs.getDouble("HOURLY_RATE"),
                    rs.getString("STATUS_"),
                    null,
                    rs.getString("EQUIPMENT_TYPE")
            );
            list.add(e);
        }

        con.close();
        return list;
    }

    // ================= SEARCH BY ID =================
    public Equipment searchById(int id) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM EQUIPMENT WHERE EQUIPMENT_ID_ = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        Equipment equipment = null;

        if (rs.next()) {
            equipment = new Equipment(
                    rs.getInt("EQUIPMENT_ID_"),
                    null,
                    rs.getString("MODEL"),
                    rs.getInt("ENGINE_POWER"),
                    rs.getDouble("HOURLY_RATE"),
                    rs.getString("STATUS_"),
                    null,
                    rs.getString("EQUIPMENT_TYPE")
            );
        }

        con.close();
        return equipment;
    }

    // ================= SEARCH BY MODEL =================
    public List<Equipment> searchByModel(String model) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM EQUIPMENT WHERE MODEL LIKE ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + model + "%");

        ResultSet rs = ps.executeQuery();

        List<Equipment> list = new ArrayList<>();

        while (rs.next()) {
            Equipment e = new Equipment(
                    rs.getInt("EQUIPMENT_ID_"),
                    null,
                    rs.getString("MODEL"),
                    rs.getInt("ENGINE_POWER"),
                    rs.getDouble("HOURLY_RATE"),
                    rs.getString("STATUS_"),
                    null,
                    rs.getString("EQUIPMENT_TYPE")
            );
            list.add(e);
        }

        con.close();
        return list;
    }

    // ================= SEARCH BY STATUS =================
    public List<Equipment> searchByStatus(String status) throws Exception {
        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM EQUIPMENT WHERE STATUS_ = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, status);

        ResultSet rs = ps.executeQuery();

        List<Equipment> list = new ArrayList<>();

        while (rs.next()) {
            Equipment e = new Equipment(
                    rs.getInt("EQUIPMENT_ID_"),
                    null,
                    rs.getString("MODEL"),
                    rs.getInt("ENGINE_POWER"),
                    rs.getDouble("HOURLY_RATE"),
                    rs.getString("STATUS_"),
                    null,
                    rs.getString("EQUIPMENT_TYPE")
            );
            list.add(e);
        }

        con.close();
        return list;
    }
}