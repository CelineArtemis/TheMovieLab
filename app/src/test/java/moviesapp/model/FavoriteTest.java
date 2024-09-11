package moviesapp.model;

import moviesapp.model.Favorite;
import moviesapp.model.Movie;
import moviesapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FavoriteTest {
    private Favorite favorite;
    private User user;
    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        favorite = new Favorite();
        user = new User("username"); // Assume User constructor exists
        movie1 = new Movie(); // Assume Movie constructor exists
        movie2 = new Movie(); // Assume Movie constructor exists
    }

/*    @Test
    void testAddMovie() {
        assertEquals(0, favorite.length());

        String result = favorite.addMovie(user, movie1);
        assertEquals("Successfully added Movie 1 to favorites.", result);
        assertTrue(favorite.contains(movie1));
        assertEquals(1, favorite.length());

        // Adding the same movie again should return a message indicating it's already in favorites
        result = favorite.addMovie(user, movie1);
        assertEquals("Movie 1 is already in favorites.", result);
        assertEquals(1, favorite.length());

        // Adding a different movie
        result = favorite.addMovie(user, movie2);
        assertEquals("Successfully added Movie 2 to favorites.", result);
        assertTrue(favorite.contains(movie2));
        assertEquals(2, favorite.length());
    }

    @Test
    void testRemoveMovie() {
        // Adding movies to favorites
        favorite.addMovie(user, movie1);
        favorite.addMovie(user, movie2);

        assertEquals(2, favorite.length());

        // Removing a movie
        String result = favorite.removeMovie(user, movie1);
        assertEquals("Successfully removed Movie 1 from favorites.", result);
        assertFalse(favorite.contains(movie1));
        assertEquals(1, favorite.length());

        // Removing a movie not in favorites should return a message indicating it's not in favorites
        result = favorite.removeMovie(user, movie1);
        assertEquals("Movie 1 is not in favorites.", result);
        assertEquals(1, favorite.length());

        // Removing another movie
        result = favorite.removeMovie(user, movie2);
        assertEquals("Successfully removed Movie 2 from favorites.", result);
        assertFalse(favorite.contains(movie2));
        assertEquals(0, favorite.length());
    }*/
}
