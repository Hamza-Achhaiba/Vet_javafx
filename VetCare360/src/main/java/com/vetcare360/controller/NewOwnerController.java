package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.service.OwnerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the new/edit owner view.
 * This controller handles adding and editing owners.
 */
public class NewOwnerController {

    private final OwnerService ownerService = new OwnerService();

    private Owner currentOwner;
    private boolean ownerSaved = false;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField telephoneField;

    @FXML
    private Label errorLabel;

    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        try {
            // Clear any previous error messages
            if (errorLabel != null) {
                errorLabel.setText("");
            }
        } catch (Exception e) {
            System.err.println("Error initializing NewOwnerController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the owner to edit.
     * @param owner the owner to edit
     */
    @SuppressWarnings("exports")
    public void setOwner(Owner owner) {
        try {
            if (owner == null) {
                System.err.println("Warning: Null owner passed to setOwner");
                return;
            }

            this.currentOwner = owner;

            // Check if UI components are initialized
            if (firstNameField == null || lastNameField == null || 
                addressField == null || cityField == null || telephoneField == null) {
                showError("UI Error", "UI components not initialized properly");
                return;
            }

            // Update the form fields
            firstNameField.setText(owner.getFirstName());
            lastNameField.setText(owner.getLastName());
            addressField.setText(owner.getAddress());
            cityField.setText(owner.getCity());
            telephoneField.setText(owner.getTelephone());
        } catch (Exception e) {
            System.err.println("Error in setOwner: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to load owner details: " + e.getMessage());
        }
    }

    /**
     * Save the owner.
     * @param event the action event
     */
    @FXML
    private void saveOwner(ActionEvent event) {
        try {
            // Check if UI components are initialized
            if (firstNameField == null || lastNameField == null || 
                addressField == null || cityField == null || telephoneField == null) {
                showError("UI Error", "UI components not initialized properly");
                return;
            }

            // Get the form values
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String telephone = telephoneField.getText().trim();

            // Create or update the owner
            if (currentOwner == null) {
                currentOwner = new Owner(firstName, lastName, address, city, telephone);
            } else {
                currentOwner.setFirstName(firstName);
                currentOwner.setLastName(lastName);
                currentOwner.setAddress(address);
                currentOwner.setCity(city);
                currentOwner.setTelephone(telephone);
            }

            // Validate the owner
            if (!ownerService.validateOwner(currentOwner)) {
                if (errorLabel != null) {
                    errorLabel.setText("Please fill in all required fields and ensure the telephone number is valid.");
                } else {
                    showError("Validation Error", "Please fill in all required fields and ensure the telephone number is valid.");
                }
                return;
            }

            // Save the owner
            try {
                ownerService.saveOwner(currentOwner);
            } catch (Exception e) {
                System.err.println("Error saving owner: " + e.getMessage());
                e.printStackTrace();
                showError("Save Error", "Failed to save owner: " + e.getMessage());
                return;
            }

            // Set the flag
            ownerSaved = true;

            // Close the window
            closeWindow();
        } catch (Exception e) {
            System.err.println("Error in saveOwner: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Cancel the operation.
     * @param event the action event
     */
    @FXML
    private void cancel(ActionEvent event) {
        try {
            closeWindow();
        } catch (Exception e) {
            System.err.println("Error in cancel: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to close window: " + e.getMessage());
        }
    }

    /**
     * Close the current window.
     */
    private void closeWindow() {
        try {
            if (firstNameField != null && firstNameField.getScene() != null) {
                Stage stage = (Stage) firstNameField.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                } else {
                    System.err.println("Cannot close window - stage is null");
                }
            } else {
                System.err.println("Cannot close window - firstNameField or scene is null");
            }
        } catch (Exception e) {
            System.err.println("Error closing window: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to be caught by the calling method
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

    /**
     * Check if the owner was saved.
     * @return true if the owner was saved, false otherwise
     */
    public boolean isOwnerSaved() {
        return ownerSaved;
    }
}
