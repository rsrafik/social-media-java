import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for the User interface, verifying its existence and implementation
 * by the PlatformUser class.
 *
 * @author Mckinley Newman, L22
 * @version November 2, 2024
 */
public class UserTestcase {

    /**
     * Tests if the User interface exists.
     */
    @Test
    public void testUserInterfaceExists() {
        try {
            Class<?> userInterface = Class.forName("User");
            if (!userInterface.isInterface()) {
                fail("User exists but is not an interface");
            }
        } catch (ClassNotFoundException e) {
            fail("User interface does not exist");
        }
    }

    /**
     * Tests if PlatformUser implements the User interface.
     */
    @Test
    public void testPlatformImplementsUser() {
        PlatformUser user = new PlatformUser("JohnS", "Hacker");
        assertTrue("PlatformUser should implement User interface", user instanceof User);
    }
}
