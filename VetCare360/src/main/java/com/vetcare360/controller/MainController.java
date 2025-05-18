package com.vetcare360.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Contrôleur pour la vue principale.
 * Ce contrôleur gère la disposition principale et la navigation entre les vues.
 */
public class MainController {

    @FXML
    private BorderPane contentPane;

    /**
     * Initialise le contrôleur.
     * Cette méthode est appelée automatiquement après le chargement du fichier FXML.
     */
    @FXML
    private void initialize() {
        // Charger la vue d'accueil par défaut
        loadView("/com/vetcare360/view/home.fxml");
    }

    /**
     * Affiche la vue d'accueil.
     */
    @FXML
    public void showHome() {
        loadView("/com/vetcare360/view/home.fxml");
    }

    /**
     * Affiche la vue des vétérinaires.
     * @param event l'événement d'action
     */
    @FXML
    public void showVets(@SuppressWarnings("exports") ActionEvent event) {
        loadView("/com/vetcare360/view/vets.fxml");
    }

    /**
     * Affiche la vue de recherche de propriétaires.
     * @param event l'événement d'action
     */
    @FXML
    public void showOwnerSearch(@SuppressWarnings("exports") ActionEvent event) {
        loadView("/com/vetcare360/view/ownerSearch.fxml");
    }

    /**
     * Affiche la boîte de dialogue "À propos".
     * @param event l'événement d'action
     */
    @FXML
    private void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos de VetCare360");
        alert.setHeaderText("VetCare360 - Système de gestion vétérinaire");
        alert.setContentText("Version 1.0\n\nUn système complet pour la gestion des cliniques vétérinaires.");
        alert.showAndWait();
    }

    /**
     * Charge une vue dans le panneau de contenu.
     * @param fxmlPath le chemin du fichier FXML
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            // Injection du MainController dans HomeController si nécessaire
            Object controller = loader.getController();
            if (controller instanceof HomeController) {
                ((HomeController) controller).setMainController(this);
            }
            contentPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur de chargement de la vue", "Impossible de charger la vue demandée : " + fxmlPath);
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur.
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
}
