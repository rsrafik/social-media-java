import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.io.*;
import java.util.ArrayList;

public class DatabaseTestcaseTwo {
    private FoundationDatabase db;

    @Before
    public void initialize() {
        // Initialize a new instance of FoundationDatabase
        db = new FoundationDatabase();
    }

    @After
    public void delete() {
        // Clean up any created files after each test
        File userFile = new File("users.dat");
        if (userFile.exists()) {
            userFile.delete();
        }

        File postFile = new File("posts.dat");
        if (postFile.exists()) {
            postFile.delete();
        }
    }

    @Test
    public void testGetAllUsers() throws InterruptedException {
        // Add two users to the database
        PlatformUser one = new PlatformUser("one", "one");
        PlatformUser two = new PlatformUser("two", "two");
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
        // Add two post to the database
        PlatformPost one = new PlatformPost(1,"One");
        PlatformPost two = new PlatformPost(2,"Two");
        db.addPost(one);
        db.addPost(two);

        // Test get all posts and check that both of the added posts exist in users.
        ArrayList<PlatformPost> posts = db.getAllPosts();
        Assert.assertEquals("Should contain 2 posts", 2, posts.size());
        Assert.assertTrue("Post one should be in the list", posts.contains(one));
        Assert.assertTrue("Post two should be in the list", posts.contains(two));
    }







}
