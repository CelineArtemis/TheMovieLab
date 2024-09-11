package moviesapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * The ApiReader class handles API requests to themoviedb.org.
 */
public class ApiReader {
    private final String apiKey;
    private final String readAccessToken;
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY_PARAM = "api_key=";
    private static final String PAGE_PARAM = "&page=";
    private static final String MOVIE_ID_PARAM = "?api_key=";
    private static final String POPULAR_MOVIES_URL = BASE_URL + "/movie/popular?" + API_KEY_PARAM;
    private static final String MOVIE_DETAILS_URL = BASE_URL + "/movie/";
    private static final String MOVIE_CREDITS_URL = MOVIE_DETAILS_URL;
    private static final String PERSON_SEARCH_URL = BASE_URL + "/search/person?" + API_KEY_PARAM;
    private static final String PERSON_MOVIES_URL = BASE_URL + "/person/";
    private static final String SIMILAR_MOVIES_URL = BASE_URL + "/movie/";
    private static final String GENRES_LIST_URL = BASE_URL + "/genre/movie/list?" + API_KEY_PARAM;
    public ApiReader(String apiKey, String readAccessToken) {
        this.apiKey = apiKey;
        this.readAccessToken = readAccessToken;
    }

    /**
     * Get a list of popular movies from the API.
     *
     * @param page The page number.
     * @return JSON response containing popular movies.
     * @throws IOException If an error occurs during the API request.
     */
    public String getMovies(int page) throws IOException {
        String url = POPULAR_MOVIES_URL + this.apiKey + PAGE_PARAM + page;
        return sendGetRequest(url);
    }

    /**
     * Get details for a specific movie from the API.
     *
     * @param movieId The ID of the movie.
     * @return JSON response containing movie details.
     * @throws IOException If an error occurs during the API request.
     */
    public String getMovieDetails(int movieId) throws IOException {
        String url = MOVIE_DETAILS_URL + movieId + MOVIE_ID_PARAM + this.apiKey;
        return sendGetRequest(url);
    }

    /**
     * Get credits for a specific movie from the API.
     *
     * @param movieId The ID of the movie.
     * @return JSON response containing movie credits.
     * @throws IOException If an error occurs during the API request.
     */
    public String getMovieCredits(int movieId) throws IOException {
        String url = MOVIE_CREDITS_URL + movieId + "/credits?" + API_KEY_PARAM + this.apiKey;
        return sendGetRequest(url);
    }

    /**
     * Send a GET request to the specified URL.
     *
     * @param url The URL to send the request to.
     * @return The response from the GET request.
     * @throws IOException If an error occurs during the request.
     */
    private String sendGetRequest(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + this.readAccessToken);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        return response.toString();
    }
    /**
     * Get the ID of a person based on their name.
     *
     * @param personName The name of the person to search for.
     * @return The ID of the person.
     * @throws IOException If an error occurs during the API request.
     */
    public int getPersonId(String personName) throws IOException {
        String url = PERSON_SEARCH_URL + this.apiKey + "&query=" + URLEncoder.encode(personName, "UTF-8") + "&language=en-US";
        String response = sendGetRequest(url);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");
        if (results.length() > 0) {
            return results.getJSONObject(0).getInt("id");
        } else {
            throw new IOException("Person not found");
        }
    }

    /**
     * Get movies associated with a specific person.
     *
     * @param personId The ID of the person.
     * @return JSON response containing movies associated with the person.
     * @throws IOException If an error occurs during the API request.
     */
    public String getPersonMovies(int personId) throws IOException {
        String url = PERSON_MOVIES_URL + personId + "/movie_credits?api_key=" + this.apiKey + "&language=en-US";
        return sendGetRequest(url);
    }

    /**
     * Get movies similar to a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return JSON response containing similar movies.
     * @throws IOException If an error occurs during the API request.
     */
    public String getSimilarMovies(int movieId) throws IOException {
        String url = SIMILAR_MOVIES_URL + movieId + "/similar?api_key=" + this.apiKey;
        return sendGetRequest(url);
    }

    /**
     * Get the ID of a genre based on its name.
     *
     * @param genreName The name of the genre.
     * @return The ID of the genre.
     * @throws IOException If an error occurs during the API request.
     */
    public int getGenreId(String genreName) throws IOException {
        String url = GENRES_LIST_URL + this.apiKey;
        String response = sendGetRequest(url);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray genres = jsonObject.getJSONArray("genres");

        for (int i = 0; i < genres.length(); i++) {
            JSONObject genre = genres.getJSONObject(i);
            if (genre.getString("name").toLowerCase().contains(genreName.toLowerCase())) {
                return genre.getInt("id");
            }
        }

        throw new IllegalArgumentException("Invalid genre name: " + genreName);
    }

    /**
     * Search for movies based on a query and type.
     *
     * @param query The search query.
     * @param type  The type of search (title, year, genre).
     * @return JSON response containing search results.
     * @throws IOException If an error occurs during the API request.
     */
    public String searchMovies(String query, String type) throws IOException {
        String url;
        switch (type) {
            case "title":
                url = BASE_URL + "/search/movie?" + API_KEY_PARAM + this.apiKey + "&query=" + URLEncoder.encode(query, "UTF-8");
                break;
            case "year":
                url = BASE_URL + "/discover/movie?" + API_KEY_PARAM + this.apiKey + "&primary_release_year=" + URLEncoder.encode(query, "UTF-8");
                break;
            case "genre":
                url = BASE_URL + "/discover/movie?" + API_KEY_PARAM + this.apiKey + "&with_genres=" + URLEncoder.encode(query, "UTF-8");
                break;
            default:
                throw new IllegalArgumentException("Invalid search type: " + type);
        }
        return sendGetRequest(url);
    }

    /**
     * Get movies by genre and page number.
     *
     * @param genreId The ID of the genre.
     * @param page    The page number.
     * @return JSON response containing movies of the specified genre.
     * @throws IOException If an error occurs during the API request.
     */
    public String getMoviesByGenre(int genreId, int page) throws IOException {
        String url = BASE_URL + "/discover/movie?" + API_KEY_PARAM + this.apiKey + "&with_genres=" + genreId + PAGE_PARAM + page;
        return sendGetRequest(url);
    }

    /**
     * Search for movies by minimum rating.
     *
     * @param minRating The minimum rating to filter movies.
     * @return JSON response containing movies with a rating greater than or equal to the specified minimum.
     * @throws IOException If an error occurs during the API request.
     */
    public String searchMoviesByRating(double minRating) throws IOException {
        String url = BASE_URL + "/discover/movie?" + API_KEY_PARAM + this.apiKey + "&vote_average.gte=" + minRating;
        return sendGetRequest(url);
    }
}
