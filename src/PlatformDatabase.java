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
public class PlatformDatabase /* TODO: implements Database */ {
    private static final Object USER_LOCK = new Object(); // Synchronization lock for users
    private static final Object POST_LOCK = new Object(); // Synchronization lock for posts

    // these two are kinda only useful for saving to file
    private ArrayList<PlatformUser> users;
    private ArrayList<PlatformPost> posts;

    private HashMap<Integer, PlatformUser> userMap;
    private LinkedHashMap<String, Integer> usernameMap;
    private LinkedHashMap<Integer, PlatformPost> postMap; // Map of posts

    public PlatformDatabase() {
        users = new ArrayList<>();
        posts = new ArrayList<>();
        userMap = new HashMap<>();
        usernameMap = new LinkedHashMap<>();
        postMap = new LinkedHashMap<>();
    }

    /**
     * Reads users from a file on disk
     * 
     * @param filename the file to read from
     * @throws IOException if a file I/O error occurs
     * @throws ClassNotFoundException if there is bad data in the file
     */
    public void readUsers(String filename) throws IOException, ClassNotFoundException {
        synchronized (USER_LOCK) {
            ArrayList<PlatformUser> userList = new ArrayList<>();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                while (true) {
                    try {
                        userList.add((PlatformUser) in.readObject());
                    } catch (EOFException e) {
                        break; // End of file reached
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw e;
            }
            // We only add the users if the entire file is read successfully.
            for (PlatformUser user : userList) {
                addUser(user);
            }
        }
    }

    /**
     * Reads posts from a file on disk
     * 
     * @param filename the file to read from
     * @throws IOException if a file I/O error occurs
     * @throws ClassNotFoundException if there is bad data in the file
     */
    public void readPosts(String filename) throws IOException, ClassNotFoundException {
        synchronized (POST_LOCK) {
            ArrayList<PlatformPost> postList = new ArrayList<>();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                while (true) {
                    try {
                        postList.add((PlatformPost) in.readObject());
                    } catch (EOFException e) {
                        break; // End of file reached
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw e;
            }
            // We only add the posts if the entire file is read successfully.
            for (PlatformPost post : postList) {
                addPost(post);
            }
        }
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The PlatformUser to add to the database
     */
    public void addUser(PlatformUser user) {
        synchronized (USER_LOCK) {
            users.add(user);
            userMap.put(user.getUserId(), user);
            usernameMap.put(user.getUsername(), user.getUserId());
        }
    }

    /**
     * Adds a new post to the database.
     *
     * @param post The PlatformPost to add to the database
     */
    public void addPost(PlatformPost post) {
        synchronized (USER_LOCK) {
            synchronized (POST_LOCK) {
                int creatorId = post.getCreatorId();
                PlatformUser creator = userMap.get(creatorId);
                creator.addPost(post.getPostId());
                posts.add(post);
                postMap.put(post.getPostId(), post);
            }
        }
    }

    /**
     * Retrieves the unique user associated with an integer id.
     * 
     * @param id the unique id of the user we
     * @return the user associated with the integer id
     */
    public PlatformUser getUser(int id) {
        return userMap.get(id);
    }

    /**
     * Retrieves the unique integer id associated with a username.
     * 
     * @param username the username to query
     * @return the unique integer id associated witht the username or null if the user doesn't exist
     */
    public Integer getUserId(String username) {
        return usernameMap.get(username);
    }

    /**
     * Retrieves all users stored in the database.
     *
     * @return A LinkedHashMap of PlatformUser objects with usernames as keys.
     */
    public ArrayList<PlatformUser> getUsers() {
        return users;
    }

    public PlatformPost getPost(int id) {
        return postMap.get(id);
    }

    /**
     * Retrieves all posts stored in the database.
     *
     * @return A LinkedHashMap of PlatformPost objects with post IDs as keys.
     */
    public ArrayList<PlatformPost> getPosts() {
        return posts;
    }

    /**
     * Saves user information to disk.
     * 
     * @param filename the file to save to
     */
    public void saveUsers(String filename) {
        synchronized (USER_LOCK) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                for (PlatformUser user : users) {
                    out.writeObject(user);
                }
                out.writeObject(users);
            } catch (IOException e) {
                // System.out.println("Error saving user data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves post information to disk.
     * 
     * @param filename the file to save to
     */
    public void savePosts(String filename) {
        synchronized (POST_LOCK) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                System.out.println("Saving data...");
                for (PlatformPost post : posts) {
                    out.writeObject(post);
                }
                out.writeObject(posts);
            } catch (IOException e) {
                // System.out.println("Error saving post data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
