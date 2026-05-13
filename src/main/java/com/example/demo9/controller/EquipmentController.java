package com.example.demo9.controller;

import com.example.demo9.dao.EquipmentDAO;
import com.example.demo9.model.Equipment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EquipmentController {

    @FXML private TextField idField;
    @FXML private TextField modelField;
    @FXML private TextField powerField;
    @FXML private TextField rateField;
    @FXML private TextField statusField;
    @FXML private TextField typeField;

    @FXML private TableView<Equipment> equipmentTable;
    @FXML private TableColumn<Equipment, Integer> colId;
    @FXML private TableColumn<Equipment, String> colModel;
    @FXML private TableColumn<Equipment, Integer> colPower;
    @FXML private TableColumn<Equipment, Double> colRate;
    @FXML private TableColumn<Equipment, String> colStatus;

    @FXML private Label statusLabel;

    private final EquipmentDAO dao = new EquipmentDAO();
    private final ObservableList<Equipment> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPower.setCellValueFactory(new PropertyValueFactory<>("enginePower"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        equipmentTable.setItems(list);

        loadEquipment();
    }

    private void loadEquipment() {

        try {
            list.clear();
            list.addAll(dao.getAll());

            setStatus("Loaded: " + list.size(), true);

        } catch (Exception e) {
            setStatus(e.getMessage(), false);
        }
    }

    @FXML
    private void handleInsert() {

        try {
            Equipment e = new Equipment(
                    Integer.parseInt(idField.getText()),
                    null,
                    modelField.getText(),
                    Integer.parseInt(powerField.getText()),
                    Double.parseDouble(rateField.getText()),
                    statusField.getText(),
                    null,
                    typeField.getText()
            );

            dao.insertEquipment(e);

            loadEquipment();
            clear();

        } catch (Exception ex) {
            setStatus(ex.getMessage(), false);
        }
    }

    @FXML
    private void handleUpdate() {

        try {
            dao.updateStatus(
                    Integer.parseInt(idField.getText()),
                    statusField.getText()
            );

            loadEquipment();

        } catch (Exception e) {
            setStatus(e.getMessage(), false);
        }
    }

    @FXML
    private void handleDelete() {

        try {
            dao.deleteEquipment(Integer.parseInt(idField.getText()));

            loadEquipment();
            clear();

        } catch (Exception e) {
            setStatus(e.getMessage(), false);
        }
    }

    @FXML
    private void handleRefresh() {
        loadEquipment();
    }

    private void clear() {
        idField.clear();
        modelField.clear();
        powerField.clear();
        rateField.clear();
        statusField.clear();
        typeField.clear();
    }

    private void setStatus(String msg, boolean ok) {
        statusLabel.setText(msg);
        statusLabel.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}