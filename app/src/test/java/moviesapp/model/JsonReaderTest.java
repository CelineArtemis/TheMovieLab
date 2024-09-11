//package moviesapp.model;
//
//import org.junit.jupiter.api.Test;
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.time.LocalDate;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
//class JsonReaderTest {
//
//    private final String apiKey = "6823e7296bee3292692bfb54cd2cd17d";
//    private final String readAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ODIzZTcyOTZiZWUzMjkyNjkyYmFmYjU0Y2QyY2NkMTdkIiwic3ViIjoiNjViODJmOWE4YzMxNTkwMTdiZjBkMmJhIiwic2NvcGUiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.iz0j8TRD4ZIDkJW4DSTNUuSygFtVsGd3uQRkgR-sWY8";
//
//    @Test
//    void testReadJson() {
//        // Sample JSON data for testing
//        String jsonData = "{\"results\":[{\"id\":1,\"title\":\"Movie 1\",\"overview\":\"Overview 1\",\"release_date\":\"2023-01-01\",\"vote_average\":7.5,\"genre_ids\":[1,2],\"poster_path\":\"/poster1.jpg\"},{\"id\":2,\"title\":\"Movie 2\",\"overview\":\"Overview 2\",\"release_date\":\"2023-02-01\",\"vote_average\":8.0,\"genre_ids\":[3,4],\"poster_path\":\"/poster2.jpg\"}]}";
//        InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());
//        ApiReader apiReader = new ApiReader(apiKey, readAccessToken);
//        JsonReader jsonReader = new JsonReader(inputStream, apiReader);
//
//        try {
//            List<Movie> movies = jsonReader.readJson();
//            assertNotNull(movies);
//            assertEquals(2, movies.size());
//
//            Movie movie1 = movies.get(0);
//            assertEquals(1, movie1.getId());
//            assertEquals("Movie 1", movie1.getTitle());
//            assertEquals("Overview 1", movie1.getOverview());
//            assertEquals(LocalDate.of(2023, 1, 1), movie1.getReleaseDate());
//            assertEquals(7.5, movie1.getRating());
//            assertEquals(List.of("Genre 1", "Genre 2"), movie1.getGenres());
//            assertEquals("/poster1.jpg", movie1.getPosterPath());
//
//            Movie movie2 = movies.get(1);
//            assertEquals(2, movie2.getId());
//            assertEquals("Movie 2", movie2.getTitle());
//            assertEquals("Overview 2", movie2.getOverview());
//            assertEquals(LocalDate.of(2023, 2, 1), movie2.getReleaseDate());
//            assertEquals(8.0, movie2.getRating());
//            assertEquals(List.of("Genre 3", "Genre 4"), movie2.getGenres());
//            assertEquals("/poster2.jpg", movie2.getPosterPath());
//
//        } catch (Exception e) {
//            fail("Exception occurred: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void testReadPersonMovies() {
//        // Sample JSON data for testing
//        String jsonData = "{\"crew\":[{\"id\":1,\"title\":\"Movie 1\",\"overview\":\"Overview 1\",\"release_date\":\"2023-01-01\",\"vote_average\":7.5,\"genre_ids\":[1,2],\"poster_path\":\"/poster1.jpg\"},{\"id\":2,\"title\":\"Movie 2\",\"overview\":\"Overview 2\",\"release_date\":\"2023-02-01\",\"vote_average\":8.0,\"genre_ids\":[3,4],\"poster_path\":\"/poster2.jpg\"}]}";
//        InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());
//        ApiReader apiReader = new ApiReader(apiKey, readAccessToken);
//        JsonReader jsonReader = new JsonReader(inputStream, apiReader);
//
//        try {
//            List<Movie> movies = jsonReader.readPersonMovies("Director Name");
//            assertNotNull(movies);
//            assertEquals(2, movies.size());
//
//            // Test properties of the first movie
//            Movie movie1 = movies.get(0);
//            assertEquals(1, movie1.getId());
//            assertEquals("Movie 1", movie1.getTitle());
//            assertEquals("Overview 1", movie1.getOverview());
//            assertEquals(LocalDate.of(2023, 1, 1), movie1.getReleaseDate());
//            assertEquals(7.5, movie1.getRating());
//            assertEquals(List.of("Genre 1", "Genre 2"), movie1.getGenres());
//            assertEquals("/poster1.jpg", movie1.getPosterPath());
//            assertEquals("Director Name", movie1.getDirector());
//
//            // Test properties of the second movie similarly
//
//        } catch (Exception e) {
//            fail("Exception occurred: " + e.getMessage());
//        }
//    }
//}
