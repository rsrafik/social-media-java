import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Test case for the User interface and class
 *
 * @author Mckinley Newman
 * @version Nov 2, 2024
 */
public class UserTestCase {

    //check if user interface exist
    @Test
    public void testUserInterfaceExists() {
        try {
            // Attempt to load the User interface using reflection
            Class<?> userInterface = Class.forName("User");

            if (!userInterface.isInterface()) {
                fail("User exists but is not an interface");
            }

        } catch (ClassNotFoundException e) {
            // Fail the test if the interface does not exist
            fail("User interface does not exist");
        }
    }

    //check if PlatformUser implements User
    @Test
    public void testPlatformImplementsUser() {
        PlatformUser user = new PlatformUser("JohnS", "Hacker");

        assertTrue("PlatformUser should implement User interface", user instanceof User);
    }    //end of checking implementation

    //this code tests each method, which we don't need to do.
    //do not include when submitting
    /**
    //checks getUserId, it should be 0 because I am only making one user
    @Test
    public void testGetUserID() {
        PlatformUser user = new PlatformUser("JohnS","Hacker");

        assertEquals("UserId should be 0",0,user.getUserId());
    }

    //test get Username
    @Test
    public void testGetUsername() {
        PlatformUser user = new PlatformUser("JohnS","Hacker");
        assertEquals("Username should be 'JohnS'","JohnS",user.getUsername());
    }

    //test setUsername
    @Test
    public void testSetUsername() {
        PlatformUser user = new PlatformUser("JohnS","Hacker");

        //changes user to Bob
        user.setUsername("Bob");
        //checks if user was changed to Bob
        assertEquals("Username should be updated to 'Bob'","Bob",user.getUsername());
    }

    //test getPassword
    // user interface doesn't have get password
    @Test
    public void testTestPassword() {
        PlatformUser user = new PlatformUser("JohnS","Hacker");
        assertTrue("UserPassword should be 'Hacker'","Hacker",user.testPassword());
    }

    //test setPassword
    @Test
    public void testSetPassword() {
        PlatformUser user = new PlatformUser("JohnS","Hacker");
        user.setPassword("Program");
        assertTrue("UserPassword should be updated to 'Program'",user.testPassword("Program"));
    }

    //test getFriendIds
    @Test
    public void testGetFriendIds() {
        //create user with some friends
        PlatformUser user = new PlatformUser("JohnS","Hacker");
        user.addFriendId(12);
        user.addFriendId(13);

        //get friend ids
        ArrayList<Integer> friendIds = user.getFriendIds();

        List<Integer> expectedIds = Arrays.asList(12, 13);
        assertEquals("FriendIds should match expected list",expectedIds,friendIds);
    }
 */



}    //end of UserTestCase
