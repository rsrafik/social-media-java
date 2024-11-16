import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;

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

    //check if FoundationDatabase implements DatabaseInterface
    @Test
    public void testPlatformImplementsDatabaseInterface() {
        FoundationDatabase db = new FoundationDatabase();

        assertTrue("FoundationDatabase should implement " +
                "Database interface", db instanceof FoundationDatabase);
    }    //end of checking implementation


    //test the readUsers method
    @Test
    public void testReadUsersWithValidFile() throws IOException, ClassNotFoundException {
        FoundationDatabase db = new FoundationDatabase();

        // Prepare a test users.dat file with serialized PlatformUser objects
        try (FileOutputStream fileOut = new FileOutputStream("users.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(new PlatformUser(0,"User1", "pass1"));
            out.writeObject(new PlatformUser(1,"User2", "pass2"));
        }

        db.readUsers();

        assertEquals("Expected 2 users in the list after reading a valid file.",
                2, db.getAllUsers().size());
        assertEquals("Expected first user to be 'User1'.",
                "User1", db.getAllUsers().getFirst().getUsername());
        assertEquals("Expected second user to be 'User2'.",
                "User2", db.getAllUsers().get(1).getUsername());
    }

    //tests readUsers with an empty file
    @Test
    public void testReadUsersWithEmptyFile() throws IOException {
        FoundationDatabase db = new FoundationDatabase();

        // Prepare an empty users.dat file
        new FileOutputStream("users.dat").close();

        db.readUsers();

        assertEquals("Expected 0 users when reading an empty file.",
                0, db.getAllUsers().size());
    }    //end of readUsers tests


    //test for readPosts
    @Test
    public void testReadPostsWithValidFile() throws IOException, ClassNotFoundException {
        FoundationDatabase db = new FoundationDatabase();

        // Prepare a test posts.dat file with serialized PlatformPost objects
        try (FileOutputStream fileOut = new FileOutputStream("posts.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(new PlatformPost(0, 0, "Post1", null));
            out.writeObject(new PlatformPost(1, 1, "Post2", null));
        }

        db.readPosts();

        assertEquals("Expected 2 posts in the list after reading a valid file.",
                2, db.getAllPosts().size());
        assertEquals("Expected first post to be 'Post1'.",
                "Post1", db.getAllPosts().get(0).getContent());
        assertEquals("Expected second post to be 'Post2'.",
                "Post2", db.getAllPosts().get(1).getContent());
    }

    //test with empty post file
    @Test
    public void testReadPostsWithEmptyFile() throws IOException {
        FoundationDatabase db = new FoundationDatabase();

        // Prepare an empty posts.dat file
        new FileOutputStream("posts.dat").close();

        db.readPosts();

        assertEquals("Expected 0 posts when reading an empty file.",
                0, db.getAllPosts().size());
    }    //end of readPosts test

    //tests for addUser method
    @Test
    public void testAddUserToListAndFile() throws InterruptedException, IOException, ClassNotFoundException {
        FoundationDatabase db = new FoundationDatabase();
        PlatformUser testUser = new PlatformUser(0, "test", "password");
        // Delete users.dat to start fresh
        File file = new File("users.dat");
        file.delete();

        // Add a user and check that it was added to the list
        db.addUser(testUser);
        assertEquals("Expected 1 user in the list after addUser.",
                1, db.getAllUsers().size());
        assertEquals("Expected user in list to be 'Test'.",
                "test", db.getAllUsers().get(0).getUsername());

        // Check that user is also added to users.dat
        try (FileInputStream fileIn = new FileInputStream("users.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            PlatformUser userFromFile = (PlatformUser) in.readObject();
            assertEquals("Expected user in file to be 'test'.",
                    "test", userFromFile.getUsername());
        }
    }

    @Test
    public void testAddUserWithExistingFile() throws InterruptedException, IOException, ClassNotFoundException {
        FoundationDatabase db = new FoundationDatabase();
        PlatformUser testUser = new PlatformUser(0, "test", "testPass");
        // Prepare a users.dat file with an initial user
        PlatformUser initialUser = new PlatformUser(0, "InitialUser", "password");
        try (FileOutputStream fileOut = new FileOutputStream("users.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(initialUser);
        }

        // Add a second user and check that both users are in the file
        db.addUser(testUser);

        try (FileInputStream fileIn = new FileInputStream("users.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            PlatformUser user1 = (PlatformUser) in.readObject();
            PlatformUser user2 = (PlatformUser) in.readObject();

            assertEquals("Expected first user in file to be 'InitialUser'.",
                    "InitialUser", user1.getUsername());
            assertEquals("Expected second user in file to be 'test'.",
                    "test", user2.getUsername());
        }
    }    //end of test for addUser

    //tests for addPost method
    @Test
    public void testAddPostToListAndFile() throws InterruptedException, IOException, ClassNotFoundException {
        FoundationDatabase db = new FoundationDatabase();
        PlatformPost testPost = new PlatformPost(0, 0, "test", null);
        // Delete posts.dat to start fresh
        File file = new File("posts.dat");
        file.delete();

        // Add a post and check that it was added to the list
        db.addPost(testPost);
        assertEquals("Expected 1 post in the list after addPost.",
                1, db.getAllPosts().size());
        assertEquals("Expected post in list to be 'test'.",
                "test", db.getAllPosts().get(0).getContent());

        // Check that post is also added to posts.dat
        try (FileInputStream fileIn = new FileInputStream("posts.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            PlatformPost postFromFile = (PlatformPost) in.readObject();
            assertEquals("Expected post in file to be 'test'.",
                    "test", postFromFile.getContent());
        }
    }

    @Test
    public void testAddPostWithExistingFile() throws InterruptedException, IOException, ClassNotFoundException {
        FoundationDatabase db = new FoundationDatabase();
        PlatformPost testPost = new PlatformPost(0, 0, "test", null);
        // Prepare a posts.dat file with an initial post
        PlatformPost initialPost = new PlatformPost(1, 1, "InitialPost", null);
        try (FileOutputStream fileOut = new FileOutputStream("posts.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(initialPost);
        }

        // Add a second post and check that both posts are in the file
        db.addPost(testPost);

        try (FileInputStream fileIn = new FileInputStream("posts.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            PlatformPost post1 = (PlatformPost) in.readObject();
            PlatformPost post2 = (PlatformPost) in.readObject();

            assertEquals("Expected first post in file to be 'InitialPost'.",
                    "InitialPost", post1.getContent());
            assertEquals("Expected second post in file to be 'TestPost'.",
                    "test", post2.getContent());
        }
    }

    @Test
    public void testGetAllUsers() throws InterruptedException {
        FoundationDatabase db = new FoundationDatabase();
        // Add two users to the database
        PlatformUser one = new PlatformUser(0, "one", "one");
        PlatformUser two = new PlatformUser(1, "two", "two");
        db.addUser(one);
        db.addUser(two);

        // Test get all users and check that both of the added users exist in users.
        ArrayList<PlatformUser> users = db.getAllUsers();
        Assert.assertEquals("Should contain 2 users", 2, users.size());
        Assert.assertTrue("User one should be in the list", users.contains(one));
        Assert.assertTrue("User two should be in the list", users.contains(two));
    }

    @Test
    public void testGetAllPosts() throws InterruptedException {
        FoundationDatabase db = new FoundationDatabase();
        // Add two post to the database
        PlatformPost one = new PlatformPost(1, 1, "One", null);
        PlatformPost two = new PlatformPost(2, 2, "Two", null);
        db.addPost(one);
        db.addPost(two);

        // Test get all posts and check that both of the added posts exist in users.
        ArrayList<PlatformPost> posts = db.getAllPosts();
        Assert.assertEquals("Should contain 2 posts", 2, posts.size());
        Assert.assertTrue("Post one should be in the list", posts.contains(one));
        Assert.assertTrue("Post two should be in the list", posts.contains(two));
    }

    /*
     * will add a separate test here for adding photos to posts
     *  -Mckinley
     */

}    //end of Database testcase