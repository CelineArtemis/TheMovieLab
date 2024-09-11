/**
 * A utility class for managing navigation between views in the MovieApp application.
 */
package moviesapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for managing navigation between views in the MovieApp application.
 */
public class NavigationManager {

    /**
     * Navigate to a specific FXML page.
     * @param path The path to the FXML file representing the target page.
     * @param currentStage The current stage to set the new scene.
     * @throws IOException If an error occurs during FXML loading or scene setting.
     */
    public void navigateTo(String path, Stage currentStage) throws IOException {
        // Load the FXML file for the target page
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        // Set the new scene on the current stage
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }
}
