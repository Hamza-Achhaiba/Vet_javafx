package com.vetcare360.controller;

import com.vetcare360.model.Pet;
import com.vetcare360.model.Vet;
import com.vetcare360.model.Visit;
import com.vetcare360.service.VetService;
import com.vetcare360.service.VisitService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the new/edit visit view.
 * This controller handles adding and editing visits.
 */
public class VisitController {

    private final VisitService visitService = new VisitService();
    private final VetService vetService = new VetService();

    private Visit currentVisit;
    private Pet currentPet;
    private boolean visitSaved = false;
    private boolean visitDeleted = false;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ComboBox<Vet> vetComboBox;

    @FXML
    private Label petLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button deleteVisitButton;

    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        try {
            // Check if UI components are initialized
            if (datePicker == null || descriptionArea == null || vetComboBox == null) {
                System.err.println("Error: UI components not initialized properly");
                return;
            }

            // Set the date picker to today's date
            datePicker.setValue(LocalDate.now());

            // Load all vets into the combo box
            try {
                List<Vet> vets = vetService.getAllVets();
                vetComboBox.setItems(FXCollections.observableArrayList(vets));
            } catch (Exception e) {
                System.err.println("Error loading vets: " + e.getMessage());
                e.printStackTrace();
                // Continue with an empty list rather than failing
                vetComboBox.setItems(FXCollections.observableArrayList());
            }

            // Set the cell factory to display the vet's full name
            vetComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Vet item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getFullName());
                    }
                }
            });

            // Set the button cell to display the selected vet's full name
            vetComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Vet item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getFullName());
                    }
                }
            });

            // Clear any previous error messages
            if (errorLabel != null) {
                errorLabel.setText("");
            }
        } catch (Exception e) {
            System.err.println("Error initializing VisitController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the pet for a new visit.
     * @param pet the pet
     */
    public void setPet(Pet pet) {
        try {
            System.out.println("[DEBUG] setPet called with: " + (pet != null ? pet.getName() : "null"));
            if (pet == null) {
                System.err.println("[ERROR] Null pet passed to setPet");
                return;
            }

            this.currentPet = pet;
            this.currentVisit = new Visit();

            // Debug UI elements
            System.out.println("[DEBUG] UI elements in setPet:");
            System.out.println("  petLabel: " + (petLabel != null ? "present" : "null"));
            System.out.println("  datePicker: " + (datePicker != null ? "present" : "null"));
            System.out.println("  vetComboBox: " + (vetComboBox != null ? "present" : "null"));
            System.out.println("  descriptionArea: " + (descriptionArea != null ? "present" : "null"));

            // Check if UI components exist before using them
            if (petLabel != null) {
                petLabel.setText(pet.getName() + " (" + pet.getType() + ")");
            } else {
                System.err.println("[WARN] petLabel is null in setPet");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception in setPet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the visit to edit.
     * @param visit the visit to edit
     */
    public void setVisit(Visit visit) {
        try {
            System.out.println("Setting visit for editing: " + (visit != null ? visit.getId() : "null"));
            if (visit == null) {
                System.err.println("Error: Null visit passed to setVisit");
                return;
            }

            this.currentVisit = visit;
            this.currentPet = visit.getPet();

            // Check if UI components exist before using them
            if (datePicker != null) {
                datePicker.setValue(visit.getDate());
            } else {
                System.err.println("Warning: datePicker is null in setVisit");
            }

            if (descriptionArea != null) {
                descriptionArea.setText(visit.getDescription());
            } else {
                System.err.println("Warning: descriptionArea is null in setVisit");
            }

            if (vetComboBox != null) {
                vetComboBox.setValue(visit.getVet());
            } else {
                System.err.println("Warning: vetComboBox is null in setVisit");
            }

            // Update the pet label
            Pet pet = visit.getPet();
            if (pet != null && petLabel != null) {
                petLabel.setText(pet.getName() + " (" + pet.getType() + ")");
            } else {
                System.err.println("Warning: pet or petLabel is null in setVisit");
            }
        } catch (Exception e) {
            System.err.println("Error in setVisit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save the visit.
     * @param event the action event
     */
    @FXML
    private void saveVisit(ActionEvent event) {
        try {
            System.out.println("[DEBUG] Attempting to save visit");
            System.out.println("[DEBUG] currentVisit: " + (currentVisit != null ? currentVisit : "null"));
            System.out.println("[DEBUG] currentPet: " + (currentPet != null ? currentPet : "null"));

            // Check if UI components are initialized
            if (datePicker == null || descriptionArea == null || vetComboBox == null) {
                System.err.println("[ERROR] UI components not initialized properly");
                showError("UI Error", "UI components not initialized properly");
                return;
            }

            // Get the form values
            LocalDate date = datePicker.getValue();
            String description = descriptionArea.getText().trim();
            Vet vet = vetComboBox.getValue();

            System.out.println("[DEBUG] Visit info: Date=" + date + ", Description=" + description + ", Vet=" + (vet != null ? vet.getFullName() : "null"));

            // Basic validation
            if (date == null || description.isEmpty() || vet == null) {
                if (errorLabel != null) {
                    errorLabel.setText("Please fill in all required fields.");
                }
                System.err.println("[ERROR] Validation failed: missing fields");
                showError("Validation Error", "Please fill in all required fields.");
                return;
            }

            // Update the visit
            currentVisit.setDate(date);
            currentVisit.setDescription(description);
            currentVisit.setVet(vet);

            // Set the pet if this is a new visit
            if (currentVisit.getPet() == null) {
                if (currentPet == null) {
                    System.err.println("[ERROR] No pet assigned to this visit");
                    showError("Error", "No pet assigned to this visit");
                    return;
                }
                System.out.println("[DEBUG] Setting pet for visit");
                currentVisit.setPet(currentPet);
                currentPet.addVisit(currentVisit);
            }

            // Validate the visit
            if (!visitService.validateVisit(currentVisit)) {
                if (errorLabel != null) {
                    errorLabel.setText("Please fill in all required fields.");
                }
                System.err.println("[ERROR] Visit validation failed");
                showError("Validation Error", "Please fill in all required fields.");
                return;
            }

            // Save the visit
            System.out.println("[DEBUG] Saving visit");
            visitService.saveVisit(currentVisit);
            System.out.println("[DEBUG] Visit saved successfully with ID: " + currentVisit.getId());

            // Set the flag
            visitSaved = true;

            // Close the window
            closeWindow();
        } catch (Exception e) {
            System.err.println("[ERROR] Exception saving visit: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to save visit: " + e.getMessage());
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
     * Check if the visit was saved.
     * @return true if the visit was saved, false otherwise
     */
    public boolean isVisitSaved() {
        return visitSaved;
    }

    /**
     * Close the current window.
     */
    private void closeWindow() {
        try {
            // Try to find a UI component to get the Stage
            Control control = null;
            if (datePicker != null) control = datePicker;
            else if (descriptionArea != null) control = descriptionArea;
            else if (vetComboBox != null) control = vetComboBox;

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

    @FXML
    private void deleteVisit(ActionEvent event) {
        if (currentVisit == null || currentVisit.getId() == 0) {
            showError("Delete Visit", "No visit selected or visit not saved.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Visit");
        confirmAlert.setHeaderText("Delete Visit");
        confirmAlert.setContentText("Are you sure you want to delete this visit?");
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                visitService.deleteVisit(currentVisit.getId());
                visitDeleted = true;
                closeWindow();
            }
        });
    }

    public boolean isVisitDeleted() {
        return visitDeleted;
    }
}
