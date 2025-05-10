package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.service.OwnerService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the owner search view.
 * This controller handles searching for owners and displaying the results.
 */
public class OwnerSearchController {

    private final OwnerService ownerService = new OwnerService();

    @FXML
    private TextField lastNameField;

    @FXML
    private TableView<Owner> ownersTable;

    @FXML
    private TableColumn<Owner, String> nameColumn;

    @FXML
    private TableColumn<Owner, String> addressColumn;

    @FXML
    private TableColumn<Owner, String> cityColumn;

    @FXML
    private TableColumn<Owner, String> telephoneColumn;

    @FXML
    private TableColumn<Owner, Integer> petsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox rootVBox;

    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        try {
            // Check if UI components are initialized
            if (ownersTable == null || nameColumn == null || addressColumn == null || 
                cityColumn == null || telephoneColumn == null || petsColumn == null || 
                lastNameField == null || statusLabel == null || rootVBox == null) {
                System.err.println("Error: UI components not initialized properly in initialize");
                return;
            }

            // Set up the table columns
            try {
                nameColumn.setCellValueFactory(cellData -> {
                    Owner owner = cellData.getValue();
                    if (owner == null) return javafx.beans.binding.Bindings.createStringBinding(() -> "");
                    return javafx.beans.binding.Bindings.createStringBinding(() -> owner.getFullName());
                });
                addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
                cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
                telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
                petsColumn.setCellValueFactory(cellData -> {
                    Owner owner = cellData.getValue();
                    if (owner == null) return javafx.beans.binding.Bindings.createObjectBinding(() -> 0);
                    return javafx.beans.binding.Bindings.createObjectBinding(() -> owner.getPets().size());
                });
            } catch (Exception e) {
                System.err.println("Error setting up table columns: " + e.getMessage());
                e.printStackTrace();
            }

            // Only open owner details on double-click
            ownersTable.setRowFactory(tv -> {
                TableRow<Owner> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        showOwnerDetails(row.getItem());
                    }
                });
                return row;
            });

            // Deselect when clicking outside the table (robust)
            if (rootVBox != null) {
                rootVBox.setOnMouseClicked(event -> {
                    Object target = event.getTarget();
                    if (!(target instanceof TableRow)) {
                        ownersTable.getSelectionModel().clearSelection();
                    }
                });
            }

            // Make search interactive: update as user types
            lastNameField.textProperty().addListener((obs, oldText, newText) -> {
                searchOwners(null);
            });

            // Load all owners initially
            try {
                searchOwners(null);
            } catch (Exception e) {
                System.err.println("Error loading initial owners: " + e.getMessage());
                e.printStackTrace();
                statusLabel.setText("Error loading owners. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing OwnerSearchController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Search for owners by last name.
     * @param event the action event
     */
    @FXML
    private void searchOwners(ActionEvent event) {
        try {
            // Check if UI components are initialized
            if (lastNameField == null || ownersTable == null || statusLabel == null) {
                System.err.println("Error: UI components not initialized properly in searchOwners");
                return;
            }

            String lastName = lastNameField.getText().trim();

            List<Owner> owners;
            try {
                if (lastName.isEmpty()) {
                    // If no last name is provided, get all owners
                    owners = ownerService.getAllOwners();
                    statusLabel.setText("Showing all owners");
                } else {
                    // Otherwise, search by last name
                    owners = ownerService.findOwnersByLastName(lastName);
                    statusLabel.setText("Found " + owners.size() + " owner(s) with last name containing '" + lastName + "'");
                }
            } catch (Exception e) {
                System.err.println("Error searching for owners: " + e.getMessage());
                e.printStackTrace();
                showError("Search Error", "Failed to search for owners: " + e.getMessage());
                return;
            }

            // Update the table
            ownersTable.setItems(FXCollections.observableArrayList(owners));
        } catch (Exception e) {
            System.err.println("Unexpected error in searchOwners: " + e.getMessage());
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred while searching: " + e.getMessage());
        }
    }

    /**
     * Show the add owner view.
     * @param event the action event
     */
    @FXML
    private void showAddOwner(ActionEvent event) {
        try {
            System.out.println("Attempting to open 'Add Owner' view.");

            // Path to newOwner.fxml
            String resourcePath = "/com/vetcare360/view/newOwner.fxml";
            var resource = getClass().getResource(resourcePath);

            // Check if the FXML resource exists
            if (resource == null) {
                System.err.println("FXML resource not found: " + resourcePath);
                showError("Resource Not Found", "Could not locate: " + resourcePath);
                return;
            }

            System.out.println("FXML resource loaded from: " + resource.toExternalForm());
            FXMLLoader loader = new FXMLLoader(resource);

            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                System.err.println("Error loading FXML: " + e.getMessage());
                e.printStackTrace();
                showError("Loading Error", "Failed to load the Add Owner view: " + e.getMessage());
                return;
            }

            // Get the controller
            NewOwnerController controller = loader.getController();
            if (controller == null) {
                System.err.println("NewOwnerController not initialized.");
                showError("Controller Error", "Could not initialize NewOwnerController.");
                return;
            }

            // Create and show the new window
            Stage stage = new Stage();
            stage.setTitle("Add Owner");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.showAndWait();

            // Refresh the table if an owner was added
            if (controller.isOwnerSaved()) {
                System.out.println("New owner added successfully. Refreshing owner list.");
                searchOwners(null);
            }
        } catch (Exception e) {
            System.err.println("Unexpected error in showAddOwner: " + e.getMessage());
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Show the owner details view.
     * @param owner the owner to display
     */
    private void showOwnerDetails(Owner owner) {
        try {
            System.out.println("Attempting to open owner details for: " + (owner != null ? owner.getFullName() : "null"));

            // Check if owner is null
            if (owner == null) {
                System.err.println("Error: Null owner passed to showOwnerDetails");
                showError("Error", "No owner selected.");
                return;
            }

            // Path to ownerDetails.fxml
            String resourcePath = "/com/vetcare360/view/ownerDetails.fxml";
            var resource = getClass().getResource(resourcePath);

            // Check if the FXML resource exists
            if (resource == null) {
                System.err.println("FXML resource not found: " + resourcePath);
                showError("Resource Not Found", "Could not locate: " + resourcePath);
                return;
            }

            System.out.println("FXML resource loaded from: " + resource.toExternalForm());
            FXMLLoader loader = new FXMLLoader(resource);

            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                System.err.println("Error loading FXML: " + e.getMessage());
                e.printStackTrace();
                showError("Loading Error", "Failed to load the Owner Details view: " + e.getMessage());
                return;
            }

            // Get the controller
            OwnerController controller = loader.getController();
            if (controller == null) {
                System.err.println("OwnerController not initialized.");
                showError("Controller Error", "Could not initialize OwnerController.");
                return;
            }

            // Set the owner in the controller
            controller.setOwner(owner);

            // Create and show the new window
            Stage stage = new Stage();
            stage.setTitle("Owner Details: " + owner.getFullName());
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.showAndWait();

            // Refresh the table after viewing owner details
            System.out.println("Owner details closed. Refreshing owner list.");
            searchOwners(null);
        } catch (Exception e) {
            System.err.println("Unexpected error in showOwnerDetails: " + e.getMessage());
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
