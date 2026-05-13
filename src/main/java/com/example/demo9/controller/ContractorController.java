package com.example.demo9.controller;

import com.example.demo9.dao.ContractorDAO;
import com.example.demo9.model.Contractor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ContractorController {

    @FXML private TextField contractorIdField;
    @FXML private TextField companyNameField;
    @FXML private TextField creditLimitField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    @FXML private TableView<Contractor> contractorTable;
    @FXML private TableColumn<Contractor, Integer> colContractorId;
    @FXML private TableColumn<Contractor, String>  colCompanyName;
    @FXML private TableColumn<Contractor, Double>  colCreditLimit;
    @FXML private TableColumn<Contractor, String>  colAddress;
    @FXML private TableColumn<Contractor, String>  colEmail;
    @FXML private TableColumn<Contractor, String>  colPhone;

    @FXML private CheckMenuItem menuId;
    @FXML private CheckMenuItem menuName;
    @FXML private CheckMenuItem menuCredit;
    @FXML private CheckMenuItem menuAddress;
    @FXML private CheckMenuItem menuEmail;
    @FXML private CheckMenuItem menuPhone;

    @FXML private Label statusLabel;

    private final ContractorDAO dao = new ContractorDAO();
    private final ObservableList<Contractor> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colContractorId.setCellValueFactory(new PropertyValueFactory<>("contractorId"));
        colCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colCreditLimit.setCellValueFactory(new PropertyValueFactory<>("creditLimit"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        contractorTable.setItems(data);

        contractorTable.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                contractorIdField.setText(String.valueOf(sel.getContractorId()));
                companyNameField.setText(sel.getCompanyName());
                creditLimitField.setText(String.valueOf(sel.getCreditLimit()));
                addressField.setText(sel.getAddress() != null ? sel.getAddress() : "");
                emailField.setText(sel.getEmail() != null ? sel.getEmail() : "");
                phoneField.setText(sel.getPhone() != null ? sel.getPhone() : "");
            }
        });
        handleViewContractors();
    }

    @FXML
    private void onAddContractor() {
        try {
            Contractor c = buildFromFields();
            dao.insertContractor(c);
            setStatus("Contractor inserted successfully.", true);
            clearFields();
            handleViewContractors();
        } catch (Exception ex) {
            setStatus("Insert failed: " + ex.getMessage(), false);
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            int id = Integer.parseInt(contractorIdField.getText().trim());
            double limit = Double.parseDouble(creditLimitField.getText().trim());
            dao.updateCreditLimit(id, limit);
            setStatus("Credit limit updated.", true);
            handleViewContractors();
        } catch (Exception ex) {
            setStatus("Update failed: " + ex.getMessage(), false);
        }
    }

    @FXML
    private void handleDelete() {
        try {
            int id = Integer.parseInt(contractorIdField.getText().trim());
            dao.deleteContractor(id);
            setStatus("Contractor deleted.", true);
            clearFields();
            handleViewContractors();
        } catch (Exception ex) {
            setStatus("Delete failed: " + ex.getMessage(), false);
        }
    }

    @FXML
    public void handleViewContractors() {
        try {
            data.setAll(dao.getAllContractors());
            setStatus("Table refreshed. " + data.size() + " records.", true);
        } catch (Exception ex) {
            setStatus("Load failed: " + ex.getMessage(), false);
        }
    }

    @FXML
    private void handleColumnVisibility() {
        colContractorId.setVisible(menuId.isSelected());
        colCompanyName.setVisible(menuName.isSelected());
        colCreditLimit.setVisible(menuCredit.isSelected());
        colAddress.setVisible(menuAddress.isSelected());
        colEmail.setVisible(menuEmail.isSelected());
        colPhone.setVisible(menuPhone.isSelected());
    }

    @FXML
    private void handleClear() {
        clearFields();
        contractorTable.getSelectionModel().clearSelection();
    }

    private Contractor buildFromFields() {
        int id = Integer.parseInt(contractorIdField.getText().trim());
        String name = companyNameField.getText().trim();
        double credit = Double.parseDouble(creditLimitField.getText().trim());
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        return new Contractor(id, name, credit, address, email, phone);
    }

    private void clearFields() {
        contractorIdField.clear(); companyNameField.clear(); creditLimitField.clear();
        addressField.clear(); emailField.clear(); phoneField.clear();
    }

    private void setStatus(String msg, boolean ok) {
        statusLabel.setText(msg);
        statusLabel.setStyle(ok ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                : "-fx-text-fill: #c0392b; -fx-font-weight: bold;");
    }
}