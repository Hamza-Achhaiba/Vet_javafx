package com.vetcare360.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Controller for the main view.
 * This controller handles the main layout and navigation between views.
 */
public class MainController {

    @FXML
    private BorderPane contentPane;

    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Load the home view by default
        loadView("/com/vetcare360/view/home.fxml");
    }

    /**
     * Show the home view.
     * @param event the action event
     */
    @FXML
    private void showHome(ActionEvent event) {
        loadView("/com/vetcare360/view/home.fxml");
    }

    /**
     * Show the veterinarians view.
     * @param event the action event
     */
    @FXML
    public void showVets(ActionEvent event) {
        loadView("/com/vetcare360/view/vets.fxml");
    }

    /**
     * Show the owner search view.
     * @param event the action event
     */
    @FXML
    public void showOwnerSearch(ActionEvent event) {
        loadView("/com/vetcare360/view/ownerSearch.fxml");
    }

    /**
     * Show the about dialog.
     * @param event the action event
     */
    @FXML
    private void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About VetCare360");
        alert.setHeaderText("VetCare360 - Veterinary Management System");
        alert.setContentText("Version 1.0\n\nA comprehensive system for managing veterinary clinics.");
        alert.showAndWait();
    }

    /**
     * Load a view into the content pane.
     * @param fxmlPath the path to the FXML file
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading view", "Could not load the requested view: " + fxmlPath);
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
