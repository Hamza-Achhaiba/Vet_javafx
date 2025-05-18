package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.model.Visit;
import com.vetcare360.service.OwnerService;
import com.vetcare360.service.PetService;
import com.vetcare360.service.VisitService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

/**
 * Controller for the pet details and new pet views.
 */
public class PetController {

    private final PetService petService = new PetService();
    private final OwnerService ownerService = new OwnerService();
    private final VisitService visitService = new VisitService();

    private Pet currentPet;
    private Owner currentOwner;
    private boolean petSaved = false;
    private boolean petDeleted = false;

    // FXML fields for all views
    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label ownerLabel;
    @FXML private TableView<Visit> visitsTable;
    @FXML private TableColumn<Visit, LocalDate> dateColumn;
    @FXML private TableColumn<Visit, String> vetColumn;
    @FXML private TableColumn<Visit, String> descriptionColumn;
    @FXML private TextField nameField;
    @FXML private TextField typeField;
    @FXML private DatePicker birthDatePicker;
    @FXML private Label errorLabel;
    @FXML private VBox rootVBox;
    @FXML private Button deletePetButton;

    /**
     * Initialize the controller.
     */
    @FXML
    private void initialize() {
        System.out.println("PetController initialized");

        try {
            // Reset the petSaved flag
            petSaved = false;

            // Initialize visit table if it exists (not in newPet.fxml)
            if (visitsTable != null) {
                // Initialize table columns
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                vetColumn.setCellValueFactory(cellData -> {
                    Visit visit = cellData.getValue();
                    if (visit != null && visit.getVet() != null) {
                        return javafx.beans.binding.Bindings.createStringBinding(
                            () -> visit.getVet().getFullName());
                    }
                    return javafx.beans.binding.Bindings.createStringBinding(() -> "");
                });
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

                // Set row click handler for visits
                visitsTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            showVisitDetails(newSelection);
                        }
                    });

                // Deselect visit when clicking outside the table (robust)
                if (rootVBox != null) {
                    rootVBox.setOnMouseClicked(event -> {
                        Object target = event.getTarget();
                        if (!(target instanceof TableRow)) {
                            visitsTable.getSelectionModel().clearSelection();
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing PetController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the pet to display in pet details view.
     */
    @SuppressWarnings("exports")
    public void setPet(Pet pet) {
        try {
            System.out.println("Setting pet: " + (pet != null ? pet.getName() : "null"));
            if (pet == null) {
                System.err.println("Error: Null pet passed to setPet");
                return;
            }

            this.currentPet = pet;
            this.currentOwner = pet.getOwner();

            // Reset the petSaved flag
            petSaved = false;

            // Check if UI components exist before using them
            if (nameLabel != null) nameLabel.setText(pet.getName());
            if (typeLabel != null) typeLabel.setText(pet.getType());
            if (birthDateLabel != null) birthDateLabel.setText(pet.getBirthDate().toString());
            if (ownerLabel != null) ownerLabel.setText(pet.getOwner() != null ? pet.getOwner().getFullName() : "");

            // Load the pet's visits if the table exists
            if (visitsTable != null) {
                List<Visit> visits = visitService.getVisitsByPetId(pet.getId());
                visitsTable.setItems(FXCollections.observableArrayList(visits != null ? visits : Collections.emptyList()));
            }

            // Set up the edit form if fields exist
            if (nameField != null) nameField.setText(pet.getName());
            if (typeField != null) typeField.setText(pet.getType());
            if (birthDatePicker != null) birthDatePicker.setValue(pet.getBirthDate());

        } catch (Exception e) {
            System.err.println("Error in setPet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the owner for a new pet in the new pet form.
     */
    @SuppressWarnings("exports")
    public void setOwner(Owner owner) {
        try {
            System.out.println("[DEBUG] setOwner called with: " + (owner != null ? owner.getFullName() : "null"));
            if (owner == null) {
                System.err.println("[ERROR] Null owner passed to setOwner");
                return;
            }

            this.currentOwner = owner;
            this.currentPet = new Pet();
            this.currentPet.setOwner(owner);

            // Reset the petSaved flag
            petSaved = false;

            // Debug UI elements
            System.out.println("[DEBUG] UI elements in setOwner:");
            System.out.println("  nameLabel: " + (nameLabel != null ? "present" : "null"));
            System.out.println("  typeLabel: " + (typeLabel != null ? "present" : "null"));
            System.out.println("  birthDateLabel: " + (birthDateLabel != null ? "present" : "null"));
            System.out.println("  ownerLabel: " + (ownerLabel != null ? "present" : "null"));
            System.out.println("  nameField: " + (nameField != null ? "present" : "null"));
            System.out.println("  typeField: " + (typeField != null ? "present" : "null"));
            System.out.println("  birthDatePicker: " + (birthDatePicker != null ? "present" : "null"));

            // Clear the display labels if they exist
            if (nameLabel != null) nameLabel.setText("");
            if (typeLabel != null) typeLabel.setText("");
            if (birthDateLabel != null) birthDateLabel.setText("");
            if (ownerLabel != null) ownerLabel.setText(owner.getFullName());

            // Clear the visits table if it exists
            if (visitsTable != null) {
                visitsTable.setItems(FXCollections.observableArrayList());
            }

            // Set up the edit form if fields exist
            if (nameField != null) nameField.setText("");
            if (typeField != null) typeField.setText("");
            if (birthDatePicker != null) birthDatePicker.setValue(LocalDate.now());

        } catch (Exception e) {
            System.err.println("[ERROR] Exception in setOwner: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save the pet (used in both new pet and pet details views).
     */
    @FXML
    private void savePet(ActionEvent event) {
        try {
            if (nameField == null || typeField == null || birthDatePicker == null || errorLabel == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "UI components not initialized properly. Please contact support.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            errorLabel.setText("");
            if (currentOwner == null) {
                errorLabel.setText("No owner assigned to this pet.");
                return;
            }
            String name = nameField.getText() != null ? nameField.getText().trim() : "";
            String type = typeField.getText() != null ? typeField.getText().trim() : "";
            LocalDate birthDate = birthDatePicker.getValue();
            if (name.isEmpty() || type.isEmpty() || birthDate == null) {
                errorLabel.setText("Please fill in all required fields.");
                return;
            }
            if (currentPet == null) {
                currentPet = new Pet();
            }
            currentPet.setName(name);
            currentPet.setType(type);
            currentPet.setBirthDate(birthDate);
                currentPet.setOwner(currentOwner);
            // Ensure the pet is in the owner's pet list
            if (!currentOwner.getPets().contains(currentPet)) {
                currentOwner.addPet(currentPet);
            }
            try {
            petService.savePet(currentPet);
            ownerService.saveOwner(currentOwner);
            } catch (Exception e) {
                errorLabel.setText("Failed to save pet: " + e.getMessage());
                return;
            }
            petSaved = true;
            // After saving, reload the owner from storage and update the pet list in the UI
            Owner reloadedOwner = ownerService.getOwnerById(currentOwner.getId());
            if (reloadedOwner != null) {
                currentOwner = reloadedOwner;
            }
            closeWindow();
        } catch (Exception e) {
            if (errorLabel != null) errorLabel.setText("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Show the add visit view.
     */
    @FXML
    private void showAddVisit(ActionEvent event) {
        try {
            System.out.println("Attempting to open 'Add Visit' view.");

            // Path to newVisit.fxml
            String resourcePath = "/com/vetcare360/view/newVisit.fxml";
            var resource = getClass().getResource(resourcePath);

            // Check if the FXML resource exists
            if (resource == null) {
                System.err.println("FXML resource not found: " + resourcePath);
                showError("Resource Not Found", "Could not locate: " + resourcePath);
                return;
            }

            System.out.println("FXML resource loaded from: " + resource.toExternalForm());
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            // Get the controller
            VisitController controller = loader.getController();
            if (controller == null) {
                System.err.println("VisitController not initialized.");
                showError("Controller Error", "Could not initialize VisitController.");
                return;
            }

            // Set the pet in the VisitController
            controller.setPet(currentPet);

            // Create and show the new window
            Stage stage = new Stage();
            stage.setTitle("Add New Visit");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.showAndWait();

            // Refresh the visits table if a visit was saved
            if (controller.isVisitSaved()) {
                System.out.println("New visit added successfully. Refreshing pet details.");
                setPet(petService.getPetById(currentPet.getId()));
            }
        } catch (IOException e) {
            System.err.println("Error loading Add Visit view: " + e.getMessage());
            e.printStackTrace();
            showError("Loading Error", "Failed to load the Add Visit view: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Show the visit details.
     */
    private void showVisitDetails(Visit visit) {
        try {
            System.out.println("Attempting to open visit details for date: " + visit.getDate());

            String resourcePath = "/com/vetcare360/view/newVisit.fxml";
            var resource = getClass().getResource(resourcePath);

            if (resource == null) {
                showError("Resource Error", "Could not find the newVisit.fxml file.");
                System.err.println("Resource not found: " + resourcePath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            VisitController controller = loader.getController();
            if (controller == null) {
                showError("Controller Error", "Could not load the VisitController.");
                return;
            }

            controller.setVisit(visit);

            Stage stage = new Stage();
            stage.setTitle("Visit Details: " + visit.getDate());
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.showAndWait();

            // Refresh the visits table after editing or deleting
            if (controller.isVisitDeleted()) {
                setPet(petService.getPetById(currentPet.getId()));
            } else if (controller.isVisitSaved()) {
                System.out.println("Visit was saved, refreshing pet data");
                setPet(petService.getPetById(currentPet.getId()));
            } else {
                System.out.println("Visit was not saved");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open the visit details view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred while showing visit details: " + e.getMessage());
        }
    }

    /**
     * Check if the pet was saved.
     */
    public boolean isPetSaved() {
        return petSaved;
    }

    /**
     * Cancel and close the window.
     */
    @FXML
    public void cancelButton(@SuppressWarnings("exports") ActionEvent event) {
        closeWindow();
    }

    /**
     * Close the current window.
     */
    private void closeWindow() {
        try {
            // Try to find a UI component to get the Stage
            Control control = null;
            if (nameField != null) control = nameField;
            else if (typeField != null) control = typeField;
            else if (birthDatePicker != null) control = birthDatePicker;

            if (control != null && control.getScene() != null) {
                Stage stage = (Stage) control.getScene().getWindow();
                stage.close();
            } else {
                System.err.println("Cannot close window - no UI controls available");
            }
        } catch (Exception e) {
            System.err.println("Error closing window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show an error dialog.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void deletePet(ActionEvent event) {
        if (currentPet == null || currentPet.getId() == 0) {
            showError("Delete Pet", "No pet selected or pet not saved.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Pet");
        confirmAlert.setHeaderText("Delete Pet");
        confirmAlert.setContentText("Are you sure you want to delete " + currentPet.getName() + "?");
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                petService.deletePet(currentPet.getId());
                petDeleted = true;
                closeWindow();
            }
        });
    }

    public boolean isPetDeleted() {
        return petDeleted;
    }
}

