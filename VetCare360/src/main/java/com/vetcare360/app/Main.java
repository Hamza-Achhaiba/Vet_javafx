package com.vetcare360.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main application class for VetCare360.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Get the resource URL
            URL mainFxmlUrl = Main.class.getResource("/com/vetcare360/view/main.fxml");
            if (mainFxmlUrl == null) {
                showError("Resource Error", "Could not find the main.fxml file. The application cannot start.");
                Platform.exit();
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(mainFxmlUrl);

            // Load the FXML
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                showError("Loading Error", "Could not load the main view: " + e.getMessage());
                e.printStackTrace();
                Platform.exit();
                return;
            }

            // Add stylesheet
            try {
                URL cssUrl = getClass().getResource("/com/vetcare360/css/styles.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("Warning: Could not find the CSS file. The application will continue without styling.");
                }
            } catch (Exception e) {
                System.err.println("Error loading CSS: " + e.getMessage());
                e.printStackTrace();
                // Continue without styling
            }

            // Set up and show the stage
            stage.setTitle("VetCare360 - Veterinary Management System");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            showError("Startup Error", "An unexpected error occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Show an error dialog.
     * @param title the dialog title
     * @param message the error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText("An error occurred");
        alert.showAndWait();
    }

    /**
     * Main method to launch the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
