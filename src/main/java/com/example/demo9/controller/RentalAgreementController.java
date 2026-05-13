package com.example.demo9.controller;

import com.example.demo9.dao.RentalAgreementDAO;
import com.example.demo9.database.DBConnection;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RentalAgreementController {

    // ================= FIELDS =================

    @FXML
    private TextField rentalIdField;

    @FXML
    private TextField contractorIdField;

    @FXML
    private TextField equipmentIdField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField totalCostField;

    @FXML
    private TextField rentalStatusField;

    // ================= JOIN =================

    @FXML
    private ComboBox<String> joinComboBox;

    // ================= TABLE =================

    @FXML
    private TableView<RentalAgreementCustom> rentalTable;

    @FXML
    private TableColumn<RentalAgreementCustom, Integer> colRentalId;

    @FXML
    private TableColumn<RentalAgreementCustom, String> colContName;

    @FXML
    private TableColumn<RentalAgreementCustom, String> colEquipModel;

    @FXML
    private TableColumn<RentalAgreementCustom, Double> colTotalCost;

    @FXML
    private TableColumn<RentalAgreementCustom, String> colRentalStatus;

    // ================= MENU =================

    @FXML
    private CheckMenuItem menuRentalId;

    @FXML
    private CheckMenuItem menuContName;

    @FXML
    private CheckMenuItem menuEquipModel;

    @FXML
    private CheckMenuItem menuCost;

    @FXML
    private CheckMenuItem menuStatus;

    // ================= STATUS =================

    @FXML
    private Label statusLabel;

    // ================= DAO =================

    private final RentalAgreementDAO dao =
            new RentalAgreementDAO();

    // ================= DATA =================

    private final ObservableList<RentalAgreementCustom> tableData =
            FXCollections.observableArrayList();

    // ================= INITIALIZE =================

    @FXML
    public void initialize() {

        colRentalId.setCellValueFactory(
                new PropertyValueFactory<>("rentalId"));

        colContName.setCellValueFactory(
                new PropertyValueFactory<>("contractorName"));

        colEquipModel.setCellValueFactory(
                new PropertyValueFactory<>("equipmentModel"));

        colTotalCost.setCellValueFactory(
                new PropertyValueFactory<>("totalCost"));

        colRentalStatus.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        rentalTable.setItems(tableData);

        joinComboBox.getItems().addAll(
                "No Join",
                "Join Contractor",
                "Join Equipment",
                "Full Join"
        );

        joinComboBox.setValue("No Join");

        loadData();
    }

    // ================= LOAD =================

    private void loadData() {

        try {

            tableData.clear();

            Connection con =
                    DBConnection.getConnection();

            String selected =
                    joinComboBox.getValue();

            String sql;

            switch (selected) {

                case "Join Contractor":

                    sql =
                            """
                            SELECT
                            R.RENTAL_ID_,
                            C.COMPANY_NAME,
                            'N/A' AS MODEL,
                            R.TOTAL_COST,
                            R.RENTAL_STATUS
                            FROM RENTAL_AGREEMENT R
                            JOIN CONTRACTOR C
                            ON R.CONTRACTOR_ID_ = C.CONTRACTOR_ID_
                            """;

                    break;

                case "Join Equipment":

                    sql =
                            """
                            SELECT
                            R.RENTAL_ID_,
                            'N/A' AS COMPANY_NAME,
                            E.MODEL,
                            R.TOTAL_COST,
                            R.RENTAL_STATUS
                            FROM RENTAL_AGREEMENT R
                            JOIN INCLUDES I
                            ON R.RENTAL_ID_ = I.RENTAL_ID_
                            JOIN EQUIPMENT E
                            ON I.EQUIPMENT_ID_ = E.EQUIPMENT_ID_
                            """;

                    break;

                case "Full Join":

                    sql =
                            """
                            SELECT
                            R.RENTAL_ID_,
                            C.COMPANY_NAME,
                            E.MODEL,
                            R.TOTAL_COST,
                            R.RENTAL_STATUS
                            FROM RENTAL_AGREEMENT R
                            JOIN CONTRACTOR C
                            ON R.CONTRACTOR_ID_ = C.CONTRACTOR_ID_
                            JOIN INCLUDES I
                            ON R.RENTAL_ID_ = I.RENTAL_ID_
                            JOIN EQUIPMENT E
                            ON I.EQUIPMENT_ID_ = E.EQUIPMENT_ID_
                            """;

                    break;

                default:

                    sql =
                            """
                            SELECT
                            RENTAL_ID_,
                            'N/A' AS COMPANY_NAME,
                            'N/A' AS MODEL,
                            TOTAL_COST,
                            RENTAL_STATUS
                            FROM RENTAL_AGREEMENT
                            """;
            }

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(sql);

            while (rs.next()) {

                tableData.add(

                        new RentalAgreementCustom(

                                rs.getInt("RENTAL_ID_"),

                                rs.getString("COMPANY_NAME"),

                                rs.getString("MODEL"),

                                rs.getDouble("TOTAL_COST"),

                                rs.getString("RENTAL_STATUS")
                        )
                );
            }

            con.close();

            setStatus(
                    "Loaded Successfully",
                    true
            );

        } catch (Exception e) {

            setStatus(
                    "Load Failed : " + e.getMessage(),
                    false
            );
        }
    }

    // ================= INSERT =================

    @FXML
    private void handleInsertRental() {

        try {

            RentalAgreement rental =
                    new RentalAgreement(

                            Integer.parseInt(
                                    rentalIdField.getText()),

                            Integer.parseInt(
                                    contractorIdField.getText()),

                            startDatePicker.getValue().toString(),

                            endDatePicker.getValue().toString(),

                            Double.parseDouble(
                                    totalCostField.getText()),

                            0,

                            rentalStatusField.getText()
                    );

            dao.insertRental(rental);

            setStatus(
                    "Inserted Successfully",
                    true
            );

            loadData();

        } catch (Exception e) {

            setStatus(
                    "Insert Failed : " + e.getMessage(),
                    false
            );
        }
    }

    // ================= UPDATE =================

    @FXML
    private void handleUpdateRental() {

        try {

            int id =
                    Integer.parseInt(
                            rentalIdField.getText());

            String status =
                    rentalStatusField.getText();

            dao.updateRentalStatus(id, status);

            setStatus(
                    "Updated Successfully",
                    true
            );

            loadData();

        } catch (Exception e) {

            setStatus(
                    "Update Failed : " + e.getMessage(),
                    false
            );
        }
    }

    // ================= DELETE =================

    @FXML
    private void handleDeleteRental() {

        try {

            int id =
                    Integer.parseInt(
                            rentalIdField.getText());

            dao.deleteRental(id);

            setStatus(
                    "Deleted Successfully",
                    true
            );

            loadData();

        } catch (Exception e) {

            setStatus(
                    "Delete Failed : " + e.getMessage(),
                    false
            );
        }
    }

    // ================= REFRESH =================

    @FXML
    private void handleRefreshTable() {

        loadData();
    }

    // ================= JOIN =================

    @FXML
    private void handleGenerateJoinReport() {

        loadData();
    }

    // ================= COLUMN VISIBILITY =================

    @FXML
    private void handleColumnVisibility() {

        colRentalId.setVisible(
                menuRentalId.isSelected());

        colContName.setVisible(
                menuContName.isSelected());

        colEquipModel.setVisible(
                menuEquipModel.isSelected());

        colTotalCost.setVisible(
                menuCost.isSelected());

        colRentalStatus.setVisible(
                menuStatus.isSelected());
    }

    // ================= STATUS =================

    private void setStatus(String message, boolean success) {

        if (statusLabel != null) {

            statusLabel.setText(message);

            if (success) {

                statusLabel.setStyle(
                        "-fx-text-fill: green; -fx-font-weight: bold;"
                );

            } else {

                statusLabel.setStyle(
                        "-fx-text-fill: red; -fx-font-weight: bold;"
                );
            }
        }
    }

    // ================= GO BACK =================

    @FXML
    private void handleGoBack(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/example/demo9/MainView.fxml"
                            )
                    );

            Scene scene =
                    new Scene(loader.load());

            Stage stage =
                    (Stage) ((Node)
                            event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ================= CUSTOM CLASS =================

    public static class RentalAgreementCustom {

        private final int rentalId;

        private final String contractorName;

        private final String equipmentModel;

        private final double totalCost;

        private final String status;

        public RentalAgreementCustom(
                int rentalId,
                String contractorName,
                String equipmentModel,
                double totalCost,
                String status
        ) {

            this.rentalId = rentalId;
            this.contractorName = contractorName;
            this.equipmentModel = equipmentModel;
            this.totalCost = totalCost;
            this.status = status;
        }

        public int getRentalId() {
            return rentalId;
        }

        public String getContractorName() {
            return contractorName;
        }

        public String getEquipmentModel() {
            return equipmentModel;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public String getStatus() {
            return status;
        }
    }
}