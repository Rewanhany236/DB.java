package com.example.demo9.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    private void openWindow(String fileName, ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo9/" + fileName)
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node)
                    event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openContractor(ActionEvent event) {
        openWindow("Contractor.fxml", event);
    }

    public void openEquipment(ActionEvent event) {
        openWindow("Equipment.fxml", event);
    }

    public void openRental(ActionEvent event) {
        openWindow("RentalAgreement.fxml", event);
    }
}