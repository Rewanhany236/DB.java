package com.example.demo9;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {

        try {

            System.out.println("1");

            URL fxmlLocation =
                    getClass().getResource("/com/example/demo9/MainView.fxml");

            System.out.println("2 = " + fxmlLocation);

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);

            System.out.println("3");

            Scene scene = new Scene(fxmlLoader.load());

            System.out.println("4");

            stage.setTitle("Rental Management System");
            stage.setScene(scene);
            stage.show();

            System.out.println("5");

        } catch (Exception e) {

            System.out.println("🚨 ERROR HERE 🚨");
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        launch();
    }
}