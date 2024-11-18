import org.junit.Test;
import java.net.Socket;
import java.awt.image.BufferedImage;
import static org.junit.Assert.*;

public class PlatformClientHandlerTestCase {

    @Test
    public void testIsLoggedIn() throws Exception {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        assertFalse(handler.isLoggedIn());
        handler.createUser("User", "Password");
        handler.logIn("User", "Password");
        assertTrue(handler.isLoggedIn());
        handler.logOut();
        assertFalse(handler.isLoggedIn());
    }

    @Test
    public void testLogIn() throws Exception {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        boolean isUserCreated = handler.createUser("User", "Password");
        assertTrue(isUserCreated);
        boolean loginSuccessful = handler.logIn("User", "Password");
        assertTrue(loginSuccessful);
        boolean loginFailed = handler.logIn("User", "WrongPassword");
        assertFalse(loginFailed);
    }

    @Test
    public void testLogOut() throws Exception {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        handler.createUser("User", "Password");
        handler.logIn("User", "Password");
        boolean logoutSuccessful = handler.logOut();
        assertTrue(logoutSuccessful);
        assertFalse(handler.isLoggedIn());
    }

    @Test
    public void testCreateUser() throws Exception {
        PlatformClientHandler handler = new PlatformClientHandler(new Socket());
        boolean userCreated = handler.createUser("User", "Password");
        assertTrue(userCreated);
        boolean duplicateUser = handler.createUser("User", "Password");
        assertFalse(duplicateUser);
    }

    @Test
    public void testCreatePost() throws Exception {
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