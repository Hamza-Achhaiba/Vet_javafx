package com.vetcare360.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Classe principale de l'application VetCare360.
 */
public class Main extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) {
        try {
            // Récupérer l'URL de la ressource
            URL mainFxmlUrl = Main.class.getResource("/com/vetcare360/view/main.fxml");
            if (mainFxmlUrl == null) {
                showError("Erreur de ressource", "Impossible de trouver le fichier main.fxml. L'application ne peut pas démarrer.");
                Platform.exit();
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(mainFxmlUrl);

            // Charger le FXML
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 600);
            } catch (IOException e) {
                showError("Erreur de chargement", "Impossible de charger la vue principale : " + e.getMessage());
                e.printStackTrace();
                Platform.exit();
                return;
            }

            // Ajouter la feuille de style
            try {
                URL cssUrl = getClass().getResource("/com/vetcare360/css/styles.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("Attention : Impossible de trouver le fichier CSS. L'application continuera sans style.");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du CSS : " + e.getMessage());
                e.printStackTrace();
                // Continuer sans style
            }

            // Configurer et afficher la fenêtre principale
            stage.setTitle("VetCare360 - Système de gestion vétérinaire");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            showError("Erreur au démarrage", "Une erreur inattendue est survenue lors du démarrage de l'application : " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
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

    /**
     * Méthode principale pour lancer l'application.
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch();
    }
}
