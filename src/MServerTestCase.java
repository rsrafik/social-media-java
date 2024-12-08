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
/*
public class MServerTestCase {

    private PlatformClientHandler handler;
    private PlatformDatabase database;
    private PlatformUser loggedInUser;
    private PlatformUser targetUser;

    //this whole section is for testing friend requests with multiple possible situations



}
*/
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;

public class MServerTestCase {
    private PlatformClientHandler clientHandler;
    private MockSocket mockSocket;
    private PlatformDatabase database;

    @Before
    public void setUp() {
        // Initialize a mock socket and client handler for testing
        mockSocket = new MockSocket();
        clientHandler = new PlatformClientHandler(mockSocket);
        database = PlatformClientHandler.getDatabase(); // Assuming we have a method to get the database
    }

    @Test
    public void testSendFollowRequestToNonexistentUser() {
        clientHandler.createUser("testUser", "password123");
        clientHandler.logIn("testUser", "password123");

        // Attempt to send follow request to a nonexistent user (user ID 9999 doesn't exist)
        boolean result = clientHandler.sendFollowRequest(9999);
        assertFalse(result); // Should return false as the user doesn't exist
    }

    @Test
    public void testSendFollowRequestToExistingFriend() {
        // Create users and log in
        clientHandler.createUser("user1", "password123");
        clientHandler.createUser("user2", "password123");
        clientHandler.logIn("user1", "password123");

        // Send a follow request from user1 to user2
        boolean result = clientHandler.sendFollowRequest(2);
        assertTrue(result); // Should be successful as user2 exists

        // Check that user2 is in user1's follow requests
        User user1 = database.getUser(1); // Assuming user ID 1 corresponds to "user1"
        assertTrue(user1.getFollowRequests().contains(2)); // user1 should have user2 in follow requests
    }

    @Test
    public void testSendFollowRequestWithPendingRequest() {
        // Create users and log in
        clientHandler.createUser("user1", "password123");
        clientHandler.createUser("user2", "password123");
        clientHandler.logIn("user1", "password123");

        // Send a follow request from user1 to user2
        boolean result = clientHandler.sendFollowRequest(2);
        assertTrue(result); // Should be successful initially

        // Attempt to send the follow request again (it should fail because it's already pending)
        result = clientHandler.sendFollowRequest(2);
        assertFalse(result); // Follow request is already pending, so it should fail
    }

    @Test
    public void testSendFollowRequestSuccessfully() {
        // Create users and log in
        clientHandler.createUser("user1", "password123");
        clientHandler.createUser("user2", "password123");
        clientHandler.logIn("user1", "password123");

        // Send a follow request from user1 to user2
        boolean result = clientHandler.sendFollowRequest(2);
        assertTrue(result); // Should be successful as the user exists and isn't blocked

        // Verify the follow request has been added to user2's follow requests
        User user2 = database.getUser(2); // Assuming user ID 2 corresponds to "user2"
        assertTrue(user2.getFollowRequests().contains(1)); // user2 should have user1's follow request
    }

    // Mock socket for testing
    private static class MockSocket extends java.net.Socket {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        private final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }
    }
}