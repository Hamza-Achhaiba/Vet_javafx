package com.vetcare360.controller;

import com.vetcare360.model.Vet;
import com.vetcare360.model.Visit;
import com.vetcare360.service.VetService;
import com.vetcare360.service.VisitService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the veterinarians view.
 * This controller handles displaying and managing veterinarians.
 */
public class VetController {
    
    private final VetService vetService = new VetService();
    private final VisitService visitService = new VisitService();
    
    @FXML
    private TableView<Vet> vetsTable;
    
    @FXML
    private TableColumn<Vet, String> nameColumn;
    
    @FXML
    private TableColumn<Vet, String> specializationColumn;
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField specializationField;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private TableView<Visit> visitsTable;
    
    @FXML
    private TableColumn<Visit, LocalDate> dateColumn;
    
    @FXML
    private TableColumn<Visit, String> petColumn;
    
    @FXML
    private TableColumn<Visit, String> descriptionColumn;
    
    @FXML private VBox rootVBox;
    
    private Vet currentVet;
    
    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Set up the vets table columns
        nameColumn.setCellValueFactory(cellData -> {
            Vet vet = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> vet.getFullName());
        });
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        
        // Set up the visits table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        petColumn.setCellValueFactory(cellData -> {
            Visit visit = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() -> 
                visit.getPet() != null ? visit.getPet().getName() : "");
        });
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        // Add a row click handler for vets
        vetsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showVetDetails(newSelection);
            }
        });
        
        // Load all vets
        loadVets();
        
        // Clear the form
        clearForm();
        
        // Deselect when clicking outside the table (robust)
        if (rootVBox != null) {
            rootVBox.setOnMouseClicked(event -> {
                Object target = event.getTarget();
                if (!(target instanceof TableRow)) {
                    vetsTable.getSelectionModel().clearSelection();
                }
            });
        }
    }
    
    /**
     * Load all veterinarians into the table.
     */
    private void loadVets() {
        List<Vet> vets = vetService.getAllVets();
        vetsTable.setItems(FXCollections.observableArrayList(vets));
    }
    
    /**
     * Show the details of a veterinarian.
     * @param vet the vet to display
     */
    private void showVetDetails(Vet vet) {
        currentVet = vet;
        
        // Update the form fields
        firstNameField.setText(vet.getFirstName());
        lastNameField.setText(vet.getLastName());
        specializationField.setText(vet.getSpecialization());
        
        // Load the vet's visits
        List<Visit> visits = visitService.getVisitsByVetId(vet.getId());
        visitsTable.setItems(FXCollections.observableArrayList(visits));
        
        // Update button text
        saveButton.setText("Update");
    }
    
    /**
     * Save or update a veterinarian.
     * @param event the action event
     */
    @FXML
    private void saveVet(ActionEvent event) {
        // Get the form values
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String specialization = specializationField.getText().trim();
        
        // Create or update the vet
        Vet vet = currentVet != null ? currentVet : new Vet();
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        vet.setSpecialization(specialization);
        
        // Validate the vet
        if (!vetService.validateVet(vet)) {
            errorLabel.setText("Please fill in all required fields.");
            return;
        }
        
        // Save the vet
        vetService.saveVet(vet);
        
        // Always reload the list of vets from storage
        List<Vet> updatedVets = vetService.getAllVets();
        vetsTable.setItems(FXCollections.observableArrayList(updatedVets));
        
        // Select the updated/added vet in the table
        for (Vet v : updatedVets) {
            if (v.getFirstName().equals(vet.getFirstName()) && v.getLastName().equals(vet.getLastName()) && v.getSpecialization().equals(vet.getSpecialization())) {
                vetsTable.getSelectionModel().select(v);
                showVetDetails(v);
                break;
            }
        }
        
        // Show success message
        errorLabel.setText("Veterinarian saved successfully.");
        // Set button text
        saveButton.setText(currentVet == null ? "Add" : "Update");
    }
    
    /**
     * Clear the form.
     * @param event the action event
     */
    @FXML
    private void clearForm(ActionEvent event) {
        clearForm();
    }
    
    /**
     * Clear the form.
     */
    private void clearForm() {
        currentVet = null;
        firstNameField.clear();
        lastNameField.clear();
        specializationField.clear();
        errorLabel.setText("");
        saveButton.setText("Add");
        visitsTable.setItems(FXCollections.observableArrayList());
    }
    
    /**
     * Delete a veterinarian.
     * @param event the action event
     */
    @FXML
    private void deleteVet(ActionEvent event) {
        if (currentVet == null) {
            errorLabel.setText("Please select a veterinarian to delete.");
            return;
        }
        
        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Veterinarian");
        alert.setHeaderText("Delete Veterinarian");
        alert.setContentText("Are you sure you want to delete " + currentVet.getFullName() + "?");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Delete the vet
            vetService.deleteVet(currentVet.getId());
            
            // Refresh the table
            loadVets();
            
            // Clear the form
            clearForm();
            
            // Show success message
            errorLabel.setText("Veterinarian deleted successfully.");
        }
    }
}