package com.example.demo9.controller;

import com.example.demo9.dao.RentalAgreementDAO;
import com.example.demo9.model.RentalAgreement;
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

public class RentalAgreementController {

    // ================= FIELDS =================
    @FXML private TextField rentalIdField;
    @FXML private TextField contractorIdField;
    @FXML private TextField contractorNameField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField totalCostField;
    @FXML private TextField totalHoursField;
    @FXML private TextField rentalStatusField;
    @FXML private TextField searchField;

    // ================= TOGGLE BUTTON =================
    @FXML private ToggleButton joinToggleButton;

    // ================= TABLE =================
    @FXML private TableView<RentalAgreement> rentalTable;
    @FXML private TableColumn<RentalAgreement, Integer> colRentalId;
    @FXML private TableColumn<RentalAgreement, Integer> colContractorId;
    @FXML private TableColumn<RentalAgreement, String> colContractorName;
    @FXML private TableColumn<RentalAgreement, String> colStartDate;
    @FXML private TableColumn<RentalAgreement, String> colEndDate;
    @FXML private TableColumn<RentalAgreement, Double> colTotalCost;
    @FXML private TableColumn<RentalAgreement, Integer> colTotalHours;
    @FXML private TableColumn<RentalAgreement, String> colRentalStatus;

    // ================= STATUS =================
    @FXML private Label statusLabel;

    // ================= DAO =================
    private final RentalAgreementDAO dao = new RentalAgreementDAO();
    private final ObservableList<RentalAgreement> rentalList = FXCollections.observableArrayList();

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        // Setup table columns
        colRentalId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        colContractorId.setCellValueFactory(new PropertyValueFactory<>("contractorId"));
        colContractorName.setCellValueFactory(new PropertyValueFactory<>("contractorName"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colTotalHours.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        colRentalStatus.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));

        rentalTable.setItems(rentalList);

        // Toggle button text
        joinToggleButton.setText("Show Contractor Name");

        // Load data when toggled
        joinToggleButton.selectedProperty().addListener((obs, old, newValue) -> {
            loadData();
        });

        // Load data when clicking on a row
        rentalTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newValue) -> {
                    if (newValue != null) {
                        fillFields(newValue);
                    }
                }
        );

        loadData();
    }

    // ================= FILL FIELDS =================
    private void fillFields(RentalAgreement rental) {
        rentalIdField.setText(String.valueOf(rental.getRentalId()));
        contractorIdField.setText(String.valueOf(rental.getContractorId()));
        contractorNameField.setText(rental.getContractorName());

        totalCostField.setText(String.valueOf(rental.getTotalCost()));

        totalHoursField.setText(String.valueOf(rental.getTotalHours()));

        rentalStatusField.setText(rental.getRentalStatus());

        // ===== START DATE =====
        if (rental.getStartDate() != null && !rental.getStartDate().isEmpty()) {

            try {

                String startDateOnly = rental.getStartDate().split(" ")[0];

                startDatePicker.setValue(java.time.LocalDate.parse(startDateOnly));

            } catch (Exception e) {

                startDatePicker.setValue(null);

                System.out.println("Invalid Start Date Format");
            }
        } else {

            startDatePicker.setValue(null);
        }

        // ===== END DATE =====
        if (rental.getEndDate() != null && !rental.getEndDate().isEmpty()) {

            try {

                String endDateOnly = rental.getEndDate().split(" ")[0];

                endDatePicker.setValue(java.time.LocalDate.parse(endDateOnly));

            } catch (Exception e) {

                endDatePicker.setValue(null);

                System.out.println("Invalid End Date Format");
            }
        } else {

            endDatePicker.setValue(null);
        }
    }

    // ================= LOAD DATA =================
    private void loadData() {
        try {
            rentalList.clear();

            if (joinToggleButton.isSelected()) {
                rentalList.addAll(dao.getAllWithContractor());
                setStatus("Loaded with Contractor Name - " + rentalList.size() + " records", true);
            } else {
                rentalList.addAll(dao.getAllSimple());
                setStatus("Loaded Simple View - " + rentalList.size() + " records", true);
            }

        } catch (Exception e) {
            setStatus("Load Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= SEARCH =================
    @FXML
    private void handleSearch() {
        try {
            String searchText = searchField.getText().trim();

            if (searchText.isEmpty()) {
                loadData();
                return;
            }

            rentalList.clear();
            rentalList.addAll(dao.searchByContractorName(searchText));
            setStatus("Found " + rentalList.size() + " records matching: " + searchText, true);

        } catch (Exception e) {
            setStatus("Search Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= INSERT =================
    @FXML
    private void handleInsert() {
        try {
            RentalAgreement rental = new RentalAgreement(
                    Integer.parseInt(rentalIdField.getText().trim()),
                    Integer.parseInt(contractorIdField.getText().trim()),
                    "",
                    startDatePicker.getValue().toString(),
                    endDatePicker.getValue().toString(),
                    Double.parseDouble(totalCostField.getText().trim()),
                    Integer.parseInt(totalHoursField.getText().trim()),
                    rentalStatusField.getText().trim()
            );

            dao.insertRental(rental);
            setStatus("Inserted Successfully", true);
            loadData();
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
            String idText = rentalIdField.getText().trim();
            if (idText.isEmpty()) {
                setStatus("Please select a rental first", false);
                return;
            }

            RentalAgreement rental = new RentalAgreement(
                    Integer.parseInt(idText),
                    Integer.parseInt(contractorIdField.getText().trim()),
                    "",
                    startDatePicker.getValue().toString(),
                    endDatePicker.getValue().toString(),
                    Double.parseDouble(totalCostField.getText().trim()),
                    Integer.parseInt(totalHoursField.getText().trim()),
                    rentalStatusField.getText().trim()
            );

            dao.updateRental(rental);
            setStatus("Updated Successfully", true);
            loadData();
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
            String idText = rentalIdField.getText().trim();

            if (idText.isEmpty()) {
                RentalAgreement selected = rentalTable.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    setStatus("Please select a rental first", false);
                    return;
                }
                idText = String.valueOf(selected.getRentalId());
            }

            int id = Integer.parseInt(idText);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete rental ID: " + id + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                dao.deleteRental(id);
                setStatus("Deleted Successfully", true);
                loadData();
                clearFields();
            }

        } catch (Exception e) {
            setStatus("Delete Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= REFRESH =================
    @FXML
    private void handleRefresh() {
        loadData();
        clearFields();
    }

    // ================= CLEAR FIELDS =================
    private void clearFields() {
        rentalIdField.clear();
        contractorIdField.clear();
        contractorNameField.clear();
        totalCostField.clear();
        totalHoursField.clear();
        rentalStatusField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    // ================= STATUS =================
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