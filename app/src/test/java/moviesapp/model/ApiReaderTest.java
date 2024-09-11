package moviesapp.model;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ApiReaderTest {

    private final String apiKey = "6823e7296bee3292692bfb54cd2cd17d";
    private final String readAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ODIzZTcyOTZiZWUzMjkyNjkyYmFmYjU0Y2QyY2NkMTdkIiwic3ViIjoiNjViODJmOWE4YzMxNTkwMTdiZjBkMmJhIiwic2NvcGUiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.iz0j8TRD4ZIDkJW4DSTNUuSygFtVsGd3uQRkgR-sWY8";

    @Test
    void testGetMovies() {
        ApiReader apiReader = new ApiReader(apiKey, readAccessToken);
        try {
            String movies = apiReader.getMovies(1);
            assertNotNull(movies);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void testGetMovieDetails() {
        ApiReader apiReader = new ApiReader(apiKey, readAccessToken);
        try {
            String movieDetails = apiReader.getMovieDetails(123);
            assertNotNull(movieDetails);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }


}
