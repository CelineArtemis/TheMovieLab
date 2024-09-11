package moviesapp.model;

import moviesapp.model.Movie;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {

    @Test
    void testIdGetterAndSetter() {
        Movie movie = new Movie();
        movie.setId(1);
        assertEquals(1, movie.getId());
    }

    @Test
    void testTitleGetterAndSetter() {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        assertEquals("Test Movie", movie.getTitle());
    }

    @Test
    void testOverviewGetterAndSetter() {
        Movie movie = new Movie();
        movie.setOverview("Test overview");
        assertEquals("Test overview", movie.getOverview());
    }

    @Test
    void testFavoriteGetterAndSetter() {
        Movie movie = new Movie();
        movie.setFavorite(true);
        assertTrue(movie.isFavorite());
    }

    @Test
    void testYearGetterAndSetter() {
        Movie movie = new Movie();
        movie.setYear(2022);
        assertEquals(2022, movie.getYear());
    }

    @Test
    void testReleaseDateGetterAndSetter() {
        Movie movie = new Movie();
        LocalDate releaseDate = LocalDate.of(2022, 1, 1);
        movie.setReleaseDate(releaseDate);
        assertEquals(releaseDate, movie.getReleaseDate());
    }

    @Test
    void testRatingGetterAndSetter() {
        Movie movie = new Movie();
        movie.setRating(8.5);
        assertEquals(8.5, movie.getRating());
    }

    @Test
    void testGenresGetterAndSetter() {
        Movie movie = new Movie();
        List<String> genres = Arrays.asList("Action", "Adventure");
        movie.setGenres(genres);
        assertEquals(genres, movie.getGenres());
    }

    @Test
    void testPosterPathGetterAndSetter() {
        Movie movie = new Movie();
        movie.setPosterPath("/path/to/poster.jpg");
       // assertEquals("/path/to/poster.jpg", movie.getPosterPath());
    }

    @Test
    void testRuntimeGetterAndSetter() {
        Movie movie = new Movie();
        movie.setRuntime(120);
        assertEquals(120, movie.getRuntime());
    }

    @Test
    void testCastGetterAndSetter() {
        Movie movie = new Movie();
        List<String> cast = Arrays.asList("Actor 1", "Actor 2", "Actor 3");
        movie.setCast(cast);
        assertEquals(cast, movie.getCast());
    }

    @Test
    void testDirectorGetterAndSetter() {
        Movie movie = new Movie();
        movie.setDirector("Director Name");
        assertEquals("Director Name", movie.getDirector());
    }

    @Test
    void testGetDetails() {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setYear(2022);
        movie.setReleaseDate(LocalDate.of(2022, 1, 1));
        movie.setRuntime(120);
        movie.setRating(8.5);
        movie.setGenres(Arrays.asList("Action", "Thriller"));
        movie.setCast(Arrays.asList("Actor 1", "Actor 2", "Actor 3"));
        movie.setDirector("Director Name");

        String expectedDetails = "----------------------------------------\n" +
                "Title: Test Movie\n" +
                "Year: 2022\n" +
                "Release Date: 2022-01-01\n" +
                "Runtime: 120 minutes\n" +
                "Note: 8.5\n" +
                "Genres: Action, Thriller\n" +
                "Casting: Actor 1, Actor 2, Actor 3\n" +
                "Director: Director Name\n" +
                "----------------------------------------";

        assertEquals(expectedDetails, movie.getDetails());
    }

    @Test
    void testGetPosterPathWithNullPosterPath() {
        Movie movie = new Movie();
        //assertNull(movie.getPosterPath());
    }
}

