package moviesapp.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private Connection conn;
    private static final String url = "jdbc:sqlite:mydatabase.db";

    @BeforeEach
    void setUp() {
        // Set up the database before each test
        Database.createNewDatabase();
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Close the connection after each test
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testCreateNewDatabase() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");

            assertTrue(rs.next());
            assertEquals("Users", rs.getString("name"));

            assertTrue(rs.next());
            assertEquals("ratings", rs.getString("name"));

            assertTrue(rs.next());
            assertEquals("favorite_movies", rs.getString("name"));

            assertFalse(rs.next());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}