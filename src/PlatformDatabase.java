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
    private final Object USER_LOCK = new Object(); // Synchronization lock for users
    private final Object POST_LOCK = new Object(); // Synchronization lock for posts

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
        synchronized (USER_LOCK) {
            return users;
        }
    }

    @Override
    public User getUser(int userId) {
        synchronized (USER_LOCK) {
            return userMap.get(userId);
        }
    }

    @Override
    public Integer getUserId(String username) {
        synchronized (USER_LOCK) {
            return usernameMap.get(username);
        }
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
        synchronized (USER_LOCK) {
            return users.size();
        }
    }

    @Override
    public List<Post> getPosts() {
        synchronized (POST_LOCK) {
            return posts;
        }
    }

    @Override
    public Post getPost(int postId) {
        synchronized (POST_LOCK) {
            return postMap.get(postId);
        }
    }

    @Override
    public void addPost(Post post) {
        int creatorId = post.getCreatorId();
        synchronized (USER_LOCK) {
            User creator = userMap.get(creatorId);
            creator.addPost(post.getId());
        }
        synchronized (POST_LOCK) {
            posts.add(post);
            postMap.put(post.getId(), post);
        }
    }

    @Override
    public void addUpvotePost(int postId, int userId) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            post.addUpvote(userId);
        }
    }

    @Override
    public void addDownvotePost(int postId, int userId) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            post.addDownvote(userId);
        }
    }

    @Override
    public void addComment(int postId, Comment comment) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            post.addComment(comment);
        }
    }

    @Override
    public void addBlockedUser(int userId, int blockedId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            user.addBlockedUser(blockedId);
        }
    }

    @Override
    public void removeBlockedUser(int userId, int blockedId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            user.removeBlockedUser(blockedId);
        }
    }

    // TODO
    public void addFriendRequest(int userId, int toId) {
        synchronized (USER_LOCK) {
            User to = getUser(toId);
            to.addFriendRequest(userId);
        }
    }

    // TODO
    public void removeFriendRequest(int userId, int fromId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            user.removeFriendRequest(fromId);
        }
    }

    @Override
    public List<User> searchUsername(String search) {
        if (search.isEmpty()) {
            return null;
        }
        synchronized (USER_LOCK) {
            ArrayList<User> searchedUsers = new ArrayList<>();
            for (User user : users) {
                if (user.getUsername().contains(search)) {
                    searchedUsers.add(user);
                }
            }
            return searchedUsers;
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
