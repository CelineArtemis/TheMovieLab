package moviesapp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * The Database class is responsible for creating a new database and necessary tables.
 */
public class Database {
    private static final String url = "jdbc:sqlite:mydatabase.db";

    /**
     * Method to create a new database and necessary tables.
     */
    public static void createNewDatabase() {
        // Try-with-resources to automatically close the connection
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // SQL statement to create 'Users' table
                String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                        + " id integer PRIMARY KEY,\n"
                        + " username text NOT NULL,\n"
                        + " password text NOT NULL,\n"
                        + " email text NOT NULL,\n"
                        + " favorites text DEFAULT ''\n"
                        + ");";

                // Execute the SQL statement
                stmt.execute(sql);

                // Call methods to create additional tables
                createFavoriteMoviesTable();
                createRatingTable();
            }
        } catch (Exception e) {
            // Print any exceptions that occur during database creation
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to create 'ratings' table.
     */
    public static void createRatingTable() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // SQL statement to create 'ratings' table
                String sql = "CREATE TABLE IF NOT EXISTS ratings (\n"
                        + " user_id integer NOT NULL,\n"
                        + " movie_id integer NOT NULL,\n"
                        + " rating real,\n"
                        + " PRIMARY KEY (user_id, movie_id)\n"
                        + ");";

                // Execute the SQL statement
                stmt.execute(sql);
            }

        } catch (Exception e) {
            // Print any exceptions that occur during table creation
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to create 'favorite_movies' table.
     */
    public static void createFavoriteMoviesTable() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // SQL statement to create 'favorite_movies' table
                String sql = "CREATE TABLE IF NOT EXISTS favorite_movies (\n"
                        + " user_id integer,\n"
                        + " movie_id integer,\n"
                        + " title text,\n"
                        + " year integer,\n"
                        + " release_date text,\n"
                        + " rating real,\n"
                        + " genres text,\n"
                        + " poster_path text,\n"
                        + " runtime integer,\n"
                        + " director text,\n"
                        + " overview text,\n"
                        + " cast text,\n"
                        + " PRIMARY KEY (user_id, movie_id)\n"
                        + ");";

                // Execute the SQL statement
                stmt.execute(sql);
            }
        } catch (Exception e) {
            // Print any exceptions that occur during table creation
            System.out.println(e.getMessage());
        }
    }
}