import org.junit.Test;

import java.net.Socket;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

/**
 * Test case for the methods isLoggedIn(), LogIn(), LogOut(), CreateUser(), and CreatePost() in the
 * PlatformClientHandler class.
 *
 * @author Navan Dendukuri, L22
 * @version November 17, 2024
 */
public class PlatformClientHandlerTestCase {

    // Tests the isLoggedIn method from PlatformClientHandler
    @Test
    public void testIsLoggedIn() {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        assertFalse(handler.isLoggedIn());
        handler.createUser("User", "Password");
        handler.logIn("User", "Password");
        assertTrue(handler.isLoggedIn());
        handler.logOut();
        assertFalse(handler.isLoggedIn());
    }

    // Tests the LogIn method from PlatformClientHandler
    @Test
    public void testLogIn() {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        boolean isUserCreated = handler.createUser("User", "Password");
        assertTrue(isUserCreated);
        boolean loginSuccessful = handler.logIn("User", "Password");
        assertTrue(loginSuccessful);
        boolean loginFailed = handler.logIn("User", "WrongPassword");
        assertFalse(loginFailed);
    }

    // Tests the LogOut method from PlatformClientHandler
    @Test
    public void testLogOut() {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        handler.createUser("User", "Password");
        handler.logIn("User", "Password");
        boolean logoutSuccessful = handler.logOut();
        assertTrue(logoutSuccessful);
        assertFalse(handler.isLoggedIn());
    }

    // Tests the createUser method from PlatformClientHandler
    @Test
    public void testCreateUser() {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        boolean userCreated = handler.createUser("Username", "Password");
        assertTrue(userCreated);
        boolean duplicateUser = handler.createUser("Username", "newPassword");
        assertFalse(duplicateUser);
    }

    // Tests the createPosts method from PlatformClientHandler
    @Test
    public void testCreatePost() {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        handler.createUser("User", "Password");
        handler.logIn("User", "Password");
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        boolean postCreated = handler.createPost("Post successfully created", testImage);
        assertTrue(postCreated);
        handler.logOut();
        boolean postFailed = handler.createPost("Failed post creation", testImage);
        assertFalse(postFailed);
    }
}