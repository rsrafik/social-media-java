import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Image;

/**
 * A client to communicate with the PlatformServer.
 * 
 * @author Ropan Datta, L22
 * @version December 7, 2024
 */
public class PlatformClient implements Closeable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public PlatformClient(String host, int port) throws IOException, UnknownHostException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
    }

    public boolean isLoggedIn() throws IOException {
        out.writeObject(OperationType.IS_LOGGEDIN);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public boolean logIn(String username, String password) throws IOException {
        out.writeObject(OperationType.LOGIN);
        out.writeUTF(username);
        out.writeUTF(password);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public boolean logOut() throws IOException {
        out.writeObject(OperationType.LOGOUT);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public boolean createUser(String username, String password) throws IOException {
        out.writeObject(OperationType.CREATE_USER);
        out.writeUTF(username);
        out.writeUTF(password);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public User fetchLoggedInUser() throws IOException, ClassNotFoundException {
        out.writeObject(OperationType.FETCH_LOGGEDIN_USER);
        out.flush();
        User user = (User) in.readObject();
        return user;
    }

    public UserInfo fetchUserInfo(int userId) throws IOException, ClassNotFoundException {
        out.writeObject(OperationType.FETCH_USER_INFO);
        out.writeInt(userId);
        out.flush();
        UserInfo userInfo = (UserInfo) in.readObject();
        return userInfo;
    }

    public List<Integer> getBlockedUserIds() throws ClassNotFoundException, IOException {
        out.writeObject(OperationType.GET_BLOCKED_USERS);
        out.flush();
        @SuppressWarnings("unchecked")
        List<Integer> blockedUserIds = (List<Integer>) in.readObject();
        return blockedUserIds;
    }

    public boolean blockUser(int userId) throws IOException {
        out.writeObject(OperationType.BLOCK_USER);
        out.writeInt(userId);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public boolean unblockUser(int userId) throws IOException {
        out.writeObject(OperationType.UNBLOCK_USER);
        out.writeInt(userId);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public List<Integer> getFollowRequests() throws IOException, ClassNotFoundException {
        out.writeObject(OperationType.GET_FOLLOWREQUESTS);
        out.flush();
        @SuppressWarnings("unchecked")
        List<Integer> followRequests = (List<Integer>) in.readObject();
        return followRequests;
    }

    public boolean sendFollowRequest(int userId) throws IOException {
        out.writeObject(OperationType.SEND_FOLLOWREQUEST);
        out.writeInt(userId);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public boolean cancelFollowRequest(int userId) throws IOException {
        out.writeObject(OperationType.CANCEL_FOLLOWREQUEST);
        out.writeInt(userId);
        boolean result = in.readBoolean();
        return result;
    }

    public boolean acceptFollowRequest(int userId) throws IOException {
        out.writeObject(OperationType.ACCEPT_FOLLOWREQUEST);
        out.writeInt(userId);
        boolean result = in.readBoolean();
        return result;
    }

    public boolean rejectFollowRequest(int userId) throws IOException {
        out.writeObject(OperationType.REJECT_FOLLOWREQUEST);
        out.writeInt(userId);
        boolean result = in.readBoolean();
        return result;
    }

    public boolean unfollowUser(int userId) throws IOException {
        out.writeObject(OperationType.UNFOLLOW_USER);
        out.writeInt(userId);
        boolean result = in.readBoolean();
        return result;
    }

    public boolean createPost(String content, Image image) throws IOException {
        out.writeObject(OperationType.CREATE_POST);
        out.writeObject(content);
        out.writeObject(image);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public Post fetchPost(int postId) throws IOException, ClassNotFoundException {
        out.writeObject(OperationType.FETCH_POST);
        out.writeInt(postId);
        out.flush();
        Post post = (Post) in.readObject();
        return post;
    }

    public boolean upvotePost(int postId) throws IOException {
        out.writeObject(OperationType.UPVOTE_POST);
        out.writeInt(postId);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public boolean downvotePost(int postId) throws IOException {
        out.writeObject(OperationType.DOWNVOTE_POST);
        out.writeInt(postId);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public List<Comment> fetchComments(int postId) throws IOException, ClassNotFoundException {
        out.writeObject(OperationType.FETCH_COMMENTS);
        out.flush();
        @SuppressWarnings("unchecked")
        List<Comment> comments = (List<Comment>) in.readObject();
        return comments;
    }

    public boolean createComment(int postId, String content) throws IOException {
        out.writeObject(OperationType.CREATE_COMMENT);
        out.writeInt(postId);
        out.writeObject(content);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public boolean deleteComment(int commentId) throws IOException {
        out.writeObject(OperationType.DELETE_COMMENT);
        out.flush();
        boolean result = in.readBoolean();
        return result;
    }

    public List<User> searchUser(String search) throws IOException, ClassNotFoundException {
        out.writeObject(OperationType.SEARCH_USER);
        out.writeUTF(search);
        out.flush();
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) in.readObject();
        return users;
    }

    public List<Post> loadFeed() throws IOException, ClassNotFoundException {
        out.writeObject(OperationType.LOAD_FEED);
        out.flush();
        @SuppressWarnings("unchecked")
        List<Post> feed = (List<Post>) in.readObject();
        return feed;
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
