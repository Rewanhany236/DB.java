package com.example.demo9.controller;

import com.example.demo9.dao.ContractorDAO;
import com.example.demo9.model.Contractor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ContractorController {

    // ================= FIELDS =================
    @FXML private TextField contractorIdField;
    @FXML private TextField companyNameField;
    @FXML private TextField creditLimitField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    // ================= TABLE =================
    @FXML private TableView<Contractor> contractorTable;
    @FXML private TableColumn<Contractor, Integer> colContractorId;
    @FXML private TableColumn<Contractor, String> colCompanyName;
    @FXML private TableColumn<Contractor, Double> colCreditLimit;
    @FXML private TableColumn<Contractor, String> colAddress;
    @FXML private TableColumn<Contractor, String> colEmail;
    @FXML private TableColumn<Contractor, String> colPhone;

    // ================= LABEL STATUS =================
    @FXML private Label statusLabel;

    // ================= DAO =================
    private final ContractorDAO dao = new ContractorDAO();
    private final ObservableList<Contractor> list = FXCollections.observableArrayList();

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        // Setup table columns
        colContractorId.setCellValueFactory(new PropertyValueFactory<>("contractorId"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colCreditLimit.setCellValueFactory(new PropertyValueFactory<>("creditLimit"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        contractorTable.setItems(list);

        // Load data when clicking on row
        contractorTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newValue) -> {
                    if (newValue != null) {
                        fillFields(newValue);
                    }
                }
        );

        loadData();
    }

    // ================= FILL FIELDS FROM SELECTED ROW =================
    private void fillFields(Contractor c) {
        contractorIdField.setText(String.valueOf(c.getContractorId()));
        companyNameField.setText(c.getCompanyName());
        creditLimitField.setText(String.valueOf(c.getCreditLimit()));
        addressField.setText(c.getAddress());
        emailField.setText(c.getEmail());
        phoneField.setText(c.getPhone());
    }

    // ================= LOAD DATA =================
    private void loadData() {
        try {
            list.clear();
            list.addAll(dao.getAllContractors());
            setStatus("Loaded Successfully: " + list.size() + " records", true);
        } catch (Exception e) {
            setStatus("Load Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= INSERT =================
    @FXML
    private void handleInsert() {
        try {
            Contractor c = buildObject();
            dao.insertContractor(c);
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
            String idText = contractorIdField.getText().trim();
            if (idText.isEmpty()) {
                setStatus("Please select a contractor first", false);
                return;
            }

            Contractor c = buildObject();
            dao.updateContractor(c);
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
            String idText = contractorIdField.getText().trim();

            if (idText.isEmpty()) {
                Contractor selected = contractorTable.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    setStatus("Please select a contractor first", false);
                    return;
                }
                idText = String.valueOf(selected.getContractorId());
            }

            int id = Integer.parseInt(idText);

            // Confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete contractor ID: " + id + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                dao.deleteContractor(id);
                setStatus("Deleted Successfully", true);
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            setStatus("Delete Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    @FXML
    // ================= SEARCH BY ID =================
    private void handleSelect() {
        try {
            String idText = contractorIdField.getText().trim();

            if (idText.isEmpty()) {
                loadData();
                setStatus("Showing all records", true);
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                Contractor contractor = dao.searchById(id);

                list.clear();
                if (contractor != null) {
                    list.add(contractor);
                    setStatus("Found contractor with ID: " + id, true);
                    fillFields(contractor);
                } else {
                    setStatus("No contractor found with ID: " + id, false);
                    clearFields();
                }
            } catch (NumberFormatException e) {
                setStatus("Please enter a valid numeric ID", false);
            }

        } catch (Exception e) {
            setStatus("Search Failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    // ================= REFRESH =================
    @FXML
    private void handleRefresh() {
        loadData();
        clearFields();
    }

    // ================= BUILD OBJECT =================
    private Contractor buildObject() throws Exception {
        String idText = contractorIdField.getText().trim();
        if (idText.isEmpty()) {
            throw new Exception("Contractor ID is required");
        }

        String nameText = companyNameField.getText().trim();
        if (nameText.isEmpty()) {
            throw new Exception("Company Name is required");
        }

        String creditText = creditLimitField.getText().trim();
        if (creditText.isEmpty()) {
            throw new Exception("Credit Limit is required");
        }

        return new Contractor(
                Integer.parseInt(idText),
                nameText,
                Double.parseDouble(creditText),
                addressField.getText(),
                emailField.getText(),
                phoneField.getText()
        );
    }

    // ================= CLEAR FIELDS =================
    private void clearFields() {
        contractorIdField.clear();
        companyNameField.clear();
        creditLimitField.clear();
        addressField.clear();
        emailField.clear();
        phoneField.clear();
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
}