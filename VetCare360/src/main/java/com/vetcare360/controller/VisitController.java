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
 * Contrôleur pour la vue d'ajout de visite.
 * Gère la logique pour ajouter une nouvelle visite.
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
     * Initialiser le contrôleur.
     * Cette méthode est automatiquement appelée après le chargement du fichier FXML.
     */
    @FXML
    private void initialize() {
        try {
            // Vérifier si les composants de l'interface utilisateur sont initialisés
            if (datePicker == null || descriptionArea == null || vetComboBox == null) {
                System.err.println("Erreur : Composants de l'interface utilisateur non initialisés correctement");
                return;
            }

            // Définir le sélecteur de date à la date d'aujourd'hui
            datePicker.setValue(LocalDate.now());

            // Charger tous les vétérinaires dans la combo box
            try {
                List<Vet> vets = vetService.getAllVets();
                vetComboBox.setItems(FXCollections.observableArrayList(vets));
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des vétérinaires : " + e.getMessage());
                e.printStackTrace();
                // Continuer avec une liste vide plutôt que d'échouer
                vetComboBox.setItems(FXCollections.observableArrayList());
            }

            // Définir la fabrique de cellules pour afficher le nom complet du vétérinaire
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

            // Définir la cellule de bouton pour afficher le nom complet du vétérinaire sélectionné
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

            // Effacer les messages d'erreur précédents
            if (errorLabel != null) {
                errorLabel.setText("");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de VisitController : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Définir l'animal pour une nouvelle visite.
     * @param pet l'animal
     */
    @SuppressWarnings("exports")
    public void setPet(Pet pet) {
        try {
            System.out.println("[DEBUG] setPet appelé avec : " + (pet != null ? pet.getName() : "null"));
            if (pet == null) {
                System.err.println("[ERROR] Animal nul passé à setPet");
                return;
            }

            this.currentPet = pet;
            this.currentVisit = new Visit();

            // Déboguer les éléments de l'interface utilisateur
            System.out.println("[DEBUG] Éléments de l'interface utilisateur dans setPet :");
            System.out.println("  petLabel : " + (petLabel != null ? "présent" : "null"));
            System.out.println("  datePicker : " + (datePicker != null ? "présent" : "null"));
            System.out.println("  vetComboBox : " + (vetComboBox != null ? "présent" : "null"));
            System.out.println("  descriptionArea : " + (descriptionArea != null ? "présent" : "null"));

            // Vérifier si les composants de l'interface utilisateur existent avant de les utiliser
            if (petLabel != null) {
                petLabel.setText(pet.getName() + " (" + pet.getType() + ")");
            } else {
                System.err.println("[WARN] petLabel est null dans setPet");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception dans setPet : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Définir la visite à éditer.
     * @param visit la visite à éditer
     */
    @SuppressWarnings("exports")
    public void setVisit(Visit visit) {
        try {
            System.out.println("Définir la visite pour l'édition : " + (visit != null ? visit.getId() : "null"));
            if (visit == null) {
                System.err.println("Erreur : Visite nulle passée à setVisit");
                return;
            }

            this.currentVisit = visit;
            this.currentPet = visit.getPet();

            // Vérifier si les composants de l'interface utilisateur existent avant de les utiliser
            if (datePicker != null) {
                datePicker.setValue(visit.getDate());
            } else {
                System.err.println("Avertissement : datePicker est null dans setVisit");
            }

            if (descriptionArea != null) {
                descriptionArea.setText(visit.getDescription());
            } else {
                System.err.println("Avertissement : descriptionArea est null dans setVisit");
            }

            if (vetComboBox != null) {
                vetComboBox.setValue(visit.getVet());
            } else {
                System.err.println("Avertissement : vetComboBox est null dans setVisit");
            }

            // Mettre à jour l'étiquette de l'animal
            Pet pet = visit.getPet();
            if (pet != null && petLabel != null) {
                petLabel.setText(pet.getName() + " (" + pet.getType() + ")");
            } else {
                System.err.println("Avertissement : pet ou petLabel est null dans setVisit");
            }
        } catch (Exception e) {
            System.err.println("Erreur dans setVisit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sauvegarder la visite.
     * Vérifie que tous les champs sont remplis avant de sauvegarder.
     * @param event l'événement d'action
     */
    @FXML
    private void saveVisit(ActionEvent event) {
        try {
            // Vérifier si les composants de l'interface utilisateur sont initialisés
            if (datePicker == null || descriptionArea == null || vetComboBox == null) {
                showError("Erreur d'interface utilisateur", "Composants de l'interface utilisateur non initialisés correctement");
                return;
            }

            // Obtenir les valeurs du formulaire
            LocalDate date = datePicker.getValue();
            String description = descriptionArea.getText() != null ? descriptionArea.getText().trim() : "";
            Vet vet = vetComboBox.getValue();

            // Validation stricte des champs
            if (date == null || vet == null || description.isEmpty()) {
                if (errorLabel != null) {
                    errorLabel.setText("Veuillez remplir tous les champs requis.");
                }
                showError("Erreur de validation", "Veuillez remplir tous les champs requis.");
                return;
            }

            // Mettre à jour la visite
            currentVisit.setDate(date);
            currentVisit.setDescription(description);
            currentVisit.setVet(vet);

            // Définir l'animal si c'est une nouvelle visite
            if (currentVisit.getPet() == null) {
                if (currentPet == null) {
                    showError("Erreur", "Aucun animal assigné à cette visite");
                    return;
                }
                currentVisit.setPet(currentPet);
                currentPet.addVisit(currentVisit);
            }

            // Sauvegarder la visite
            visitService.saveVisit(currentVisit);
            visitSaved = true;
            closeWindow();
        } catch (Exception e) {
            showError("Erreur", "Échec de la sauvegarde de la visite : " + e.getMessage());
        }
    }

    /**
     * Annuler l'opération.
     * @param event l'événement d'action
     */
    @FXML
    private void cancel(ActionEvent event) {
        try {
            closeWindow();
        } catch (Exception e) {
            System.err.println("Erreur dans cancel : " + e.getMessage());
            e.printStackTrace();
            showError("Erreur", "Échec de la fermeture de la fenêtre : " + e.getMessage());
        }
    }

    /**
     * Vérifier si la visite a été sauvegardée.
     * @return true si la visite a été sauvegardée, false sinon
     */
    public boolean isVisitSaved() {
        return visitSaved;
    }

    /**
     * Fermer la fenêtre actuelle.
     */
    private void closeWindow() {
        try {
            // Essayer de trouver un composant de l'interface utilisateur pour obtenir la Stage
            Control control = null;
            if (datePicker != null) control = datePicker;
            else if (descriptionArea != null) control = descriptionArea;
            else if (vetComboBox != null) control = vetComboBox;

            if (control != null && control.getScene() != null) {
                Stage stage = (Stage) control.getScene().getWindow();
                stage.close();
            } else {
                System.err.println("Impossible de fermer la fenêtre - aucun contrôle de l'interface utilisateur disponible");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture de la fenêtre : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Afficher une boîte de dialogue d'erreur.
     * @param title le titre de la boîte de dialogue
     * @param message le message d'erreur
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void deleteVisit(ActionEvent event) {
        if (currentVisit == null || currentVisit.getId() == 0) {
            showError("Supprimer la visite", "Aucune visite sélectionnée ou visite non sauvegardée.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Supprimer la visite");
        confirmAlert.setHeaderText("Supprimer la visite");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette visite ?");
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
