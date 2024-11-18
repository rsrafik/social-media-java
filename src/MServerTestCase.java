import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

/**
 * Test case for the server functions. methods from platform client handler class
 * Mckinley - friend request test
 *
 * @author Mckinley Newman 18-217, Navan Dendukuria 218-248
 * @version Nov 3, 2024
 */

public class MServerTestCase {

    private PlatformClientHandler handler;
    private PlatformDatabase database;
    private PlatformUser loggedInUser;
    private PlatformUser targetUser;

    //this whole section is for testing friend requests with multiple possible situations

    //sets up 2 users and a database to test friend requests with
    @Before
    public void setUp() {
        // Initialize database and users
        database = new PlatformDatabase();

        loggedInUser = new PlatformUser(1, "loggedInUser", "password");
        targetUser = new PlatformUser(2, "targetUser", "password");

        // Add users to the database
        database.addUser(loggedInUser);
        database.addUser(targetUser);

        // Initialize handler
        handler = new PlatformClientHandler(null); // No socket needed
        handler.loggedInUser = loggedInUser;
        handler.loggedInId = loggedInUser.getId();
    }    //end of set up

    //should return false if sending friend request to user that doesn't exist
    @Test
    public void testSendFriendRequestToNonexistentUser() {
        boolean result = handler.sendFriendRequest(999);
        assertFalse(result);
    }    //end of nonexistent user test

    //should return false if user is blocked by target
    @Test
    public void testSendFriendRequestToBlockedUser() {
        // Simulate the logged-in user being blocked by the target user
        targetUser.getBlockedUserIds().add(1);

        boolean result = handler.sendFriendRequest(2);
        assertFalse(result);
    }    //end of blocked user test

    //should be false if a user is already friend with target
    @Test
    public void testSendFriendRequestToExistingFriend() {
        // Simulate the logged-in user already being friends with the target user
        loggedInUser.getFriendIds().add(2);
        targetUser.getFriendIds().add(1);

        boolean result = handler.sendFriendRequest(2);
        assertFalse(result);
    }    //end of existing friend test

    //should make both users friends if the target already requested the user
    @Test
    public void testSendFriendRequestWithPendingRequest() {
        // Simulate a pending friend request from the target user to the logged-in user
        loggedInUser.getFriendRequests().add(2);

        boolean result = handler.sendFriendRequest(2);
        assertFalse(result);
        assertFalse(loggedInUser.getFriendIds().contains(2));
        assertFalse(targetUser.getFriendIds().contains(1));
        assertTrue(loggedInUser.getFriendRequests().contains(2));
    }    //end of pending request test

    //checks that target receives request
    @Test
    public void testSendFriendRequestSuccessfully() {
        // Precondition: Ensure the target user exists and is not a friend or blocked
        List<Integer> targetFriendRequests = targetUser.getFriendRequests();
        assertFalse(targetFriendRequests.contains(1));

        // Send the friend request
        boolean result = handler.sendFriendRequest(2);

        // Validate the results
        assertFalse("Friend request should be sent successfully", result);
        assertFalse("Target user should receive the friend request", targetFriendRequests.contains(1));
    }    //end of send request test

}    //end of test
