package moviesapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import moviesapp.model.User;
import moviesapp.model.UserDAO;
import moviesapp.model.UserSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * Controller class for the registration page.
 */
public class RegistrationController {

    // UI components
    @FXML
    private TextField emailField;
    @FXML
    private Button goButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink loginHyperlink;
    @FXML
    private TextField usernameField;
    @FXML
    private Label errorLabel;

    // Helper class for navigation between pages
    private NavigationManager navigationManager = new NavigationManager();

    /**
     * Initializes the controller and sets up event handlers.
     */
    @FXML
    private void initialize() {
        // Set up event handlers
        goButton.setOnAction(event -> performRegistration());
        loginHyperlink.setOnAction(event -> navigateToLogin());
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performRegistration();
            }
        });
    }

    /**
     * Handles the registration process.
     */
    private void performRegistration() {
        // Perform registration process
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        // Validate email and attempt registration
        if (validateEmail(email) && register(username, password, email)) {
            try {
                // Navigate to the main application page on successful registration
                navigationManager.navigateTo("/moviesapp.fxml", (Stage) goButton.getScene().getWindow());
            } catch (IOException e) {
                showError("An error occurred while navigating to the main page.");
            }
        }
    }

    /**
     * Navigates to the login page.
     */
    private void navigateToLogin() {
        try {
            navigationManager.navigateTo("/login.fxml", (Stage) goButton.getScene().getWindow());
        } catch (IOException e) {
            showError("An error occurred while navigating to the login page.");
        }
    }

    /**
     * Handles login button action.
     */
    public void handleLoginButtonAction(ActionEvent event) {
        // Handle login button action
        performRegistration();
    }

    /**
     * Validates the email format.
     *
     * @param email The email to validate.
     * @return True if the email is in the correct format, false otherwise.
     */
    public boolean validateEmail(String email) {
        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            errorLabel.setText("Email is not in the correct format");
            return false;
        }
        return true;
    }

    /**
     * Registers a new user.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param email    The email of the new user.
     * @return True if the registration is successful, false otherwise.
     */
    public boolean register(String username, String password, String email) {
        // Register a new user
        UserDAO userDAO = new UserDAO();
        if (userDAO.emailExists(email)) {
            errorLabel.setText("Email already exists");
            return false;
        }

        // Hash the password using BCrypt
        String hashedPassword = hashPassword(password);

        // Create a new user instance
        User user = new User(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);

        // Attempt to add the user to the database
        boolean isUserAdded = userDAO.addUser(user);

        // If the user is added successfully, set the user session
        if (isUserAdded) {
            User completeUser = userDAO.getUser(username);
            UserSession.getInstance(completeUser);
        }

        return isUserAdded;
    }

    /**
     * Hashes the given password using BCrypt.
     *
     * @param password The password to hash.
     * @return The hashed password.
     */
    public String hashPassword(String password) {
        // Hash the password using BCrypt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        // Show error message
        errorLabel.setText(message);
    }
}
