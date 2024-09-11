package moviesapp;
import moviesapp.model.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;
/**
 * The AppCLI class serves as the entry point for the movie application's command-line interface.
 * It initializes the necessary components, handles user authentication, and processes user commands.
 */
public class AppCLI {
    // Welcome message and error message constants
    private static final String WELCOME_MESSAGE = "Welcome %s to the best movie application in the world! in degraded mode";
    private static final String TMDB_API_ERROR = "An error occurred while reading the TMDB API.";
    /**
     * Initializes the list of movies from the TMDB API.
     *
     * @return A list of Movie objects retrieved from the API.
     */
    public static List<Movie> initializeMovies() {
        // Creating an API reader with the API keys
        ApiReader apiReader = new ApiReader("6823e7296bee3292692bfb54cd2cd17d", "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ODIzZTcyOTZiZWUzMjkyNjkyYmFmYjU0Y2QyY2NkMTdkIiwic3ViIjoiNjViODJmOWE4YzMxNTkwMTdiZjBkMmJhIiwic2NvcGUiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.iz0j8TRD4ZIDkJW4DSTNUuSygFtVsGd3uQRkgR-sWY8");
        List<Movie> movies = new ArrayList<>();
        for (int page = 1; page <= 1; page++) {
            try {
                String response = apiReader.getMovies(page);
                InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                JsonReader jsonReader = new JsonReader(stream, apiReader);
                movies.addAll(jsonReader.readJson());
            } catch (Exception e) {
                System.out.println(TMDB_API_ERROR);
                e.printStackTrace();
            }
        }
        return movies;
    }
    /**
     * The main method for the command-line application.
     *
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        // Create a new database
        Database.createNewDatabase();
        // Create an API reader and MovieFilterGUI
        ApiReader apiReader = new ApiReader("6823e7296bee3292692bfb54cd2cd17d", "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ODIzZTcyOTZiZWUzMjkyNjkyYmFmYjU0Y2QyY2NkMTdkIiwic3ViIjoiNjViODJmOWE4YzMxNTkwMTdiZjBkMmJhIiwic2NvcGUiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.iz0j8TRD4ZIDkJW4DSTNUuSygFtVsGd3uQRkgR-sWY8");
        MovieFilterGUI movieFilter = new MovieFilterGUI(apiReader);
        FilterCLI filterCLI = new FilterCLI(movieFilter);
        // Create an instance of UserDAO
        UserDAO userDAO = new UserDAO();
        // Get the user with username "admin"
        User user = userDAO.getUser("admin");
        // Check if the user exists and the password is correct
        if (user != null && BCrypt.checkpw("admin", user.getPassword())) {
            // The user is logged in
            FilterCLI.displayCommands();
        } else {
            System.out.println("Failed to log in.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String command = "";
        List<Movie> movies = new ArrayList<>();
        try {
            movies = initializeMovies();
        } catch (Exception e) {
            System.out.println("An error occurred while initializing movies: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        // Loop to handle user commands
        while (!command.equals("quit") && !command.equals("7")) {
            System.out.println("Enter your command: ");
            if (scanner.hasNextLine()) {
                command = scanner.nextLine();
            } else {
                break;
            }
            switch (command) {
            case "commands":
            case "0":
                FilterCLI.displayCommands();
                break;
            case "list movies":
            case "1":
                FilterCLI.listMovies(scanner,movies);
                break;
            case "search":
            case "2":
                System.out.println("Enter the index of the search type (1: title, 2: year, 3: genre, 4: rating): ");
                int searchType = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                switch (searchType) {
                    case 1:
                        filterCLI.searchByTitle(scanner);
                        break;
                    case 2:
                        filterCLI.searchByYear(scanner);
                        break;
                    case 3:
                        filterCLI.searchByGenre(scanner);
                        break;
                    case 4:
                        filterCLI.searchByRating(scanner);
                        break;
                    default:
                        System.out.println("Invalid search type. Please enter a valid search type (1: title, 2: year, 3: genre, 4: rating).");
                        break;
                }
                break;
            case "add favorite":
            case "3":
                filterCLI.addFavorite(scanner,user);
                break;
            case "show favorites":
            case "4":
                filterCLI.showFavorites(user);
                break;
            case "remove favorite":
            case "5":
                filterCLI.removeFavorite(scanner,user);
                break;
            case "details":
            case "6":
                filterCLI.searchAndDisplayDetails(scanner);
                break;
            case "quit":
            case "7":
                break;
            default:
                System.out.println("Invalid command. Please enter a valid command.");
                break;

            }
        }
    }
}
