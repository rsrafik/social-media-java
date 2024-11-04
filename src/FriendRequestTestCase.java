import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for the FriendRequest interface, verifying its existence and implementation
 * by the PlatformFriendRequest class.
 *
 * @version November 2, 2024
 */
public class FriendRequestTestCase {

    /**
     * Tests if the FriendRequest interface exists.
     */
    @Test
    public void testUserInterfaceExists() {
        try {
            Class<?> userInterface = Class.forName("FriendRequest");
            if (!userInterface.isInterface()) {
                fail("FriendRequest exists but is not an interface");
            }
        } catch (ClassNotFoundException e) {
            fail("FriendRequest interface does not exist");
        }
    }

    /**
     * Tests if PlatformFriendRequest implements the FriendRequest interface.
     */
    @Test
    public void testPlatformImplementsFriendRequest() {
        PlatformFriendRequest friendRequest = new PlatformFriendRequest(1, "Be friends");
        assertTrue("PlatformFriendRequest should implement FriendRequest interface",
                friendRequest instanceof FriendRequest);
    }
}
