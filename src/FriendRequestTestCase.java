import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for the FriendRequest interface and implementation
 *
 * @author Mckinley Newman
 * @version Nov 2, 2024
 */
public class FriendRequestTestCase {

    //check if user interface exist
    @Test
    public void testUserInterfaceExists() {
        try {
            // Attempt to load the User interface using reflection
            Class<?> userInterface = Class.forName("FriendRequest");

            if (!userInterface.isInterface()) {
                fail("FriendRequest exists but is not an interface");
            }

        } catch (ClassNotFoundException e) {
            // Fail the test if the interface does not exist
            fail("FriendRequest interface does not exist");
        }
    }

    //check if PlatformFriendRequest implements FriendRequest
    @Test
    public void testPlatformImplementsFriendRequest() {
        PlatformFriendRequest friendRequest = new PlatformFriendRequest(1,"Be friends");

        assertTrue("PlatformFriendRequest should implement " +
            "FriendRequest interface", friendRequest instanceof FriendRequest);
    }    //end of checking implementation
}    //end of FriendRequest testcase