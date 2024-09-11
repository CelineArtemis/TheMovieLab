package moviesapp.model;

import static org.junit.jupiter.api.Assertions.*;

import moviesapp.model.*;

import org.junit.jupiter.api.*;

import java.util.List;

public class MovieServiceTest {
    private static MovieService movieService;

    @BeforeAll
    public static void setUp() {
        ApiReader apiReader = new ApiReader("6823e7296bee3292692bfb54cd2cd17d", "votre_read_access_token");
        movieService = new MovieService(apiReader);
    }

    @Test
    public void testGetSimilarMovies() {
        Movie testMovie = new Movie();
        testMovie.setId(123); // Replace with a valid movie ID
        List<Movie> similarMovies = movieService.getSimilarMovies(testMovie);
        assertNotNull(similarMovies);
        // assertFalse(similarMovies.isEmpty());
    }

    @Test
    public void testGetRecommendedMovies() {
        User testUser = new User("testUser");
        List<Movie> recommendedMovies = movieService.getRecommendedMovies(testUser);
        assertNotNull(recommendedMovies);
        // Add assertions based on your expected behavior
    }

//    @Test
//    public void testGetMoviesByDirector() {
//        String directorName = "Christopher Nolan"; // Assurez-vous que ce réalisateur existe dans la base de données de l'API
//        List<Movie> moviesByDirector = movieService.getMoviesByDirector(directorName);
//        assertNotNull(moviesByDirector);
//        assertFalse(moviesByDirector.isEmpty()); // Vous vous attendez à ce que la liste ne soit pas vide
//    }

//    @Test
//    public void testGetMoviesByInvalidDirector() {
//        String invalidDirectorName = "Invalid Director"; // Un nom de réalisateur invalide
//        List<Movie> moviesByInvalidDirector = movieService.getMoviesByDirector(invalidDirectorName);
//        assertNotNull(moviesByInvalidDirector);
//        assertTrue(moviesByInvalidDirector.isEmpty()); // Vous vous attendez à ce que la liste soit vide
//    }

//    @Test
//    public void testGetMoviesByEmptyDirector() {
//        String emptyDirectorName = "";
//        List<Movie> moviesByEmptyDirector = movieService.getMoviesByDirector(emptyDirectorName);
//        assertNotNull(moviesByEmptyDirector);
//        assertTrue(moviesByEmptyDirector.isEmpty()); // Vous vous attendez à ce que la liste soit vide
//    }

//    @Test
//    public void testGetMoviesByNullDirector() {
//        String nullDirectorName = null;
//        List<Movie> moviesByNullDirector = movieService.getMoviesByDirector(nullDirectorName);
//        assertNotNull(moviesByNullDirector);
//        assertTrue(moviesByNullDirector.isEmpty()); // Vous vous attendez à ce que la liste soit vide
//    }




}