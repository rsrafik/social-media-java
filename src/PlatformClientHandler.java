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
    private static AtomicInteger commentCount;

    private Socket socket;
    private Integer loggedInId;

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
            postCount = new AtomicInteger(database.postCount());
            commentCount = new AtomicInteger(database.commentCount());
        }
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client connected!");
            while (true) {
                try {
                    OperationType operation = (OperationType) in.readObject();
                    switch (operation) {
                        case IS_LOGGEDIN -> {
                            boolean result = isLoggedIn();
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case LOGIN -> {
                            String username = in.readUTF();
                            String password = in.readUTF();
                            boolean result = logIn(username, password);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case LOGOUT -> {
                            boolean result = logOut();
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case CREATE_USER -> {
                            String username = in.readUTF();
                            String password = in.readUTF();
                            boolean result = createUser(username, password);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case FETCH_LOGGEDIN_USER -> {
                            User user = fetchLoggedInUser();
                            out.writeObject(user);
                            out.flush();
                        }
                        case FETCH_USER_INFO -> {
                            int userId = in.readInt();
                            UserInfo info = fetchUserInfo(userId);
                            out.writeObject(info);
                            out.flush();
                        }
                        case GET_BLOCKED_USERS -> {
                            List<Integer> blockedUserIds = getBlockedUserIds();
                            out.writeObject(blockedUserIds);
                            out.flush();
                        }
                        case BLOCK_USER -> {
                            int userId = in.readInt();
                            boolean result = blockUser(userId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case UNBLOCK_USER -> {
                            int userId = in.readInt();
                            boolean result = unblockUser(userId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case GET_FOLLOWREQUESTS -> {
                            List<Integer> followRequests = getFollowRequests();
                            out.writeObject(followRequests);
                            out.flush();
                        }
                        case SEND_FOLLOWREQUEST -> {
                            int userId = in.readInt();
                            boolean result = sendFollowRequest(userId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case CANCEL_FOLLOWREQUEST -> {
                            int userId = in.readInt();
                            boolean result = cancelFollowRequest(userId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case ACCEPT_FOLLOWREQUEST -> {
                            int userId = in.readInt();
                            boolean result = acceptFollowRequest(userId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case REJECT_FOLLOWREQUEST -> {
                            int userId = in.readInt();
                            boolean result = rejectFollowRequest(userId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case UNFOLLOW_USER -> {
                            int userId = in.readInt();
                            boolean result = unfollowUser(userId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case CREATE_POST -> {
                            String content = (String) in.readObject();
                            Image image = (Image) in.readObject();
                            boolean result = createPost(content, image);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case FETCH_POST -> {
                            int postId = in.readInt();
                            Post post = fetchPost(postId);
                            out.writeObject(post);
                            out.flush();
                        }
                        case UPVOTE_POST -> {
                            int postId = in.readInt();
                            boolean result = upvotePost(postId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case DOWNVOTE_POST -> {
                            int postId = in.readInt();
                            boolean result = downvotePost(postId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case FETCH_COMMENTS -> {
                            int postId = in.readInt();
                            List<Comment> comments = fetchComments(postId);
                            out.writeObject(comments);
                            out.flush();
                        }
                        case CREATE_COMMENT -> {
                            int postId = in.readInt();
                            String content = (String) in.readObject();
                            boolean result = createComment(postId, content);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case DELETE_COMMENT -> {
                            int commentId = in.readInt();
                            boolean result = deleteComment(commentId);
                            out.writeBoolean(result);
                            out.flush();
                        }
                        case SEARCH_USER -> {
                            String search = in.readUTF();
                            List<UserInfo> users = searchUsername(search);
                            out.writeObject(users);
                            out.flush();
                        }
                        case LOAD_FEED -> {
                            List<Post> feed = loadFeed();
                            out.writeObject(feed);
                            out.flush();
                        }
                        case TESTING -> {
                            // testing
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
        } catch (Exception ex) {
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
        if (!username.matches("[A-Za-z0-9]+")) {
            return false;
        }
        if (database.getUserId(username) != null) {
            return false;
        }
        int userId = userCount.getAndIncrement();
        User user = new PlatformUser(userId, username, password);
        database.addUser(user);
        return true;
    }

    @Override
    public User fetchLoggedInUser() {
        if (!isLoggedIn()) {
            return null;
        }
        try {
            User user = database.fetchUser(loggedInId);
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public UserInfo fetchUserInfo(int userId) {
        if (!isLoggedIn()) {
            return null;
        }
        if (database.hasBlockedUser(userId, loggedInId)) {
            return null;
        }
        try {
            UserInfo userInfo = new PlatformUserInfo(database.fetchUser(userId));
            return userInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
        try {
            User loggedInUser = database.fetchUser(loggedInId);
            return loggedInUser.getBlockedUserIds();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
        try {
            User loggedInUser = database.fetchUser(loggedInId);
            return loggedInUser.getFollowRequests();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
            User loggedInUser = database.fetchUser(loggedInId);
            if (!loggedInUser.getFollowRequests().contains(userId)) {
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
    public boolean unfollowUser(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (!database.existsUser(userId)) {
            return false;
        }
        database.removeFollower(userId, loggedInId);
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
            int creatorId = database.getPosterId(postId);
            if (database.hasBlockedUser(creatorId, loggedInId)) {
                return null;
            }
            Post post = database.fetchPost(postId);
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
        if (!database.existsPost(postId)) {
            return false;
        }
        int creatorId = database.getPosterId(postId);
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
        if (!database.existsPost(postId)) {
            return false;
        }
        int creatorId = database.getPosterId(postId);
        if (database.hasBlockedUser(creatorId, loggedInId)) {
            return false;
        }
        database.addPostDownvote(postId, loggedInId);
        return true;
    }

    @Override
    public List<Comment> fetchComments(int postId) {
        if (!isLoggedIn()) {
            return null;
        }
        if (!database.existsPost(postId)) {
            return null;
        }
        int creatorId = database.getPosterId(postId);
        if (database.hasBlockedUser(creatorId, loggedInId)) {
            return null;
        }
        try {
            Post post = database.fetchPost(postId);
            List<Comment> comments = post.getComments();
            comments.removeIf(
                    comment -> database.hasBlockedUser(comment.getCreatorId(), loggedInId));
            return comments;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean createComment(int postId, String content) {
        if (!isLoggedIn()) {
            return false;
        }
        if (!database.existsPost(postId)) {
            return false;
        }
        int creatorId = database.getPosterId(postId);
        if (database.hasBlockedUser(creatorId, loggedInId)) {
            return false;
        }
        int commentId = commentCount.getAndIncrement();
        Comment comment = new PlatformComment(commentId, loggedInId, content);
        database.addComment(postId, comment);
        return true;
    }

    @Override
    public boolean deleteComment(int commentId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (!database.existsComment(commentId)) {
            return false;
        }
        int postId = database.getPostIdOfComment(commentId);
        int commenterId = database.getCommenterId(commentId);
        int creatorId = database.getPosterId(postId);
        if (loggedInId.intValue() == commenterId || loggedInId.intValue() == creatorId) {
            database.removeComment(commentId);
        }
        return true;
    }

    @Override
    public List<UserInfo> searchUsername(String search) {
        if (!isLoggedIn()) {
            return null;
        }
        try {
            List<UserInfo> userInfos = database.searchUsername(search);
            userInfos.removeIf(userInfo -> database.hasBlockedUser(userInfo.getId(), loggedInId));
            return userInfos;
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
