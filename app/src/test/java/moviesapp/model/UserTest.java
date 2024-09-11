package moviesapp.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testConstructorWithUsername() {
        String username = "testUser";
        User user = new User(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void testIdGetterAndSetter() {
        User user = new User("");
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void testUsernameGetter() {
        String username = "testUser";
        User user = new User(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void testPasswordGetterAndSetter() {
        User user = new User("");
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    void testEmailGetterAndSetter() {
        User user = new User("");
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }
}
