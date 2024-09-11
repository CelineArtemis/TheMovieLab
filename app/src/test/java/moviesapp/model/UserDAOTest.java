package moviesapp.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.List;

public class UserDAOTest {
    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        userDAO = new UserDAO();
        Database.createNewDatabase(); // Assuming database setup is required
    }

    @AfterAll
    public static void tearDown() {
        userDAO.close();
    }

    @Test
    public void testAddUser() {
        User testUser = new User("testUser");
        testUser.setPassword("testPassword");
        testUser.setEmail("test@example.com");

        assertTrue(userDAO.addUser(testUser));
        assertNotNull(userDAO.getUser("testUser"));
    }

    @Test
    public void testEmailExists() {
        assertFalse(userDAO.emailExists("nonexistent@example.com"));
        //assertTrue(userDAO.emailExists("admin@example.com"));
    }

    @Test
    public void testGetUser() {
        User adminUser = userDAO.getUser("admin");
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUsername());
        //assertEquals("admin@example.com", adminUser.getEmail());
    }


}