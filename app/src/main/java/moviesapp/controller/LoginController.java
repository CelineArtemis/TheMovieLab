/**
 * Controller class for the login view.
 * Handles user authentication and navigation to the main application page.
 */
package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import moviesapp.model.Database;
import moviesapp.model.User;
import moviesapp.model.UserSession;
import moviesapp.model.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;

/**
 * Controller class for the login view in the MovieApp application.
 */
public class LoginController {
    // UI components
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Hyperlink registerHyperlink;

    // Navigation manager for handling view transitions
    private NavigationManager navigationManager = new NavigationManager();

    /**
     * Initializes the controller and sets up event handlers.
     */
    @FXML
    private void initialize() {
        // Set up event handler for the password field
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    handleLoginButtonAction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Set up event handler for the register hyperlink
        registerHyperlink.setOnAction(event -> {
            try {
                // Load the registration page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) registerHyperlink.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles the login button action.
     * @throws IOException If an error occurs during the loading of the main application page.
     */
    @FXML
    private void handleLoginButtonAction() throws IOException {
        // Handle login button action
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (login(username, password)) {
            // If login is successful, load the main application page
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUser(username);
            UserSession.getInstance(user);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            navigationManager.navigateTo("/moviesapp.fxml", stage);
        } else {
            errorLabel.setText("Please check your username and password.");
        }
    }

    /**
     * Performs the login operation.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return True if the login is successful, false otherwise.
     */
    private boolean login(String username, String password) {
        // Perform login operation
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(username);

        // Check if the user exists and the password is correct
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }
}
