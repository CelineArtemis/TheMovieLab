/**
 * MovieService is responsible for providing movie-related services such as fetching similar movies,
 * recommending movies based on user favorites, and getting movies by director.
 */
package moviesapp.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieService provides various services related to movies using data obtained from the API.
 */
public class MovieService {
    // ApiReader instance to fetch movie data from the API.
    private ApiReader apiReader;

    /**
     * Constructor for MovieService.
     * Initializes the MovieService with the provided ApiReader.
     * @param apiReader The ApiReader instance used for fetching movie data.
     */
    public MovieService(ApiReader apiReader) {
        this.apiReader = apiReader;
    }

    /**
     * Get a list of similar movies for the given movie.
     * @param movie The reference movie to find similar ones.
     * @return List of similar movies.
     */
    public List<Movie> getSimilarMovies(Movie movie) {
        List<Movie> similarMovies = new ArrayList<>();
        try {
            // Fetch similar movies from the API using the movie ID.
            String response = apiReader.getSimilarMovies(movie.getId());

            if (response != null && !response.isEmpty()) {
                // Convert the API response to an InputStream.
                InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                // Create a JsonReader instance to parse the JSON and fetch movie details.
                JsonReader jsonReader = new JsonReader(stream, apiReader);
                // Obtain the list of similar movies from the parsed JSON data.
                similarMovies = jsonReader.readJson();
            } else {
                System.out.println("La réponse de l'API TMDB est vide pour le film avec l'ID : " + movie.getId());
            }
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de la lecture de l'API TMDB.");
            e.printStackTrace();
        }
        return similarMovies;
    }

    /**
     * Get a list of recommended movies based on the user's favorites.
     * @param currentUser The user for whom recommendations are generated.
     * @return List of recommended movies.
     */
    public List<Movie> getRecommendedMovies(User currentUser) {
        List<Movie> recommendedMovies = new ArrayList<>();

        if (currentUser != null) {
            // Create a UserDAO instance to interact with user data in the database.
            UserDAO userDAO = new UserDAO();
            // Fetch the favorite movies from the database for the current user.
            List<Movie> favorites = userDAO.getFavorites(currentUser);

            if (!favorites.isEmpty()) {
                // Limit the number of favorites to consider for recommendations (e.g., 2).
                List<Movie> limitedFavorites = favorites.stream().limit(2).collect(Collectors.toList());

                // Fetch similar movies for each favorite and add them to the recommendedMovies list.
                for (Movie favorite : limitedFavorites) {
                    recommendedMovies.addAll(getSimilarMovies(favorite));
                }
            }
        }

        // Remove duplicate movies from the recommended list.
        recommendedMovies = recommendedMovies.stream().distinct().collect(Collectors.toList());
        return recommendedMovies;
    }

    /**
     * Get a list of movies directed by a specified director.
     * @param directorName The name of the director.
     * @return List of movies directed by the specified director.
     */
    public List<Movie> getMoviesByDirector(String directorName) {
        try {
            // Get the person ID for the specified director name.
            int directorId = apiReader.getPersonId(directorName);
            // Fetch movies associated with the director from the API.
            String response = apiReader.getPersonMovies(directorId);
            // Convert the API response to an InputStream.
            InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            // Create a JsonReader instance to parse the JSON and fetch movie details.
            JsonReader jsonReader = new JsonReader(stream, apiReader);
            // Return the list of movies directed by the specified director.
            return jsonReader.readPersonMovies(directorName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
