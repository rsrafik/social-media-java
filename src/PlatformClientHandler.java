import java.io.*;
import java.net.Socket;
import java.awt.Image;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: reconsider friend requests

/**
 * PlatformClientHandler
 * 
 * <p>
 * This class provides a Runnable handler for each client.
 * </p>
 * 
 * @author Ropan Datta, L22
 * @version November 16, 2024
 */
public class PlatformClientHandler implements ClientHandler {
    private static final String USERS_FILE = "users.dat";
    private static final String POSTS_FILE = "posts.dat";

    private static PlatformDatabase database;
    private static AtomicInteger userCount;
    private static AtomicInteger postCount;

    private Socket socket;
    protected Integer loggedInId;
    protected User loggedInUser;

    public PlatformClientHandler(Socket socket) {
        if (database == null) {
            database = new PlatformDatabase();
            try {
                database.readUsers(USERS_FILE);
                database.readPosts(POSTS_FILE);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            userCount = new AtomicInteger(database.userCount());
            postCount = new AtomicInteger(database.getPosts().size());
        }
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client connected!");
            while (true) {
                try {
                    OperationType operation = (OperationType) inputStream.readObject();
                    switch (operation) {
                        case IS_LOGGEDIN -> {
                            boolean result = isLoggedIn();
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case LOGIN -> {
                            String username = (String) inputStream.readObject();
                            String password = (String) inputStream.readObject();
                            boolean result = logIn(username, password);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case LOGOUT -> {
                            boolean result = logOut();
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case CREATE_USER -> {
                            String username = (String) inputStream.readObject();
                            String password = (String) inputStream.readObject();
                            boolean result = createUser(username, password);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case CREATE_POST -> {
                            String content = (String) inputStream.readObject();
                            Image image = (Image) inputStream.readObject();
                            boolean result = createPost(content, image);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case GET_BLOCKED_USERS -> {
                            outputStream.writeObject(getBlockedUserIds());
                        }
                        case BLOCK_USER -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = addBlockedUser(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case UNBLOCK_USER -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = removeBlockedUser(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case GET_FRIENDREQUESTS -> {
                            outputStream.writeObject(getFriendRequests());
                            outputStream.flush();
                        }
                        case SEND_FRIENDREQUEST -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = sendFriendRequest(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case CANCEL_FRIENDREQUEST -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = cancelFriendRequest(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case FETCH_POST -> {
                            int postId = (Integer) inputStream.readObject();
                            Post post = fetchPost(postId);
                            outputStream.writeObject(post);
                            outputStream.flush();
                        }
                        case UPVOTE_POST -> {
                            int postId = (Integer) inputStream.readObject();
                            boolean result = upvotePost(postId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case DOWNVOTE_POST -> {
                            int postId = (Integer) inputStream.readObject();
                            boolean result = downvotePost(postId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case SEARCH_USER -> {
                            String search = (String) inputStream.readObject();
                            List<User> users = searchUsername(search);
                            outputStream.writeObject(users);
                            outputStream.flush();
                        }
                        case TESTING -> {
                            // System.out.println("testing");
                            // String str = (String) inputStream.readObject();
                            Object lol = inputStream.readObject();
                            System.out.println(lol);
                        }
                        default -> {
                            throw new UnsupportedOperationException(String
                                    .format("Unimplemented operation %s!", operation.toString()));
                        }
                    }
                } catch (EOFException ex) {
                    break;
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Client disconnected.");
            try {
                database.saveUsers(USERS_FILE);
                database.savePosts(POSTS_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean isLoggedIn() {
        return loggedInId != null;
    }

    @Override
    public boolean logIn(String username, String password) {
        Integer userId = database.getUserId(username);
        if (userId == null) {
            return false;
        }
        User user = database.getUser(userId);
        if (user.passwordEquals(password)) {
            loggedInId = userId;
            loggedInUser = user;
            return true;
        }
        return false;
    }

    @Override
    public boolean logOut() {
        if (loggedInId == null) {
            return false;
        }
        loggedInId = null;
        return true;
    }

    @Override
    public boolean createUser(String username, String password) {
        if (database.getUserId(username) != null) {
            return false;
        }
        int userId = userCount.getAndIncrement();
        User user = new PlatformUser(userId, username, password);
        database.addUser(user);
        return true;
    }

    @Override
    public boolean createPost(String content, Image image) {
        if (!isLoggedIn()) {
            return false;
        }
        int postId = postCount.getAndIncrement();
        Post post = new PlatformPost(postId, loggedInId, content, image);
        database.addPost(post);
        return true;
    }

    @Override
    public List<Integer> getBlockedUserIds() {
        if (!isLoggedIn()) {
            return null;
        }
        return loggedInUser.getBlockedUserIds();
    }

    @Override
    public boolean addBlockedUser(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        database.addBlockedUser(loggedInId, userId);
        return true;
    }

    @Override
    public boolean removeBlockedUser(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        database.removeBlockedUser(loggedInId, userId);
        return true;
    }

    public List<Integer> getFriendRequests() {
        if (!isLoggedIn()) {
            return null;
        }
        return loggedInUser.getFriendRequests();
    }

    @Override
    public boolean sendFriendRequest(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (userId == loggedInId) { // can't be friends with yourself unfortunately
            return false;
        }
        // TODO: make this thread-safe
        User user = database.getUser(userId);
        if (user == null || user.getBlockedUserIds().contains(loggedInId)) {
            return false;
        }
        if (user.getFriendIds().contains(loggedInId)) { // already friends with the user
            return false;
        }
        List<Integer> friendRequests = loggedInUser.getFriendRequests();
        if (friendRequests.contains(loggedInId)) {
            friendRequests.remove((Integer) loggedInId);
            user.addFriend(loggedInId);
            loggedInUser.addFriend(userId);
        } else {
            if (!user.getFriendRequests().contains(loggedInId)) {
                user.addFriendRequest(loggedInId);
            }
        }
        return true;
    }

    public boolean cancelFriendRequest(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        // TODO: change this
        User user = database.getUser(userId);
        if (user == null) {
            return false;
        }
        List<Integer> friendRequests = user.getFriendRequests();
        if (friendRequests.contains(loggedInId)) {
            database.removeFriendRequest(loggedInId, userId);
        }

        return true;
    }

    @Override
    public Post fetchPost(int postId) {
        if (!isLoggedIn()) {
            return null;
        }
        Post post = database.getPost(postId);
        User creator = database.getUser(post.getCreatorId());
        // TODO: consider if all posts are public or if posts are only visible to friends/followers
        if (creator != null && creator.hasBlockedUser(loggedInId)) {
            return null;
        }
        return post;
    }

    public boolean upvotePost(int postId) {
        Post post = database.getPost(postId);
        // TODO
        return false;
    }

    public boolean downvotePost(int postId) {
        // TODO
        return false;
    }

    public List<User> searchUsername(String search) {
        if (!isLoggedIn()) {
            return null;
        }
        List<User> users = database.searchUsername(search);
        // TODO: filter blocked users
        return users;
    }
}
