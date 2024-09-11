package moviesapp.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import moviesapp.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;

/**
 * Controller class for the main application window.
 */
public class AppController implements Initializable {
    private User user;
    private List<Movie> movies;
    private MovieService movieService;

    @FXML
    private Label usernameLabel;

    @FXML
    private GridPane movieContainer;

    @FXML
    private Label favoritesLabel;

    @FXML
    private Hyperlink disconnectHyperLink;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filters;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label SimilarMovies;

    @FXML
    private Label DescriptionLabel;

    @FXML
    private Label genresLabel;

    @FXML
    private Label popularLabel;

    int row = 1;
    int column = 0;
    private static final int MAX_COLUMNS = 7;
    ApiReader apiReader = new ApiReader("6823e7296bee3292692bfb54cd2cd17d", "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ODIzZTcyOTZiZWzMjkyNjkyYmF");
    private Label selectedLabel;

    /**
     * Handles the click event on a label in the genre section.
     *
     * @param clickedLabel The label that was clicked.
     */
    public void handleLabelClick(Label clickedLabel) {
        // Deselect the previously selected label if any
        if (selectedLabel != null) {
            selectedLabel.getStyleClass().remove("selected");
        }

        // Select the clicked label
        clickedLabel.getStyleClass().add("selected");

        // Update the selected label
        selectedLabel = clickedLabel;
    }

    /**
     * Initializes the main application window.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = UserSession.getInstance(null).getUser();
        if (user != null) {
            String username = user.getUsername();
            usernameLabel.setText(username);
        } else {
            usernameLabel.setText("Username: Not logged in");
        }
        movieService = new MovieService(apiReader);
        handleGenresClick(null);
        movies = new ArrayList<>();
        loadFavorites(); // Load the favorite movies and update the favorite icon
        for (Movie movie : movies) {
            addMovieToGrid(movie);
        }
        favoritesLabel.setOnMouseClicked(event -> {
            DescriptionLabel.setText("Your List:");
            handleLabelClick(favoritesLabel);
            int row = 1;
            int column = 0;

            User currentUser = UserSession.getInstance(null).getUser();
            if (currentUser != null) {
                UserDAO userDAO = new UserDAO();
                List<Movie> favorites = userDAO.getFavorites(currentUser); // Fetch the favorite movies from the database

                movieContainer.getChildren().clear(); // Clear the GridPane
                // Add favorite movies to the GridPane
                for (Movie movie : favorites) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/movie.fxml"));
                        Parent movieCard = fxmlLoader.load();

                        MovieController movieController = fxmlLoader.getController();
                        movieController.setData(movie);
                        movieController.setAppController(this);

                        if (column == 7) {
                            column = 0;
                            row++;
                        }

                        movieContainer.add(movieCard, column++, row);

                        if (column == 1 && movieContainer.getChildren().size() > 0) {
                            RowConstraints rowConstraints = new RowConstraints();
                            rowConstraints.setMinHeight(330);
                            movieContainer.getRowConstraints().add(rowConstraints);
                        }
                    } catch (IOException e) {
                        System.out.println("BUG");
                    }
                }
            }
            scrollPane.setVvalue(0);
        });
        filters.setItems(FXCollections.observableArrayList("Title", "Year", "Genres", "rating"));
        filters.getSelectionModel().selectFirst();
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchMovies(null);
            }
        });
        searchField.setOnMouseClicked(mouseEvent -> {
            searchField.clear();
        });
        SimilarMovies.setOnMouseClicked(event -> {
            User currentUser = UserSession.getInstance(null).getUser();
            List<Movie> recommendedMovies = movieService.getRecommendedMovies(currentUser);

            if (recommendedMovies.isEmpty()) {
                DescriptionLabel.setText("Add some movies to your favorites to get similar recommendations!");
            } else {
                DescriptionLabel.setText("For you:");
                handleLabelClick(SimilarMovies);
                int row = 1;
                int column = 0;

                movieContainer.getChildren().clear(); // Clear the GridPane
                // Add recommended movies to the GridPane
                for (Movie movie : recommendedMovies) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/movie.fxml"));
                        Parent movieCard = fxmlLoader.load();

                        MovieController movieController = fxmlLoader.getController();
                        movieController.setData(movie);
                        movieController.setAppController(this);

                        if (column == MAX_COLUMNS) {
                            column = 0;
                            row++;
                        }

                        movieContainer.add(movieCard, column++, row);

                        if (column == 1 && movieContainer.getChildren().size() > 0) {
                            RowConstraints rowConstraints = new RowConstraints();
                            rowConstraints.setMinHeight(330);
                            movieContainer.getRowConstraints().add(rowConstraints);
                        }
                    } catch (IOException e) {
                        System.out.println("BUG");
                    }
                }
                scrollPane.setVvalue(0);
            }
        });
        Platform.runLater(() -> {
            Stage stage = (Stage) scrollPane.getScene().getWindow();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });
    }

    /**
     * Loads the favorite movies and updates their icons.
     */
    private void loadFavorites() {
        User user = UserSession.getInstance(null).getUser();
        if (user != null) {
            UserDAO userDAO = new UserDAO();
            List<Movie> favorites = userDAO.getFavorites(user); // Fetch the favorite movies from the database

            // Update the favorite icon for each favorite movie
            for (Movie favorite : favorites) {
                if (favorite != null) {
                    favorite.setFavorite(true);
                }
            }
        }
    }

    /**
     * Gets the description label.
     *
     * @return The description label.
     */
    public Label getDescriptionLabel() {
        return DescriptionLabel;
    }

    /**
     * Handles the click event on the "Genres" section.
     *
     * @param event The MouseEvent triggering the action.
     */
    @FXML
    public void handleGenresClick(MouseEvent event) {
        handleLabelClick(genresLabel);
        DescriptionLabel.setText("Choose a category to discover movies:");
        try {
            String[] genres = {"Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama", "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "Thriller", "War", "Western"};
            row = 1;
            column = 0;

            // Clear the GridPane
            movieContainer.getChildren().clear();
            // For each genre, create an instance of GenresController and add it to your GridPane
            for (String genre : genres) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/genres.fxml"));
                Parent genreCard = fxmlLoader.load();
                GenresController genresController = fxmlLoader.getController();
                genresController.setApiReader(apiReader);
                genresController.setAppController(this);
                genresController.setDescriptionLabel(getDescriptionLabel()); // Pass the DescriptionLabel
                genresController.setData(genre);

                // Add genreCard to your GridPane
                movieContainer.add(genreCard, column++, row);

                // If column reaches 7, reset column to 0 and increment row
                if (column == 7) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the search for movies based on the user's input.
     *
     * @param event The MouseEvent triggering the action.
     */
    @FXML
    private void searchMovies(MouseEvent event) {
        String query = searchField.getText();
        String filter = filters.getValue();
        List<Movie> searchResults = new ArrayList<>();
        DescriptionLabel.setText("Search results for \"" + query + "\" with filter \"" + filter + "\"");

        MovieFilterGUI movieFilterGUI = new MovieFilterGUI(new ApiReader("6823e7296bee3292692bfb54cd2cd17d", "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ODIzZTcyOTZiZWzMjkyNjkyYmF"));

        switch (filter) {
            case "Title":
                searchResults = movieFilterGUI.searchMoviesByTitle(query);
                break;
            case "Year":
                searchResults = movieFilterGUI.searchMoviesByYear(query);
                break;
            case "Genres":
                searchResults = movieFilterGUI.searchMoviesByGenre(query);
                break;
            case "rating":
                try {
                    double minRating = Double.parseDouble(query);
                    searchResults = movieFilterGUI.searchMoviesByRating(minRating);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid rating format. Please enter a number.");
                }
                break;
        }

        movieContainer.getChildren().clear(); // Clear the GridPane
        row = 1; // Reset row
        column = 0; // Reset column

        for (Movie movie : searchResults) {
            addMovieToGrid(movie);
        }
    }

    /**
     * Adds a movie to the GridPane.
     *
     * @param movie The movie to be added.
     */
    public void addMovieToGrid(Movie movie) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/movie.fxml"));
            Parent movieCard = fxmlLoader.load();

            MovieController movieController = fxmlLoader.getController();
            movieController.setData(movie); // Set the movie data

            // Check if the movie is a favorite
            User user = UserSession.getInstance(null).getUser();
            if (user != null) {
                UserDAO userDAO = new UserDAO();
                boolean isFavorite = userDAO.isFavorite(user, movie);
                movie.setFavorite(isFavorite);
            }

            movieController.updateFavoriteIcon(); // Update the favorite icon
            movieController.setAppController(this);

            if (column == MAX_COLUMNS) {
                column = 0;
                row++;
            }

            movieContainer.add(movieCard, column++, row);

            if (column == 1 && movieContainer.getChildren().size() > 0) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(330);
                movieContainer.getRowConstraints().add(rowConstraints);
            }
        } catch (IOException e) {
            System.out.println("BUG");
        }
    }

    /**
     * Handles the click event on the "Discover" section.
     *
     * @param event The MouseEvent triggering the action.
     */
    @FXML
    public void handleDiscoverClick(MouseEvent event) {
        handleLabelClick(popularLabel);
        DescriptionLabel.setText("Some popular movies:");
        movieContainer.getChildren().clear(); // Clear the GridPane
        row = 1; // Reset row
        column = 0; // Reset column
        if (movies == null || movies.isEmpty()) {
            for (int page = 1; page <= 1; page++) {
                int finalPage = page;
                try {
                    String response = apiReader.getMovies(finalPage);
                    InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                    JsonReader jsonReader = new JsonReader(stream, apiReader);
                    List<Movie> moviesFromApi = jsonReader.readJson();
                    synchronized (movies) {
                        movies.addAll(moviesFromApi);
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred while reading from the TMDB API.");
                    e.printStackTrace();
                }
            }
        }
        for (Movie movie : movies) {
            addMovieToGrid(movie);
        }
        scrollPane.setVvalue(0);
    }

    /**
     * Shows the details of a specific movie.
     *
     * @param movie The movie for which details are to be shown.
     */
    @FXML
    void showMovieDetails(Movie movie) {
        try {
            // Load the details view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/details.fxml"));
            Parent detailsView = fxmlLoader.load();

            // Get the DetailsController and set the movie details
            DetailsController detailsController = fxmlLoader.getController();
            detailsController.setMovieDetails(movie);

            // Set the AppController instance on the DetailsController
            detailsController.setAppController(this);

            // Create a new Stage (window)
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setTitle("Movie Details");

            // Create a new Scene with the details view and set it on the Stage
            Scene scene = new Scene(detailsView, 661, 288);
            newWindow.setScene(scene);

            // Show the new window
            newWindow.show();
            scrollPane.setVvalue(0);
        } catch (IOException e) {
            System.out.println("An error occurred while loading the movie details view.");
            e.printStackTrace();
        }
    }

    /**
     * Shows movies directed by a specific director.
     *
     * @param directorName The name of the director.
     */
    public void showMoviesByDirector(String directorName) {
        try {
            List<Movie> directorMovies = movieService.getMoviesByDirector(directorName);
            // Create a new Stage (window)
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setTitle("Movies by " + directorName);
            // Create a new GridPane
            GridPane gridPane = new GridPane();
            int row = 0;
            int column = 0;
            for (Movie movie : directorMovies) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/movie.fxml"));
                    Parent movieCard = fxmlLoader.load();
                    MovieController movieController = fxmlLoader.getController();
                    movieController.setData(movie);
                    movieController.setAppController(this);
                    if (column == MAX_COLUMNS) {
                        column = 0;
                        row++;
                    }
                    gridPane.add(movieCard, column++, row);
                    if (column == 1) {
                        RowConstraints rowConstraints = new RowConstraints();
                        rowConstraints.setMinHeight(330);
                        gridPane.getRowConstraints().add(rowConstraints);
                    }
                } catch (IOException e) {
                    System.out.println("BUG");
                }
            }
            gridPane.setStyle("-fx-background-color: #181818;");

            // Wrap the GridPane in a ScrollPane
            ScrollPane scrollPane = new ScrollPane(gridPane);
            scrollPane.setFitToWidth(true); // to ensure the width of the GridPane matches the ScrollPane

            Scene scene = new Scene(scrollPane, 1260, 850);
            newWindow.setScene(scene);
            // Show the new window
            newWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the content of the GridPane.
     */
    public void clearGridPane() {
        movieContainer.getChildren().clear();
        row = 1;
        column = 0;
    }

    /**
     * Handles the click event on the "Disconnect" link.
     *
     * @param event The MouseEvent triggering the action.
     */
    @FXML
    public void handleDisconnectClick(MouseEvent event) {
        // Terminate the user session
        UserSession.getInstance(null).cleanUserSession();

        // Use NavigationManager to navigate to the login view
        NavigationManager navigationManager = new NavigationManager();
        try {
            navigationManager.navigateTo("/Login.fxml", (Stage) disconnectHyperLink.getScene().getWindow());
        } catch (IOException e) {
            System.out.println("An error occurred while loading the login view.");
            e.printStackTrace();
        }
    }
}
