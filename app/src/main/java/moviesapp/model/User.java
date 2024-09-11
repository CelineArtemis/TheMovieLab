package moviesapp.model;

/**
 * The User class represents a user in the application, encapsulating user-related information.
 */
public class User {
    // User attributes
    private int id;
    private String username;
    private String password;
    private String email;
    private Favorite favorites;

    /**
     * Constructor to create a User with a provided username.
     * Initializes the username and creates an empty Favorite object.
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
        this.favorites = new Favorite();
    }

    /**
     * Setter method to set the ID of the user.
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method to retrieve the ID of the user.
     * @return The ID of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter method to retrieve the username of the user.
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method to set the password of the user.
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method to retrieve the password of the user.
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method to set the email of the user.
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter method to retrieve the email of the user.
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }
}
