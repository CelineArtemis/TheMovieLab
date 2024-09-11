package moviesapp.model;

import java.util.List;
import java.util.Scanner;

/**
 * The FilterCLI class handles the command-line interface for interacting with movie filtering and user actions.
 * It provides various commands such as listing movies, searching by different criteria, adding/removing favorites, and displaying details.
 */
public class FilterCLI {
    private MovieFilterGUI movieFilter;
    /**
     * Constructs a FilterCLI instance with a specified MovieFilterGUI.
     *
     * @param movieFilter The MovieFilterGUI used for movie filtering.
     */
    public FilterCLI(MovieFilterGUI movieFilter) {
        this.movieFilter = movieFilter;
    }
    /**
     * Displays a list of available commands on the command line.
     */
    public static void displayCommands() {
        System.out.println("Here are the available commands:");
        System.out.println("0. 'commands' or 'O': Displays the list of all commands");
        System.out.println("1. 'list movies' or '1': Displays the list of all movies");
        System.out.println("2. 'search' or '2': Search for a movie by year, rating, or genre");
        System.out.println("3. 'add favorite' or '3': Adds a movie to your favorites");
        System.out.println("4. 'show favorites' or '4': Displays your favorite movies");
        System.out.println("5. 'remove favorite' or '5': Removes a movie from your favorites");
        System.out.println("6. 'search and display details' or '6': Search for a movie and display its details");
        System.out.println("7. 'quit' or '7': Quits the application");
    }
    /**
     * Lists some popular movies with their titles and release years.
     *
     * @param scanner The Scanner object for input.
     * @param movies  The list of movies to display.
     */
    public static void listMovies(Scanner scanner, List<Movie> movies) {
        System.out.println("Some popular movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle() + " (" + movies.get(i).getYear() + ")");
        }
    }
    /**
     * Displays a list of movies with their titles and release years.
     *
     * @param movies The list of movies to display.
     */
    private void displayMovies(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle() + " (" + movies.get(i).getYear() + ")");
        }
    }
    /**
     * Searches for movies by title and displays the results.
     *
     * @param scanner The Scanner object for input.
     */
    public void searchByTitle(Scanner scanner) {
        System.out.println("Enter movie title:");
        String title = scanner.nextLine();
        List<Movie> movies = movieFilter.searchMoviesByTitle(title);
        displayMovies(movies);
    }
    /**
     * Searches for movies by year and displays the results.
     *
     * @param scanner The Scanner object for input.
     */
    public void searchByYear(Scanner scanner) {
        System.out.println("Enter movie release year: ");
        String year = scanner.nextLine();
        List<Movie> movies = movieFilter.searchMoviesByYear(year);
        displayMovies(movies);
    }
    /**
     * Searches for movies by genre and displays the results.
     *
     * @param scanner The Scanner object for input.
     */
    public void searchByGenre(Scanner scanner) {
        System.out.println("Enter movie genre: ");
        String genre = scanner.nextLine();
        List<Movie> movies = movieFilter.searchMoviesByGenre(genre);
        displayMovies(movies);
    }
    /**
     * Searches for movies by rating and displays the results.
     *
     * @param scanner The Scanner object for input.
     */
    public void searchByRating(Scanner scanner) {
        System.out.println("Enter minimum movie rating: ");
        double rating = scanner.nextDouble();
        scanner.nextLine();  // Consume the remaining newline character
        List<Movie> movies = movieFilter.searchMoviesByRating(rating);
        displayMovies(movies);
    }
    /**
     * Adds a movie to the user's favorites based on the movie's title.
     *
     * @param scanner The Scanner object for input.
     * @param user    The user for whom to add the favorite.
     */
    public void addFavorite(Scanner scanner, User user) {
        System.out.println("Enter the movie title: ");
        String title = scanner.nextLine();
        List<Movie> movies = movieFilter.searchMoviesByTitle(title);
        displayMovies(movies);

        System.out.println("Enter the index of the movie to add to favorites: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            Movie movie = movies.get(index - 1);
            UserDAO userDAO = new UserDAO();
            userDAO.addMovieToFavorites(user, movie);
            System.out.println(movie.getTitle() + " has been added to your favorites.");
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Invalid index. Please enter a valid index.");
        }
    }
    /**
     * Shows the user's favorite movies.
     *
     * @param user The user for whom to show the favorites.
     */
    public void showFavorites(User user) {
        UserDAO userDAO = new UserDAO();
        List<Movie> favoriteMovies = userDAO.getFavorites(user);
        if (favoriteMovies.isEmpty()) {
            System.out.println("You have no favorite movies.");
        } else {
            System.out.println("Your favorite movies are:");
            for (int i = 0; i < favoriteMovies.size(); i++) {
                System.out.println((i + 1) + ". " + favoriteMovies.get(i).getTitle() + " (" + favoriteMovies.get(i).getYear() + ")");
            }
        }
    }
    /**
     * Removes a movie from the user's favorites based on the movie's title.
     *
     * @param scanner The Scanner object for input.
     * @param user    The user for whom to remove the favorite.
     */
    public void removeFavorite(Scanner scanner, User user) {
        UserDAO userDAO = new UserDAO();
        List<Movie> favoriteMovies = userDAO.getFavorites(user);
        System.out.println("Enter the index of the movie to remove from favorites: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            Movie movie = favoriteMovies.get(index - 1);
            userDAO.removeMovieFromFavorites(user, movie);
            System.out.println(movie.getTitle() + " has been removed from your favorites.");
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Invalid index. Please enter a valid index.");
        }
    }
    /**
     * Searches for a movie by title and displays its details.
     *
     * @param scanner The Scanner object for input.
     */
    public void searchAndDisplayDetails(Scanner scanner) {
        System.out.println("Enter the movie name: ");
        String title = scanner.nextLine();
        List<Movie> movies = movieFilter.searchMoviesByTitle(title);
        displayMovies(movies);

        System.out.println("Type the index of the film you're searching for: ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            System.out.println(movies.get(index - 1).getDetails());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Invalid index. Please enter a valid index.");
        }
    }
}

