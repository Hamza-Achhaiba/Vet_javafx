package com.vetcare360.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Contrôleur pour la vue d'accueil.
 * Ce contrôleur gère la page d'accueil.
 */
public class HomeController {

    private MainController mainController;

    // No need for contentPane field as it's not defined in the FXML

    /**
     * Initialise le contrôleur.
     * Cette méthode est appelée automatiquement après le chargement du fichier FXML.
     */
    @FXML
    private void initialize() {
        // Code d'initialisation si nécessaire
    }

    /**
     * Injecte le MainController.
     * @param mainController le contrôleur principal
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Affiche la vue d'accueil.
     * Appelée lors du clic sur le bouton "Accueil".
     * @param event l'événement d'action
     */
    @FXML
    private void showHome(ActionEvent event) {
        if (mainController != null) {
            mainController.showHome();
        } else {
            showError("Erreur de navigation", "MainController non défini. Veuillez redémarrer l'application.");
        }
    }

    /**
     * Affiche la recherche de propriétaires.
     * Appelée lors du clic sur le bouton "Trouver des propriétaires".
     * @param event l'événement d'action
     */
    @FXML
    private void showOwnerSearch(ActionEvent event) {
        if (mainController != null) {
            mainController.showOwnerSearch(event);
        } else {
            showError("Erreur de navigation", "MainController non défini. Veuillez redémarrer l'application.");
        }
    }

    /**
     * Affiche la vue des vétérinaires.
     * Appelée lors du clic sur le bouton "Vétérinaires".
     * @param event l'événement d'action
     */
    @FXML
    private void showVets(ActionEvent event) {
        if (mainController != null) {
            mainController.showVets(event);
        } else {
            showError("Erreur de navigation", "MainController non défini. Veuillez redémarrer l'application.");
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur.
     * @param title le titre de la boîte de dialogue
     * @param message le message d'erreur
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText("Une erreur est survenue");
        alert.showAndWait();
    }
}
