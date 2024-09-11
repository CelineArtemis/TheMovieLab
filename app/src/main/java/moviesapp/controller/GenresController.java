package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import moviesapp.model.ApiReader;
import moviesapp.model.JsonReader;
import moviesapp.model.Movie;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Controller class for displaying genres.
 */
public class GenresController {
    @FXML
    private Separator GenreSeparator;
    @FXML
    private Label genreLabel;
    @FXML
    private ImageView moviePoster;
    private ApiReader apiReader;
    private AppController appController;
    private Label DescriptionLabel;
    /**
     * Sets the ApiReader for handling API calls.
     *
     * @param apiReader The ApiReader instance.
     */
    public void setApiReader(ApiReader apiReader) {
        this.apiReader = apiReader;
    }

    /**
     * Sets the DescriptionLabel for displaying genre details.
     *
     * @param DescriptionLabel The Label for genre details.
     */
    public void setDescriptionLabel(Label DescriptionLabel) {
        this.DescriptionLabel = DescriptionLabel;
    }

    /**
     * Sets the AppController for handling interactions.
     *
     * @param appController The AppController instance.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    /**
     * Sets the click event for a Node to show movies of the specified genre.
     *
     * @param node  The Node to set the click event on.
     * @param genre The genre to filter movies.
     */
    private void setOnClick(Node node, String genre) {
        node.setOnMouseClicked(event -> {
            try {
                int genreId = apiReader.getGenreId(genre);
                String response = apiReader.getMoviesByGenre(genreId, 1);
                DescriptionLabel.setText(genre + " Movies:");

                // Convert the response into a list of movies
                InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                JsonReader jsonReader = new JsonReader(stream, apiReader);
                List<Movie> moviesFromApi = jsonReader.readJson();

                // Clear the GridPane and add the movies
                appController.clearGridPane();
                for (Movie movie : moviesFromApi) {
                    appController.addMovieToGrid(movie);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets data for displaying genre details.
     *
     * @param genre The genre name.
     */
    public void setData(String genre) {
        genreLabel.setText(genre);
        moviePoster.setImage(new Image("/icons/genres/" + genre.toLowerCase() + ".jpg"));
        setOnClick(moviePoster, genre);
        setOnClick(genreLabel, genre);

        // Set background color based on genre
        switch (genre) {
            case "Action":
                GenreSeparator.setStyle("-fx-background-color: red;");
                break;
            case "Adventure":
                GenreSeparator.setStyle("-fx-background-color: green;");
                break;
            case "Animation":
                GenreSeparator.setStyle("-fx-background-color: yellow;");
                break;
            case "Comedy":
                GenreSeparator.setStyle("-fx-background-color: pink;");
                break;
            case "Crime":
                GenreSeparator.setStyle("-fx-background-color: black;");
                break;
            case "Documentary":
                GenreSeparator.setStyle("-fx-background-color: gray;");
                break;
            case "Drama":
                GenreSeparator.setStyle("-fx-background-color: navy;");
                break;
            case "Family":
                GenreSeparator.setStyle("-fx-background-color: orange;");
                break;
            case "Fantasy":
                GenreSeparator.setStyle("-fx-background-color: purple;");
                break;
            case "History":
                GenreSeparator.setStyle("-fx-background-color: brown;");
                break;
            case "Horror":
                GenreSeparator.setStyle("-fx-background-color: purple;");
                break;
            case "Music":
                GenreSeparator.setStyle("-fx-background-color: gold;");
                break;
            case "Mystery":
                GenreSeparator.setStyle("-fx-background-color: darkblue;");
                break;
            case "Romance":
                GenreSeparator.setStyle("-fx-background-color: darkred;");
                break;
            case "Science Fiction":
                GenreSeparator.setStyle("-fx-background-color: silver;");
                break;
            case "Thriller":
                GenreSeparator.setStyle("-fx-background-color: lightblue;");
                break;
            case "War":
                GenreSeparator.setStyle("-fx-background-color: khaki;");
                break;
            case "Western":
                GenreSeparator.setStyle("-fx-background-color: sienna;");
                break;
            default:
                GenreSeparator.setStyle("-fx-background-color: white;");
                break;
        }
    }
}
