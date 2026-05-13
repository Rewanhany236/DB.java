package com.example.demo9.controller;
import com.example.demo9.dao.EquipmentDAO;
import com.example.demo9.model.Equipment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentController {

    @FXML private TextField idField;
    @FXML private TextField modelField;
    @FXML private TextField powerField;
    @FXML private TextField rateField;
    @FXML private TextField statusField;
    @FXML private TextField typeField;

    @FXML private TableView<Equipment> equipmentTable;

    @FXML private TableColumn<Equipment, Integer> colId;
    @FXML private TableColumn<Equipment, String>  colModel;
    @FXML private TableColumn<Equipment, Integer> colPower;
    @FXML private TableColumn<Equipment, Double>  colRate;
    @FXML private TableColumn<Equipment, String>  colStatus;

    @FXML private CheckMenuItem menuId;
    @FXML private CheckMenuItem menuModel;
    @FXML private CheckMenuItem menuPower;
    @FXML private CheckMenuItem menuRate;
    @FXML private CheckMenuItem menuStatus;

    private EquipmentDAO dao = new EquipmentDAO();

    private ObservableList<Equipment> tableData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPower.setCellValueFactory(new PropertyValueFactory<>("enginePower"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        equipmentTable.setItems(tableData);

        equipmentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedEquipment) -> {
                    if (selectedEquipment != null) {
                        idField.setText(String.valueOf(selectedEquipment.getEquipmentId()));
                        modelField.setText(selectedEquipment.getModel());
                        powerField.setText(String.valueOf(selectedEquipment.getEnginePower()));
                        rateField.setText(String.valueOf(selectedEquipment.getHourlyRate()));
                        statusField.setText(selectedEquipment.getStatus());
                        typeField.setText(selectedEquipment.getEquipmentType());
                    }
                }
        );

        loadTableData();
    }

    private void loadTableData() {
        try {
            tableData.clear();

            java.sql.Connection con = com.example.demo9.database.DBConnection.getConnection();
            String sql = "SELECT EQUIPMENT_ID_, MODEL, ENGINE_POWER, HOURLY_RATE, STATUS_, EQUIPMENT_TYPE FROM dbo.EQUIPMENT";
            java.sql.Statement stmt = con.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

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

                tableData.add(e);
            }
            con.close();

        } catch (Exception e) {
            showAlert("Error loading data: " + e.getMessage());
        }
    }

    @FXML
    private void handleInsert() {
        try {
            int id         = Integer.parseInt(idField.getText().trim());
            String model   = modelField.getText().trim();
            int power      = Integer.parseInt(powerField.getText().trim());
            double rate    = Double.parseDouble(rateField.getText().trim());
            String status  = statusField.getText().trim();
            String type    = typeField.getText().trim();

            Equipment e = new Equipment(id, null, model, power, rate, status, null, type);

            dao.insertEquipment(e);

            showAlert("Equipment inserted successfully!");

            loadTableData();

            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Invalid input! ID and Power must be numbers.");
        } catch (Exception e) {
            showAlert("Insert failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            String newStatus = statusField.getText().trim();

            dao.updateStatus(id, newStatus);

            showAlert("Status updated successfully!");
            loadTableData();
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Invalid ID! Must be a number.");
        } catch (Exception e) {
            showAlert("Update failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            dao.deleteEquipment(id);

            showAlert("Equipment deleted successfully!");
            loadTableData();
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Invalid ID! Must be a number.");
        } catch (Exception e) {
            showAlert("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewData() {
        loadTableData();
    }

    @FXML
    private void handleColumnVisibility() {
        colId.setVisible(menuId.isSelected());
        colModel.setVisible(menuModel.isSelected());
        colPower.setVisible(menuPower.isSelected());
        colRate.setVisible(menuRate.isSelected());
        colStatus.setVisible(menuStatus.isSelected());
    }

    private void clearFields() {
        idField.clear();
        modelField.clear();
        powerField.clear();
        rateField.clear();
        statusField.clear();
        typeField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Equipment Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}