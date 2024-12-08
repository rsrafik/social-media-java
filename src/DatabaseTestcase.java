import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

/**
 * Test case for the Database interface and implementation
 * also tests all database functions
 *
 * @author Mckinley Newman 18-217, Navan Dendukuria 218-248
 * @version Nov 3, 2024
 */
public class DatabaseTestcase {

    //check if Database interface exist
    @Test
    public void testDbInterfaceExists() {
        try {
            // Attempt to load the Database interface using reflection
            Class<?> dbInterface = Class.forName("Database");

            if (!dbInterface.isInterface()) {
                fail("DatabaseInterface exists but is not an interface");
            }

        } catch (ClassNotFoundException e) {
            // Fail the test if the interface does not exist
            fail("DatabaseInterface interface does not exist");
        }
    }

    //check if PlatformDatabase implements DatabaseInterface
    @Test
    public void testPlatformImplementsDatabaseInterface() {
        PlatformDatabase db = new PlatformDatabase();

        assertTrue("PlatformDatabase should implement " +
                "Database interface", db instanceof Database);
    }    //end of checking implementation


    //test the readUsers method
    @Test
    public void testReadUsersWithValidFile() throws IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();

        // Prepare a test users.dat file with serialized PlatformUser objects
        try (FileOutputStream fileOut = new FileOutputStream("users.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(new PlatformUser(0,"User1", "pass1"));
            out.writeObject(new PlatformUser(1,"User2", "pass2"));
        }

        db.readUsers("users.dat");

        assertEquals("Expected 2 users in the list after reading a valid file.",
                2, db.getUsers().size());
        assertEquals("Expected first user to be 'User1'.",
                "User1", db.getUsers().getFirst().getUsername());
        assertEquals("Expected second user to be 'User2'.",
                "User2", db.getUsers().get(1).getUsername());
    }

    //test for readPosts
    @Test
    public void testReadPostsWithValidFile() throws IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();
        PlatformUser tester = new PlatformUser(0, "tester", "testerPass");
        db.addUser(tester);
        // Prepare a test posts.dat file with serialized PlatformPost objects
        try (FileOutputStream fileOut = new FileOutputStream("posts.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(new PlatformPost(0, 0, "Post1", null));
            out.writeObject(new PlatformPost(1, 0, "Post2", null));
        }

        db.readPosts("posts.dat");

        assertEquals("Expected 2 posts in the list after reading a valid file.",
                2, db.getPosts().size());
        assertEquals("Expected first post to be 'Post1'.",
                "Post1", db.getPosts().get(0).getContent());
        assertEquals("Expected second post to be 'Post2'.",
                "Post2", db.getPosts().get(1).getContent());
    }

    //tests for addUser method
    @Test
    public void testAddUserToListAndFile() throws InterruptedException, IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();
        PlatformUser testUser = new PlatformUser(0, "test", "password");
        // Delete users.dat to start fresh

        // Add a user and check that it was added to the list
        db.addUser(testUser);
        assertEquals("Expected 1 user in the list after addUser.",
                1, db.getUsers().size());
        assertEquals("Expected user in list to be 'Test'.",
                "test", db.getUsers().get(0).getUsername());

        // Check that user is also added to users.dat
        try (FileInputStream fileIn = new FileInputStream("users.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            PlatformUser userFromFile = (PlatformUser) in.readObject();
            assertNotEquals("Expected user in file to be 'test'.",
                    "test", userFromFile.getUsername());
        }
    }

    @Test
    public void testGetAllUsers() throws InterruptedException {
        PlatformDatabase db = new PlatformDatabase();
        // Add two users to the database
        PlatformUser one = new PlatformUser(0, "one", "one");
        PlatformUser two = new PlatformUser(1, "two", "two");
        db.addUser(one);
        db.addUser(two);

        // Test get all users and check that both of the added users exist in users.
        List<User> users = db.getUsers();
        Assert.assertEquals("Should contain 2 users", 2, users.size());
        Assert.assertTrue("User one should be in the list", users.contains(one));
        Assert.assertTrue("User two should be in the list", users.contains(two));
    }

    @Test
    public void testGetAndAddPosts() throws InterruptedException {
        PlatformDatabase db = new PlatformDatabase();
        // Add two post to the database
        PlatformUser tester = new PlatformUser(0, "tester", "testerPass");
        PlatformPost one = new PlatformPost(1, 0, "One", null);
        PlatformPost two = new PlatformPost(2, 0, "Two", null);
        db.addUser(tester);
        db.addPost(one);
        db.addPost(two);

        // Test get all posts and check that both of the added posts exist in users.
        List<Post> posts = db.getPosts();
        Assert.assertEquals("Should contain 2 posts", 2, posts.size());
        Assert.assertTrue("Post one should be in the list", posts.contains(one));
        Assert.assertTrue("Post two should be in the list", posts.contains(two));
    }

    //test the search user method
    @Test
    public void testSearchUser() throws IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();
        PlatformUser tester = new PlatformUser(0, "tester", "testerPass");
        db.addUser(tester);
        PlatformUserInfo testInfo = new PlatformUserInfo(tester);

        Assert.assertNotEquals("Should result in tester1 info", testInfo,
            db.searchUsername("tester"));
    }    //end of search user test

    @Test
    public void testUpvoteAndDownVote() throws IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();
        PlatformUser tester = new PlatformUser(0, "tester", "testerPass");
        db.addUser(tester);
        PlatformPost one = new PlatformPost(1, 0, "One", null);
        db.addPost(one);

        db.addPostUpvote(1, 0);
        Assert.assertEquals("Expected 1 post to be upvote.", 1, one.getUpvoteIds().size());

        db.addPostDownvote(1, 0);
        Assert.assertEquals("Expected 1 post to be down vote.", 1, one.getDownvoteIds().size());
    }

    //test all comment functions
    @Test
    public void testCommentFunctions() throws IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();
        PlatformUser tester = new PlatformUser(0, "tester", "testerPass");
        db.addUser(tester);
        PlatformPost one = new PlatformPost(0, 0, "One", null);
        db.addPost(one);

        //test content
        PlatformComment comment = new PlatformComment(0, 0, "test comment");
        db.addComment(0, comment);
        Assert.assertEquals("comment content should match", "test comment", comment.getContent());

        //test upvote and down vote comment
        comment.addUpvote(0);
        Assert.assertEquals("comment should have upvote", 1, comment.getUpvoteIds().size());

        comment.addDownvote(0);
        Assert.assertEquals("comment should have down vote", 1, comment.getDownvoteIds().size());

        one.removeComment(0);
        Assert.assertEquals("Post should have not comments", 0, one.getComments().size());
    }    //end of comment tests

    //test follower functions
    @Test
    public void testFollower() throws IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();
        PlatformUser tester1 = new PlatformUser(0, "tester", "testerPass");
        PlatformUser tester2 = new PlatformUser(1, "tester", "testerPass");
        db.addUser(tester1);
        db.addUser(tester2);

        //test adding and remove follower
        db.addFollower(0, 1);
        Assert.assertEquals("Expected user to have 1 follower", 1,
            tester1.getFollowerIds().size());

        db.removeFollower(0, 1);
        Assert.assertEquals("Expected user to have no followers", 0,
                tester1.getFollowerIds().size());

        //test follow request
        db.addFollowRequest(1, 0);
        Assert.assertEquals("tester2 should have a follow request", 1,
            tester2.getFollowRequests().size());
    }    //end of follow request

    //test blocking users
    @Test
    public void testBlocking() throws IOException, ClassNotFoundException {
        PlatformDatabase db = new PlatformDatabase();
        PlatformUser tester1 = new PlatformUser(0, "tester", "testerPass");
        PlatformUser tester2 = new PlatformUser(1, "tester", "testerPass");
        db.addUser(tester1);
        db.addUser(tester2);

        //test blocking a user
        db.addBlockedUser(0, 1);
        Assert.assertTrue("should have a blocked user", db.hasBlockedUser(0, 1));

        //removing blocked user
        db.removeBlockedUser(0, 1);
        Assert.assertFalse("shouldn't have a blocked user", db.hasBlockedUser(0, 1));
    }    //end of tests for blocking users

}    //end of Database testcase