import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for the ClientHandler interface, verifying its existence and implementation
 * by the PlatformClientHandler class.
 *
 * @author Navan Dendukuri, L22
 * @version November 3, 2024
 */
public class ClientHandlerTestCase {

    /**
     * Tests if the ClientHandler interface exists.
     */
    @Test
    public void interfaceExists() {
        try {
            Class<?> clientHandler = Class.forName("ClientHandler");
            Assert.assertTrue("Post interface should exist.", clientHandler.isInterface());
        } catch (ClassNotFoundException e) {
            Assert.fail("ClientHandler interface does not exist");
        }
    }

    /**
     * Tests if PlatformPost implements the Post interface.
     */
    @Test
    public void interfaceImplemented() {
        boolean isImplemented = Post.class.isAssignableFrom(PlatformClientHandler.class);
        Assert.assertTrue("PlatformClientHandler should implement ClientHandler", isImplemented);
    }
}

