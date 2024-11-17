import java.io.*;
import java.util.*;

/**
 * PlatformDatabase
 *
 * This class manages a database of User objects and their associated Posts. It provides methods to
 * read users and posts from files, add new users and posts, and retrieve all users and posts.
 *
 * @author Rachel Rafik, L22
 * @author Ropan Datta, L22
 * @version November 15, 2024
 */
public class PlatformDatabase implements Database {
    private static final Object USER_LOCK = new Object(); // Synchronization lock for users
    private static final Object POST_LOCK = new Object(); // Synchronization lock for posts

    // these two are kinda only useful for saving to file
    private ArrayList<User> users;
    private ArrayList<Post> posts;

    private HashMap<Integer, User> userMap; // Map of user IDs to users
    private LinkedHashMap<String, Integer> usernameMap; // Map of usernames to user IDs
    private LinkedHashMap<Integer, Post> postMap; // Map of post IDs to posts

    /**
     * Constructs a new PlatformDatabase and initializes all collections to be empty.
     */
    public PlatformDatabase() {
        users = new ArrayList<>();
        posts = new ArrayList<>();
        userMap = new HashMap<>();
        usernameMap = new LinkedHashMap<>();
        postMap = new LinkedHashMap<>();
    }

    @Override
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

    @Override
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

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUser(int userId) {
        return userMap.get(userId);
    }

    @Override
    public Integer getUserId(String username) {
        return usernameMap.get(username);
    }

    @Override
    public void addUser(User user) {
        synchronized (USER_LOCK) {
            users.add(user);
            userMap.put(user.getId(), user);
            usernameMap.put(user.getUsername(), user.getId());
        }
    }

    @Override
    public int userCount() {
        return users.size();
    }

    @Override
    public List<Post> getPosts() {
        return posts;
    }

    @Override
    public Post getPost(int postId) {
        return postMap.get(postId);
    }

    @Override
    public void addPost(Post post) {
        synchronized (USER_LOCK) {
            synchronized (POST_LOCK) {
                int creatorId = post.getCreatorId();
                User creator = userMap.get(creatorId);
                creator.addPost(post.getId());
                posts.add(post);
                postMap.put(post.getId(), post);
            }
        }
    }

    @Override
    public void saveUsers(String filename) throws IOException {
        synchronized (USER_LOCK) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                for (User user : users) {
                    out.writeObject(user);
                }
            }
        }
    }

    @Override
    public void savePosts(String filename) throws IOException {
        synchronized (POST_LOCK) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                for (Post post : posts) {
                    out.writeObject(post);
                }
            }
        }
    }
}
