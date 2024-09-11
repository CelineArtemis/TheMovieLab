package moviesapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Favorite class represents a collection of favorite movies for a user.
 */
public class Favorite {
    private List<Movie> favoriteMovies;

    /**
     * Constructor to initialize the Favorite object with an empty list of favorite movies.
     */
    public Favorite() {
        favoriteMovies = new ArrayList<>();
    }

    /**
     * Method to add a movie to the list of favorite movies.
     * @param user The user for whom the movie is being added.
     * @param movie The movie to be added to the favorites.
     * @return A string message indicating the success or failure of the operation.
     */
    public String addMovie(User user, Movie movie) {
        if (contains(movie)) {
            return movie.getTitle() + " is already in favorites.";
        }

        UserDAO userDAO = new UserDAO();
        userDAO.addMovieToFavorites(user, movie);

        favoriteMovies.add(movie);
        return "Successfully added " + movie.getTitle() + " to favorites.";
    }

    /**
     * Method to remove a movie from the list of favorite movies.
     * @param user The user for whom the movie is being removed.
     * @param movie The movie to be removed from the favorites.
     * @return A string message indicating the success or failure of the operation.
     */
    public String removeMovie(User user, Movie movie) {
        if (contains(movie)) {
            UserDAO userDAO = new UserDAO();
            userDAO.removeMovieFromFavorites(user, movie);

            favoriteMovies.remove(movie);
            return "Successfully removed " + movie.getTitle() + " from favorites.";
        } else {
            return movie.getTitle() + " is not in favorites.";
        }
    }

    /**
     * Method to check if a movie is already in the list of favorite movies.
     * @param movie The movie to check.
     * @return True if the movie is in favorites, false otherwise.
     */
    public boolean contains(Movie movie) {
        return favoriteMovies.contains(movie);
    }

    /**
     * Method to get the number of movies in the list of favorite movies.
     * @return The number of favorite movies.
     */
    public int length() {
        return favoriteMovies.size();
    }

    /**
     * Method to get a copy of the list of favorite movies.
     * @return A copy of the list of favorite movies.
     */
    public List<Movie> getFavoriteMovies() {
        return new ArrayList<>(favoriteMovies);
    }
}
