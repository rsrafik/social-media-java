import java.io.*;
import java.util.*;

/**
 * FoundationDatabase
 *
 * This class manages a database of User objects and their associated Posts. It provides methods to
 * read users and posts from files, add new users and posts, and retrieve all users and posts.
 *
 * @version November 3, 2024
 */
public class PlatformDatabase implements Database {
    private LinkedHashMap<String, PlatformUser> users = new LinkedHashMap<>(); // Map of users
    private LinkedHashMap<Integer, PlatformPost> posts = new LinkedHashMap<>(); // Map of posts
    private static final String USERS_FILE = "users.dat";
    private static final String POSTS_FILE = "posts.dat";
    private static final Object USER_LOCK = new Object(); // Synchronization lock for users
    private static final Object POST_LOCK = new Object(); // Synchronization lock for posts

    /**
     * Reads users from a persistent storage (users.dat) into the database.
     */
    @SuppressWarnings("unchecked")
    public void readUsers() {
        synchronized (USER_LOCK) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
                users = (LinkedHashMap<String, PlatformUser>) in.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("No existing user data found. Starting with an empty database.");
                users = new LinkedHashMap<>();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error reading user data. Starting with an empty database.");
                users = new LinkedHashMap<>();
            }
        }
    }

    /**
     * Reads posts from a persistent storage (posts.dat) into the database.
     */
    @SuppressWarnings("unchecked")
    public void readPosts() {
        synchronized (POST_LOCK) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(POSTS_FILE))) {
                posts = (LinkedHashMap<Integer, PlatformPost>) in.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("No existing post data found. Starting with an empty database.");
                posts = new LinkedHashMap<>();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error reading post data. Starting with an empty database.");
                posts = new LinkedHashMap<>();
            }
        }
    }

    /**
     * Adds a new user to the database and saves it to persistent storage.
     *
     * @param user The PlatformUser to add to the database
     */
    public void addUser(PlatformUser user) {
        synchronized (USER_LOCK) {
            users.put(user.getUsername(), user);
            saveUsers();
        }
    }

    /**
     * Adds a new post to the database and saves it to persistent storage.
     *
     * @param post The PlatformPost to add to the database
     */
    public void addPost(PlatformPost post) {
        synchronized (POST_LOCK) {
            posts.put(post.getPostId(), post);
            savePosts();

            if (users.containsKey(post.getCreator())) {
                PlatformUser user = users.get(post.getCreator());
                if (user != null) {
                    user.getPosts().add(post);
                    saveUsers();
                } else {
                    System.out.println("User not found for creator: " + post.getCreator());
                }
            } else {
                System.out.println("Creator ID not found in users map: " + post.getCreator());
            }
        }
    }


    /**
     * Retrieves all users stored in the database.
     *
     * @return A LinkedHashMap of PlatformUser objects with usernames as keys.
     */
    public LinkedHashMap<String, PlatformUser> getAllUsers() {
        return users;
    }

    /**
     * Retrieves all posts stored in the database.
     *
     * @return A LinkedHashMap of PlatformPost objects with post IDs as keys.
     */
    public LinkedHashMap<Integer, PlatformPost> getAllPosts() {
        return posts;
    }

    /**
     * Saves the users map to the users.dat file.
     */
    public void saveUsers() {
        synchronized (USER_LOCK) {
            try (ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
                out.writeObject(users);
            } catch (IOException e) {
                System.out.println("Error saving user data: " + e.getMessage());
            }
        }
    }

    /**
     * Saves the posts map to the posts.dat file.
     */
    public void savePosts() {
        synchronized (POST_LOCK) {
            try (ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(POSTS_FILE))) {
                System.out.println("Saving data...");
                out.writeObject(posts);
            } catch (IOException e) {
                System.out.println("Error saving post data: " + e.getMessage());
            }
        }
    }
}
