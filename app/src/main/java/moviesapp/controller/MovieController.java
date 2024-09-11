package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import moviesapp.model.Movie;
import moviesapp.model.User;
import moviesapp.model.UserDAO;
import moviesapp.model.UserSession;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for displaying movie details.
 */
public class MovieController {

    private AppController appController;
    private Movie movie;

    @FXML
    private Label genre;

    @FXML
    private ImageView moviePoster;

    @FXML
    private Label rating;

    @FXML
    private Label title;

    @FXML
    private ImageView favorite;

    @FXML
    private Label director;

    /**
     * Sets the AppController for handling interactions.
     *
     * @param appController The AppController instance.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    /**
     * Handles the click event on the movie.
     *
     * @param event The MouseEvent.
     */
    @FXML
    private void handleClick(MouseEvent event) {
        if (appController != null && movie != null) {
            appController.showMovieDetails(movie);
        }
    }

    /**
     * Sets data for displaying movie details.
     *
     * @param movie The Movie object.
     */
    public void setData(Movie movie) {
        this.movie = movie;
        if (movie.getTitle() != null) {
            title.setText(movie.getTitle());
        }
        if (movie.getGenres() != null) {
            List<String> genresList = movie.getGenres();
            String genresString = String.join(", ", genresList);
            genre.setText(genresString);
        }

        rating.setText(String.valueOf(movie.getRating()));

        // Set director label
        if (movie.getDirector() != null) {
            director.setText(movie.getDirector());
        }

        String posterUrl = movie.getPosterPath();
        Image image = new Image(posterUrl);
        moviePoster.setImage(image);
        updateFavoriteIcon();
    }

    /**
     * Updates the favorite icon based on the favorite status of the movie.
     */
    public void updateFavoriteIcon() {
        Image favoriteImage;
        if (movie.isFavorite()) {
            favoriteImage = new Image("/icons/favadded.png");
        } else {
            favoriteImage = new Image("/icons/addfav.png");
        }
        favorite.setImage(favoriteImage);
    }

    /**
     * Handles the click event on the favorite icon.
     *
     * @param event The MouseEvent.
     */
    @FXML
    private void handleFavoriteClick(MouseEvent event) {
        User user = UserSession.getInstance(null).getUser();
        UserDAO userDAO = new UserDAO();
        if (user != null) {
            try {
                if (movie.isFavorite()) {
                    userDAO.removeMovieFromFavorites(user, movie);
                    movie.setFavorite(false);
                } else {
                    userDAO.addMovieToFavorites(user, movie);
                    movie.setFavorite(true);
                }
                updateFavoriteIcon();
            } catch (Exception e) {
                // Print a user-friendly error message
                System.out.println("An error occurred while updating your favorites: " + e.getMessage());
            } finally {
                // Close the database connection
                userDAO.close();
            }
        }
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        director.setOnMouseClicked(event -> {
            if (appController != null && movie != null) {
                appController.showMoviesByDirector(movie.getDirector());
            }
        });
    }
}
