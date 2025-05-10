package com.vetcare360.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the home view.
 * This controller handles the home page functionality.
 */
public class HomeController {

    // No need for contentPane field as it's not defined in the FXML

    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization code if needed
    }

    /**
     * Show the owner search view.
     * This method is called when the "Find Owners" button is clicked.
     * @param event the action event
     */
    @FXML
    private void showOwnerSearch(ActionEvent event) {
        try {
            System.out.println("[DEBUG] Navigating to Owner Search");
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vetcare360/view/main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            MainController controller = loader.getController();
            if (controller == null) {
                System.err.println("[ERROR] MainController not initialized in showOwnerSearch");
                showError("Controller Error", "Could not initialize MainController.");
                return;
            }
            controller.showOwnerSearch(event);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Could not navigate to the owner search view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Show the veterinarians view.
     * This method is called when the "Veterinarians" button is clicked.
     * @param event the action event
     */
    @FXML
    private void showVets(ActionEvent event) {
        try {
            System.out.println("[DEBUG] Navigating to Veterinarians");
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vetcare360/view/main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            MainController controller = loader.getController();
            if (controller == null) {
                System.err.println("[ERROR] MainController not initialized in showVets");
                showError("Controller Error", "Could not initialize MainController.");
                return;
            }
            controller.showVets(event);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Could not navigate to the veterinarians view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Show an error dialog.
     * @param title the dialog title
     * @param message the error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
