import java.io.*;
import java.net.Socket;
import java.awt.Image;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


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
    private Integer loggedInId;
    private User loggedInUser;

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
                        case GET_BLOCKED_USERS -> {
                            List<Integer> blockedUserIds = getBlockedUserIds();
                            outputStream.writeObject(blockedUserIds);
                            outputStream.flush();
                        }
                        case BLOCK_USER -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = blockUser(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case UNBLOCK_USER -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = unblockUser(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case GET_FOLLOWREQUESTS -> {
                            List<Integer> followRequests = getFollowRequests();
                            outputStream.writeObject(followRequests);
                            outputStream.flush();
                        }
                        case SEND_FOLLOWREQUEST -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = sendFollowRequest(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case CANCEL_FOLLOWREQUEST -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = cancelFollowRequest(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case ACCEPT_FOLLOWREQUEST -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = acceptFollowRequest(userId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case REJECT_FOLLOWREQUEST -> {
                            int userId = (Integer) inputStream.readObject();
                            boolean result = rejectFollowRequest(userId);
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
                        case FETCH_COMMENTS -> {
                            // TODO
                        }
                        case CREATE_COMMENT -> {
                            int postId = (Integer) inputStream.readObject();
                            String content = (String) inputStream.readObject();
                            boolean result = createComment(postId, content);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case DELETE_COMMENT -> {
                            // TODO
                        }
                        case SEARCH_USER -> {
                            String search = (String) inputStream.readObject();
                            List<User> users = searchUsername(search);
                            outputStream.writeObject(users);
                            outputStream.flush();
                        }
                        case LOAD_FEED -> {
                            List<Post> feed = loadFeed();
                            outputStream.writeObject(feed);
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
                } catch (Exception ex) {
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
        // TODO: make it thread safe
        return loggedInUser.getBlockedUserIds();
    }

    @Override
    public boolean blockUser(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (!database.existsUser(userId)) {
            return false;
        }
        database.addBlockedUser(loggedInId, userId);
        return true;
    }

    @Override
    public boolean unblockUser(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        database.removeBlockedUser(loggedInId, userId);
        return true;
    }

    @Override
    public List<Integer> getFollowRequests() {
        if (!isLoggedIn()) {
            return null;
        }
        return loggedInUser.getFollowRequests();
    }

    @Override
    public boolean sendFollowRequest(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (userId == loggedInId) { // can't be friends with yourself unfortunately
            return false;
        }
        if (!database.existsUser(userId)) {
            return false;
        }
        return database.addFollowRequest(userId, loggedInId);
    }

    @Override
    public boolean cancelFollowRequest(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (!database.existsUser(userId)) {
            return false;
        }
        database.removeFollowRequest(userId, loggedInId);
        return true;
    }

    @Override
    public boolean acceptFollowRequest(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (!database.existsUser(userId)) {
            return false;
        }
        try {
            User user = database.fetchUser(userId);
            if (!user.getFollowRequests().contains(userId)) {
                return false;
            }
            database.removeFollowRequest(loggedInId, userId);
            return database.addFollower(loggedInId, userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rejectFollowRequest(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (!database.existsUser(userId)) {
            return false;
        }
        database.removeFollowRequest(loggedInId, userId);
        return true;
    }

    @Override
    public Post fetchPost(int postId) {
        if (!isLoggedIn()) {
            return null;
        }
        if (!database.existsPost(postId)) {
            return null;
        }
        try {
            Post post = database.fetchPost(postId);
            int creatorId = post.getCreatorId();
            if (database.hasBlockedUser(creatorId, loggedInId)) {
                return null;
            }
            return post;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean upvotePost(int postId) {
        if (!isLoggedIn()) {
            return false;
        }
        Post post = database.getPost(postId);
        int creatorId = post.getCreatorId();
        if (database.hasBlockedUser(creatorId, loggedInId)) {
            return false;
        }
        database.addPostUpvote(postId, loggedInId);
        return true;
    }

    @Override
    public boolean downvotePost(int postId) {
        if (!isLoggedIn()) {
            return false;
        }
        database.addPostDownvote(postId, loggedInId);
        return true;
    }

    @Override
    public boolean createComment(int postId, String content) {
        if (!isLoggedIn()) {
            return false;
        }
        Post post = database.getPost(postId);
        User postCreator = database.getUser(post.getCreatorId());
        if (postCreator.hasBlockedUser(loggedInId)) {
            return false;
        }
        Comment comment = new PlatformComment(loggedInId, content);
        database.addComment(postId, comment);
        return true;
    }

    // TODO
    public boolean deleteComment(int commentId) {
        return false;
    }

    @Override
    public List<User> searchUsername(String search) {
        if (!isLoggedIn()) {
            return null;
        }
        try {
            List<User> users = database.searchUsername(search);
            users.removeIf(user -> user.hasBlockedUser(loggedInId));
            return users;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Post> loadFeed() {
        if (!isLoggedIn()) {
            return null;
        }
        ArrayList<Post> feed = new ArrayList<>();
        try {
            User user = database.fetchUser(loggedInId);
            for (int followingId : user.getFollowingIds()) {
                User following = database.fetchUser(followingId);
                for (int postId : following.getPostIds()) {
                    Post post = database.fetchPost(postId);
                    feed.add(post);
                }
            }
            feed.sort(Comparator.comparing(Post::calculateScore).reversed());
            return feed;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return feed;
    }
}
