package moviesapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
public class JsonReader {
    // JsonReader is responsible for reading and parsing JSON data from an API.
    // InputStream to read data from.
    private final InputStream inputStream;
    // API key for accessing the movie database API.
    private final String apiKey = "6823e7296bee3292692bfb54cd2cd17d";
    // ApiReader to fetch additional movie details.
    private final ApiReader apiReader;

    // Constructor initializes the InputStream and ApiReader.
    public JsonReader(InputStream inputStream, ApiReader apiReader) {
        this.inputStream = inputStream;
        this.apiReader = apiReader;
    }

    // readJson reads and parses JSON data from the InputStream, returning a list of Movie objects.
    public List<Movie> readJson() throws IOException {
        // Read data from InputStream.
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));
        StringBuilder content = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            content.append(line);
        }

        // Parse JSON data.
        JSONObject jsonObject = new JSONObject(content.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        List<Movie> movies = new ArrayList<>();
        Map<Integer, String> genreMap = this.getGenres();

        // Iterate over each movie in the JSON data.
        for(int i = 0; i < jsonArray.length(); ++i) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Movie movie = new Movie();
            // Set movie properties.
            movie.setId(obj.getInt("id"));
            movie.setTitle(obj.getString("title"));
            movie.setOverview(obj.getString("overview"));
            String releaseDateStr = obj.getString("release_date");
            if (!releaseDateStr.isEmpty()) {
                LocalDate releaseDate = LocalDate.parse(releaseDateStr);
                movie.setReleaseDate(releaseDate);
                int year = Integer.parseInt(releaseDateStr.substring(0, 4));
                movie.setYear(year);
            } else {
                movie.setYear(0000);
            }
            movie.setRating(obj.getDouble("vote_average"));
            JSONArray genreIds = obj.getJSONArray("genre_ids");
            List<String> genres = new ArrayList<>();

            // Set movie genres.
            for (int j = 0; j < genreIds.length(); ++j) {
                int genreId = genreIds.getInt(j);
                String genreName = genreMap.get(genreId);
                genres.add(genreName);
            }

            String posterPath = null;
            if (obj.has("poster_path") && !obj.isNull("poster_path")) {
                posterPath = obj.getString("poster_path");
                movie.setPosterPath(posterPath);
            }
            movie.setPosterPath(posterPath);
            movie.setGenres(genres);

            try {
                // Fetch and set additional movie details.
                String movieDetailsJson = apiReader.getMovieDetails(movie.getId());
                JSONObject movieDetails = new JSONObject(movieDetailsJson);
                movie.setRuntime(movieDetails.getInt("runtime"));
                String movieCreditsJson = apiReader.getMovieCredits(movie.getId());
                JSONObject movieCredits = new JSONObject(movieCreditsJson);
                JSONArray castArray = movieCredits.getJSONArray("cast");
                List<String> cast = new ArrayList<>();
                for (int k = 0; k < castArray.length(); k++) {
                    JSONObject castMember = castArray.getJSONObject(k);
                    cast.add(castMember.getString("name"));
                }
                movie.setCast(cast);
                JSONArray crewArray = movieCredits.getJSONArray("crew");
                for (int k = 0; k < crewArray.length(); k++) {
                    JSONObject crewMember = crewArray.getJSONObject(k);
                    if (crewMember.getString("job").equals("Director")) {
                        movie.setDirector(crewMember.getString("name"));
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            movies.add(movie);
        }
        return movies;
    }
    private Map<Integer, String> getGenres() throws IOException {
        // getGenres fetches the list of movie genres from the API.
        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + this.apiKey + "&language=en-EN";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();

        String inputLine;
        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        JSONObject jsonObject = new JSONObject(response.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("genres");
        Map<Integer, String> genres = new HashMap<>();

        // Parse and store genres.
        for(int i = 0; i < jsonArray.length(); ++i) {
            JSONObject genre = jsonArray.getJSONObject(i);
            int id = genre.getInt("id");
            String name = genre.getString("name");
            genres.put(id, name);
        }

        return genres;
    }
    public List<Movie> readPersonMovies(String directorName) throws IOException {
        // readPersonMovies reads and parses JSON data from the InputStream, returning a list of Movie objects.
        // Read data from InputStream.
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));
        StringBuilder content = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            content.append(line);
        }

        // Parse JSON data.
        JSONObject jsonObject = new JSONObject(content.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("crew");
        List<Movie> movies = new ArrayList<>();
        Map<Integer, String> genreMap = this.getGenres();

        // Iterate over each movie in the JSON data.
        for(int i = 0; i < jsonArray.length(); ++i) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (!obj.getString("job").equals("Director")) {
                continue;
            }
            Movie movie = new Movie();
            // Set movie properties.
            movie.setId(obj.getInt("id"));
            movie.setTitle(obj.getString("title"));
            movie.setOverview(obj.getString("overview"));
            if (obj.has("release_date") && !obj.isNull("release_date")) {
                String releaseDateStr = obj.getString("release_date");
                if (!releaseDateStr.isEmpty()) {
                    LocalDate releaseDate = LocalDate.parse(releaseDateStr);
                    movie.setReleaseDate(releaseDate);
                    int year = Integer.parseInt(releaseDateStr.substring(0, 4));
                    movie.setYear(year);
                }
            }
            movie.setRating(obj.getDouble("vote_average"));
            JSONArray genreIds = obj.getJSONArray("genre_ids");
            List<String> genres = new ArrayList<>();

// Set movie genres.
            for(int j = 0; j < genreIds.length(); ++j) {
                int genreId = genreIds.getInt(j);
                String genreName = genreMap.get(genreId);
                genres.add(genreName);
            }

            if (obj.has("poster_path") && !obj.isNull("poster_path")) {
                String posterPath = obj.getString("poster_path");
                movie.setPosterPath(posterPath);
            }

            movie.setGenres(genres);

            try {
                // Fetch and set additional movie details.
                String movieDetailsJson = apiReader.getMovieDetails(movie.getId());
                JSONObject movieDetails = new JSONObject(movieDetailsJson);
                movie.setRuntime(movieDetails.getInt("runtime"));
                String movieCreditsJson = apiReader.getMovieCredits(movie.getId());
                JSONObject movieCredits = new JSONObject(movieCreditsJson);
                JSONArray castArray = movieCredits.getJSONArray("cast");
                List<String> cast = new ArrayList<>();
                for (int k = 0; k < castArray.length(); k++) {
                    JSONObject castMember = castArray.getJSONObject(k);
                    cast.add(castMember.getString("name"));
                }
                movie.setCast(cast);
                movie.setDirector(directorName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            movies.add(movie);
        }

        return movies;
    }

}
