package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import moviesapp.model.Movie;
import moviesapp.model.User;
import moviesapp.model.UserDAO;
import moviesapp.model.UserSession;
import org.controlsfx.control.Rating;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller class for displaying movie details.
 */
public class DetailsController {
    private AppController appController;

    @FXML
    private Label cast;

    @FXML
    private Label director;

    @FXML
    private ImageView favorite;

    @FXML
    private Label genre;

    @FXML
    private ImageView moviePoster;

    @FXML
    private Label overview;

    @FXML
    private Label releasedate;

    @FXML
    private Label runtime;

    @FXML
    private Label title;

    @FXML
    private Label year;

    @FXML
    private Rating ratingControl;

    private Movie movie;

    /**
     * Sets the AppController for handling interactions.
     *
     * @param appController The AppController instance.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    /**
     * Sets the details of the movie to be displayed.
     *
     * @param movie The Movie object with details.
     */
    public void setMovieDetails(Movie movie) {
        this.movie = movie;
        try {
            title.setText(movie.getTitle());
            year.setText(String.valueOf(movie.getYear()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String releaseDateText = movie.getReleaseDate().format(formatter);
            releasedate.setText(releaseDateText);

            genre.setText(String.join(", ", movie.getGenres()));
            moviePoster.setImage(new Image(movie.getPosterPath()));

            overview.setWrapText(true);
            overview.setText(movie.getOverview());

            // Set runtime, director, and cast
            int totalMinutes = movie.getRuntime();
            int hours = totalMinutes / 60; // Get the number of hours
            int minutes = totalMinutes % 60; // Get the remaining minutes
            runtime.setText(hours + "h " + minutes + "m");
            if (movie.getDirector() != null) {
                director.setText(movie.getDirector());
            }

            List<String> castList = movie.getCast();
            if (castList.size() > 4) {
                castList = castList.subList(0, 4); // Get the first 4 actors
            }
            cast.setText(String.join(", ", castList));

        } catch (Exception e) {
            System.out.println("BUG1");
            e.printStackTrace();
        }

        // Set click event for director
        if (movie.getDirector() != null) {
            director.setText(movie.getDirector());
            director.setOnMouseClicked(event -> {
                appController.showMoviesByDirector(movie.getDirector());
            });
        }
        updateFavoriteIcon();

        // Set user's rating for the movie
        User user = UserSession.getInstance(null).getUser();
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            double rating = userDAO.getRating(user, movie.getId());
            ratingControl.setRating(rating);
            ratingControl.ratingProperty().addListener((observable, oldValue, newValue) -> handleRatingChange());
        }
    }

    /**
     * Updates the favorite icon based on the movie's favorite status.
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
     * Handles the click event on the favorite icon to add or remove the movie from favorites.
     *
     * @param event The MouseEvent triggering the action.
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
     * Handles the change in the user's rating for the movie.
     */
    @FXML
    public void handleRatingChange() {
        // Get the selected rating
        double rating = ratingControl.getRating();
        // Update the rating in the database
        User user = UserSession.getInstance(null).getUser();
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            userDAO.updateRating(user, movie.getId(), rating);
        }
    }
}
