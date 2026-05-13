package com.example.demo9.controller;

import com.example.demo9.dao.RentalAgreementDAO;
import com.example.demo9.model.RentalAgreement;
import com.example.demo9.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class RentalAgreementController {

    @FXML private TextField rentalIdField;
    @FXML private TextField contractorIdField;
    @FXML private TextField equipmentIdField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField totalCostField;
    @FXML private TextField rentalStatusField;

    @FXML private TableView<RentalAgreementCustom> rentalTable;
    @FXML private TableColumn<RentalAgreementCustom, Integer> colRentalId;
    @FXML private TableColumn<RentalAgreementCustom, String> colContName;
    @FXML private TableColumn<RentalAgreementCustom, String> colEquipModel;
    @FXML private TableColumn<RentalAgreementCustom, Double> colTotalCost;
    @FXML private TableColumn<RentalAgreementCustom, String> colRentalStatus;

    @FXML private CheckMenuItem menuRentalId, menuContName, menuEquipModel, menuCost, menuStatus;

    private RentalAgreementDAO dao = new RentalAgreementDAO();
    private ObservableList<RentalAgreementCustom> tableData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // ربط الأعمدة بالبيانات
        colRentalId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        colContName.setCellValueFactory(new PropertyValueFactory<>("contractorName"));
        colEquipModel.setCellValueFactory(new PropertyValueFactory<>("equipmentModel"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colRentalStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        rentalTable.setItems(tableData);
        handleRefreshTable(); // تحميل البيانات عند الفتح
    }

    @FXML
    private void handleInsertRental() {
        try {
            RentalAgreement r = new RentalAgreement(
                    Integer.parseInt(rentalIdField.getText()),
                    Integer.parseInt(contractorIdField.getText()),
                    startDatePicker.getValue().toString(),
                    endDatePicker.getValue().toString(),
                    Double.parseDouble(totalCostField.getText()),
                    0, // totalHours default
                    rentalStatusField.getText()
            );
            dao.insertRental(r);
            showAlert("Success", "Rental Agreement Created!");
            handleRefreshTable();
        } catch (Exception e) {
            showAlert("Error", "Insert failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateRental() {
        try {
            int id = Integer.parseInt(rentalIdField.getText());
            String status = rentalStatusField.getText();
            dao.updateRentalStatus(id, status);
            showAlert("Success", "Status Updated!");
            handleRefreshTable();
        } catch (Exception e) {
            showAlert("Error", "Update failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteRental() {
        try {
            int id = Integer.parseInt(rentalIdField.getText());
            dao.deleteRental(id);
            showAlert("Success", "Rental Deleted!");
            handleRefreshTable();
        } catch (Exception e) {
            showAlert("Error", "Delete failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefreshTable() {
        loadData(false);
    }

    @FXML
    private void handleGenerateJoinReport() {
        loadData(true); // تفعيل الـ Join لجلب الأسماء
    }

    private void loadData(boolean isJoin) {
        try {
            tableData.clear();
            Connection con = DBConnection.getConnection();
            String sql;

            if (isJoin) {
                // Select with JOIN (المطلوب في المشروع)
                sql = "SELECT R.RENTAL_ID_, C.COMPANY_NAME, E.MODEL, R.TOTAL_COST, R.RENTAL_STATUS " +
                        "FROM RENTAL_AGREEMENT R " +
                        "JOIN CONTRACTOR C ON R.CONTRACTOR_ID_ = C.CONTRACTOR_ID_ " +
                        "JOIN INCLUDES I ON R.RENTAL_ID_ = I.RENTAL_ID_ " +
                        "JOIN EQUIPMENT E ON I.EQUIPMENT_ID_ = E.EQUIPMENT_ID_";
            } else {
                sql = "SELECT RENTAL_ID_, 'N/A' as COMPANY_NAME, 'N/A' as MODEL, TOTAL_COST, RENTAL_STATUS FROM RENTAL_AGREEMENT";
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableData.add(new RentalAgreementCustom(
                        rs.getInt("RENTAL_ID_"),
                        rs.getString("COMPANY_NAME"),
                        rs.getString("MODEL"),
                        rs.getDouble("TOTAL_COST"),
                        rs.getString("RENTAL_STATUS")
                ));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Load Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleColumnVisibility() {
        colRentalId.setVisible(menuRentalId.isSelected());
        colContName.setVisible(menuContName.isSelected());
        colEquipModel.setVisible(menuEquipModel.isSelected());
        colTotalCost.setVisible(menuCost.isSelected());
        colRentalStatus.setVisible(menuStatus.isSelected());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // كلاس داخلي صغير لتسهيل عرض البيانات في الجدول (Helper Class)
    public static class RentalAgreementCustom {
        private int rentalId;
        private String contractorName;
        private String equipmentModel;
        private double totalCost;
        private String status;

        public RentalAgreementCustom(int id, String cName, String eModel, double cost, String stat) {
            this.rentalId = id; this.contractorName = cName; this.equipmentModel = eModel;
            this.totalCost = cost; this.status = stat;
        }
        public int getRentalId() { return rentalId; }
        public String getContractorName() { return contractorName; }
        public String getEquipmentModel() { return equipmentModel; }
        public double getTotalCost() { return totalCost; }
        public String getStatus() { return status; }
    }
}