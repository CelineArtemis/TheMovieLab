/**
 * MovieFilterGUI is a utility class that provides methods for searching and filtering movies.
 * It interacts with the ApiReader and JsonReader classes to fetch and parse movie data.
 */
package moviesapp.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MovieFilterGUI is responsible for providing methods to search and filter movies based on different criteria.
 */
public class MovieFilterGUI {
    // ApiReader instance to fetch movie data from the API.
    private ApiReader apiReader;

    /**
     * Constructor for MovieFilterGUI.
     * Initializes the MovieFilterGUI with the provided ApiReader.
     * @param apiReader The ApiReader instance used for fetching movie data.
     */
    public MovieFilterGUI(ApiReader apiReader) {
        this.apiReader = apiReader;
    }

    /**
     * Search movies by title.
     * @param query The title query.
     * @return List of movies matching the title query.
     */
    public List<Movie> searchMoviesByTitle(String query) {
        // Delegate the search operation to the generic searchMovies method.
        return searchMovies(query, "title");
    }

    /**
     * Search movies by release year.
     * @param query The release year query.
     * @return List of movies matching the release year query.
     */
    public List<Movie> searchMoviesByYear(String query) {
        // Delegate the search operation to the generic searchMovies method.
        return searchMovies(query, "year");
    }

    /**
     * Search movies by genre.
     * @param genreName The name of the genre.
     * @return List of movies in the specified genre.
     */
    public List<Movie> searchMoviesByGenre(String genreName) {
        try {
            // Get the genre ID using the provided genre name.
            int genreId = apiReader.getGenreId(genreName);
            // Delegate the search operation to the generic searchMovies method.
            return searchMovies(String.valueOf(genreId), "genre");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Search movies by minimum rating.
     * @param minRating The minimum rating criterion.
     * @return List of movies with ratings greater than or equal to the specified minimum.
     */
    public List<Movie> searchMoviesByRating(double minRating) {
        try {
            // Fetch the JSON response for movies with a minimum rating.
            String jsonResponse = apiReader.searchMoviesByRating(minRating);
            // Convert the JSON response to an InputStream.
            InputStream stream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));
            // Create a JsonReader instance to parse the JSON and fetch movie details.
            JsonReader jsonReader = new JsonReader(stream, apiReader);
            // Return the list of movies obtained from the JSON data.
            return jsonReader.readJson();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Private method for generic movie search based on query and type.
     * @param query The search query.
     * @param type The type of search (e.g., title, year, genre).
     * @return List of movies based on the specified query and type.
     */
    private List<Movie> searchMovies(String query, String type) {
        try {
            // Fetch the JSON response for movies based on the provided query and type.
            String jsonResponse = apiReader.searchMovies(query, type);
            // Convert the JSON response to an InputStream.
            InputStream stream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));
            // Create a JsonReader instance to parse the JSON and fetch movie details.
            JsonReader jsonReader = new JsonReader(stream, apiReader);
            // Return the list of movies obtained from the JSON data.
            return jsonReader.readJson();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
