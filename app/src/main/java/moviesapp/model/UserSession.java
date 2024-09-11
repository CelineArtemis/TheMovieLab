package moviesapp.model;

/**
 * The UserSession class represents a user session in the application,
 * implementing the Singleton pattern to ensure a single instance per user.
 */
public class UserSession {
    // Singleton instance of UserSession
    private static UserSession instance;
    // User associated with the session
    private User user;
    /**
     * Private constructor to prevent direct instantiation of UserSession.
     * Initializes the UserSession with the associated user.
     * @param user The user associated with the session.
     */
    private UserSession(User user) {
        this.user = user;
    }
    /**
     * Method to get the singleton instance of UserSession.
     * If the instance is null, a new instance is created with the provided user.
     * @param user The user associated with the session.
     * @return The singleton instance of UserSession.
     */
    public static UserSession getInstance(User user) {
        if (instance == null) {
            instance = new UserSession(user);
        }
        return instance;
    }
    /**
     * Method to get the user associated with the session.
     * @return The user associated with the session.
     */
    public User getUser() {
        return user;
    }
    /**
     * Method to clean the user session by setting the instance to null.
     */
    public static void cleanUserSession() {
        instance = null;
    }
}
