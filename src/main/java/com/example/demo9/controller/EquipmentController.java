package com.example.demo9.controller;

import com.example.demo9.dao.EquipmentDAO;
import com.example.demo9.model.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EquipmentController {

    // ================= FIELDS =================
    @FXML private TextField idField;
    @FXML private TextField modelField;
    @FXML private TextField powerField;
    @FXML private TextField rateField;
    @FXML private TextField statusField;
    @FXML private TextField typeField;

    // ================= TABLE =================
    @FXML private TableView<Equipment> equipmentTable;
    @FXML private TableColumn<Equipment, Integer> colId;
    @FXML private TableColumn<Equipment, String> colModel;
    @FXML private TableColumn<Equipment, Integer> colPower;
    @FXML private TableColumn<Equipment, Double> colRate;
    @FXML private TableColumn<Equipment, String> colStatus;
    @FXML private TableColumn<Equipment, String> colType;

    // ================= MENU =================
    @FXML private CheckMenuItem menuId;
    @FXML private CheckMenuItem menuModel;
    @FXML private CheckMenuItem menuPower;
    @FXML private CheckMenuItem menuRate;
    @FXML private CheckMenuItem menuStatus;
    @FXML private CheckMenuItem menuType;


    // ================= STATUS =================
    @FXML private Label statusLabel;

    // ================= DAO =================
    private final EquipmentDAO dao = new EquipmentDAO();
    private final ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        // Setup table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPower.setCellValueFactory(new PropertyValueFactory<>("enginePower"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colType.setCellValueFactory(new PropertyValueFactory<>("equipmentType"));


        equipmentTable.setItems(equipmentList);

        // Load data when clicking on row
        equipmentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newValue) -> {
                    if (newValue != null) {
                        fillFields(newValue);
                    }
                }
        );

        loadEquipment();
    }

    // ================= FILL FIELDS FROM SELECTED ROW =================
    private void fillFields(Equipment e) {
        idField.setText(String.valueOf(e.getEquipmentId()));
        modelField.setText(e.getModel());
        powerField.setText(String.valueOf(e.getEnginePower()));
        rateField.setText(String.valueOf(e.getHourlyRate()));
        statusField.setText(e.getStatus());
        typeField.setText(e.getEquipmentType());
    }

    // ================= LOAD EQUIPMENT =================
    private void loadEquipment() {
        try {
            equipmentList.clear();
            equipmentList.addAll(dao.getAllEquipment());
            setStatus("Loaded Successfully: " + equipmentList.size() + " records", true);
        } catch (Exception e) {
            setStatus("Load Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= INSERT =================
    @FXML
    private void handleInsert() {
        try {
            Equipment equipment = buildEquipment();
            dao.insertEquipment(equipment);
            setStatus("Inserted Successfully", true);
            loadEquipment();
            clearFields();
        } catch (Exception e) {
            setStatus("Insert Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= UPDATE =================
    @FXML
    private void handleUpdate() {
        try {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                setStatus("Please select equipment first", false);
                return;
            }

            Equipment equipment = buildEquipment();
            dao.updateEquipment(equipment);
            setStatus("Updated Successfully", true);
            loadEquipment();
            clearFields();
        } catch (Exception e) {
            setStatus("Update Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    @FXML
    private void handleDelete() {
        try {
            String idText = idField.getText().trim();

            if (idText.isEmpty()) {
                Equipment selected = equipmentTable.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    setStatus("Please select equipment first", false);
                    return;
                }
                idText = String.valueOf(selected.getEquipmentId());
            }

            int id = Integer.parseInt(idText);

            // Confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete equipment ID: " + id + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                dao.deleteEquipment(id);
                setStatus("Deleted Successfully", true);
                loadEquipment();
                clearFields();
            }
        } catch (Exception e) {
            setStatus("Delete Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= SEARCH BY ID =================
    @FXML
    private void handleSearchById() {
        try {
            String idText = idField.getText().trim();

            if (idText.isEmpty()) {
                setStatus("Please enter Equipment ID to search", false);
                return;
            }

            int id = Integer.parseInt(idText);
            Equipment equipment = dao.searchById(id);

            equipmentList.clear();

            if (equipment != null) {
                equipmentList.add(equipment);
                fillFields(equipment);
                setStatus("Found equipment: " + equipment.getModel(), true);
            } else {
                setStatus("No equipment found with ID: " + id, false);
                clearFields();
            }

        } catch (NumberFormatException e) {
            setStatus("Invalid ID format. Please enter a number.", false);
        } catch (Exception e) {
            setStatus("Search Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }


    // ================= REFRESH =================
    @FXML
    private void handleRefresh() {
        loadEquipment();
        clearFields();
    }

    // ================= VIEW ALL DATA =================
    @FXML
    private void handleViewData() {
        loadEquipment();
    }

    // ================= COLUMN VISIBILITY =================
    @FXML
    private void handleColumnVisibility() {
        if (colId != null) colId.setVisible(menuId.isSelected());
        if (colModel != null) colModel.setVisible(menuModel.isSelected());
        if (colPower != null) colPower.setVisible(menuPower.isSelected());
        if (colRate != null) colRate.setVisible(menuRate.isSelected());
        if (colStatus != null) colStatus.setVisible(menuStatus.isSelected());
        if (colType != null) colType.setVisible(menuType.isSelected());
    }

    // ================= BUILD EQUIPMENT =================
    private Equipment buildEquipment() throws Exception {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) throw new Exception("Equipment ID is required");

        String modelText = modelField.getText().trim();
        if (modelText.isEmpty()) throw new Exception("Model is required");

        String powerText = powerField.getText().trim();
        if (powerText.isEmpty()) throw new Exception("Engine Power is required");

        String rateText = rateField.getText().trim();
        if (rateText.isEmpty()) throw new Exception("Hourly Rate is required");

        String statusText = statusField.getText().trim();
        if (statusText.isEmpty()) throw new Exception("Status is required");

        String typeText = typeField.getText().trim();
        if (typeText.isEmpty()) throw new Exception("Equipment Type is required");

        return new Equipment(
                Integer.parseInt(idText),
                null,
                modelText,
                Integer.parseInt(powerText),
                Double.parseDouble(rateText),
                statusText,
                null,
                typeText
        );
    }

    // ================= CLEAR FIELDS =================
    private void clearFields() {
        idField.clear();
        modelField.clear();
        powerField.clear();
        rateField.clear();
        statusField.clear();
        typeField.clear();
    }

    // ================= SET STATUS =================
    private void setStatus(String message, boolean success) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            if (success) {
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } else {
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        }
    }

    // ================= GO BACK =================
    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo9/MainView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}