package com.vetcare360.test;

import com.vetcare360.controller.PetController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;

/**
 * A simple test application to verify the PetController's cancelButton functionality.
 */
public class PetControllerTest extends Application {

    private boolean testPassed = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create a button to run the test
        Button testButton = new Button("Run Cancel Button Test");
        testButton.setOnAction(this::runTest);

        // Create a label to show the test result
        TextField resultField = new TextField("Test not run yet");
        resultField.setEditable(false);

        // Create the layout
        VBox root = new VBox(10, testButton, resultField);
        root.setPadding(new javafx.geometry.Insets(20));

        // Set up the scene
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("PetController Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runTest(ActionEvent event) {
        try {
            // Create a new stage that will be closed by the cancelButton method
            Stage testStage = new Stage();
            TextField nameField = new TextField("Test Name");
            Scene scene = new Scene(nameField, 200, 100);
            testStage.setScene(scene);
            testStage.show();

            // Create a PetController instance
            PetController controller = new PetController();

            // Use reflection to set the nameField
            Field field = PetController.class.getDeclaredField("nameField");
            field.setAccessible(true);
            field.set(controller, nameField);

            // Call the cancelButton method
            controller.cancelButton(new ActionEvent());

            // Check if the stage is closed
            testPassed = !testStage.isShowing();

            // Update the result field
            TextField resultField = (TextField) ((Button) event.getSource()).getParent().getChildrenUnmodifiable().get(1);
            if (testPassed) {
                resultField.setText("Test PASSED: Cancel button closes the window");
                resultField.setStyle("-fx-text-fill: green;");
            } else {
                resultField.setText("Test FAILED: Window was not closed");
                resultField.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TextField resultField = (TextField) ((Button) event.getSource()).getParent().getChildrenUnmodifiable().get(1);
            resultField.setText("Test ERROR: " + e.getMessage());
            resultField.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Main method to launch the test application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}