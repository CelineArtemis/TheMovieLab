package moviesapp.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * The UserDAO class handles database operations related to users.
 */
public class UserDAO {
    private Connection conn;

    /**
     * Establishes a connection to the database.
     * @return The database connection.
     */
    private Connection connect() {
        String url = "jdbc:sqlite:mydatabase.db";
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                Statement statement = conn.createStatement();
                statement.setQueryTimeout(30); // set timeout to 30 sec.
                statement.executeUpdate("PRAGMA busy_timeout = 4000"); // set busy timeout to 4000ms
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks if an email already exists in the database.
     * @param email The email to check.
     * @return True if the email exists, false otherwise.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            // If the query returns a result, the email already exists
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * Retrieves a user from the database based on the username.
     * @param username The username of the user to retrieve.
     * @return The User object or null if not found.
     */
    public User getUser(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getString("username"));
                user.setId(rs.getInt("id"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Checks if a movie is marked as a favorite by a user.
     * @param user The user.
     * @param movie The movie.
     * @return True if the movie is a favorite, false otherwise.
     */
    public boolean isFavorite(User user, Movie movie) {
        String sql = "SELECT * FROM favorite_movies WHERE user_id = ? AND movie_id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, movie.getId());
            ResultSet rs = pstmt.executeQuery();

            // If the query returns a result, the movie is a favorite
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * Adds a new user to the database.
     * @param user The user to add.
     * @return True if the user is added successfully, false otherwise.
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO Users(username, password, email) VALUES(?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Adds a movie to the user's favorites in the database.
     * @param user The user.
     * @param movie The movie to add to favorites.
     */
    public void addMovieToFavorites(User user, Movie movie) {
        String sql = "INSERT INTO favorite_movies(user_id, movie_id, title, year, release_date, rating, genres, poster_path, runtime, director, overview, cast) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String releaseDateStr = movie.getReleaseDate().toString();

            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, movie.getId());
            pstmt.setString(3, movie.getTitle());
            pstmt.setInt(4, movie.getYear());
            pstmt.setString(5, releaseDateStr);
            pstmt.setDouble(6, movie.getRating());
            pstmt.setString(7, String.join(",", movie.getGenres()));
            pstmt.setString(8, movie.getPosterPath());
            pstmt.setInt(9, movie.getRuntime());
            pstmt.setString(10, movie.getDirector());
            pstmt.setString(11, movie.getOverview());
            pstmt.setString(12, String.join(",", movie.getCast()));
            pstmt.executeUpdate();
            movie.setFavorite(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a movie from the user's favorites in the database.
     * @param user The user.
     * @param movie The movie to remove from favorites.
     */
    public void removeMovieFromFavorites(User user, Movie movie) {
        String sql = "DELETE FROM favorite_movies WHERE user_id = ? AND movie_id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, movie.getId());
            pstmt.executeUpdate();
            movie.setFavorite(false);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves a list of favorite movies for a user from the database.
     * @param user The user.
     * @return A list of favorite movies.
     */
    public List<Movie> getFavorites(User user) {
        String sql = "SELECT * FROM favorite_movies WHERE user_id = ?";
        List<Movie> favoriteMovies = new ArrayList<>();

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                String releaseDateStr = rs.getString("release_date");
                LocalDate releaseDate = LocalDate.parse(releaseDateStr);
                movie.setReleaseDate(releaseDate);
                movie.setRating(rs.getDouble("rating"));
                movie.setGenres(Arrays.asList(rs.getString("genres").split(",")));
                movie.setPosterPath(rs.getString("poster_path"));
                movie.setRuntime(rs.getInt("runtime"));
                movie.setDirector(rs.getString("director"));
                movie.setOverview(rs.getString("overview")); // New line
                movie.setCast(Arrays.asList(rs.getString("cast").split(",")));
                movie.setFavorite(true);
                favoriteMovies.add(movie);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return favoriteMovies;
    }

    /**
     * Retrieves the rating given by a user to a movie from the database.
     * @param user The user.
     * @param movieId The ID of the movie.
     * @return The rating given by the user, or 0 if no rating is found.
     */
    public double getRating(User user, int movieId) {
        String sql = "SELECT rating FROM ratings WHERE user_id = ? AND movie_id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, movieId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("rating");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0; // Return 0 if no rating is found
    }

    /**
     * Updates the rating given by a user to a movie in the database.
     * @param user The user.
     * @param movieId The ID of the movie.
     * @param rating The new rating.
     */
    public void updateRating(User user, int movieId, double rating) {
        String sql = "INSERT OR REPLACE INTO ratings(user_id, movie_id, rating) VALUES(?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, movieId);
            pstmt.setDouble(3, rating);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
