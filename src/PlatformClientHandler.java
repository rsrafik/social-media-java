import java.io.*;
import java.net.Socket;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PlatformClientHandler
 *
 * <p>
 * This class provides a Runnable handler for each client.
 * </p>
 *
 * @version November 17, 2024
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
                        case CREATE_POST -> {
                            try {
                                System.out.println("CREATE_POST operation received.");
                                String content = (String) inputStream.readObject();
                                Image image = (Image) inputStream.readObject();
                                System.out.println("Post content: " + content);
                                boolean result = createPost(content, image);
                                System.out.println("Post creation result: " + result);
                                outputStream.writeObject(result);
                                outputStream.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                                outputStream.writeObject(false);
                                outputStream.flush();
                            }
                        }
                        case SEND_FRIENDREQUEST -> {
                            int toId = (Integer) inputStream.readObject();
                            boolean result = sendFriendRequest(toId);
                            outputStream.writeObject(result);
                            outputStream.flush();
                        }
                        case CHECK_USERNAME -> {
                            String usernameToCheck = (String) inputStream.readObject();
                            boolean isAvailable = (database.getUserId(usernameToCheck) == null);
                            outputStream.writeObject(isAvailable);
                            outputStream.flush();
                        }
                        case GET_ALL_POSTS -> {
                            // Get all posts from the internal data structure
                            LinkedHashMap<Integer, Post> posts = new LinkedHashMap<>();
                            for (Post post : database.getPosts()) {
                                posts.put(post.getId(), (PlatformPost) post);
                            }
                            outputStream.writeObject(posts);
                            outputStream.flush();
                        }
                        case GET_USER_POSTS -> {
                            String username = (String) inputStream.readObject();
                            Integer userId = database.getUserId(username);
                            List<Post> userPosts = new ArrayList<>();
                            if (userId != null) {
                                User user = database.getUser(userId);
                                for (Integer postId : user.getPostIds()) {
                                    Post post = database.getPost(postId);
                                    if (post != null) {
                                        userPosts.add(post);
                                    }
                                }
                            }
                            outputStream.writeObject(userPosts);
                            outputStream.flush();
                        }
                        case GET_FRIENDS_LIST -> {
                            String username = (String) inputStream.readObject();
                            Integer userId = database.getUserId(username);
                            List<String> friendsList = new ArrayList<>();
                            if (userId != null) {
                                User user = database.getUser(userId);
                                for (Integer friendId : user.getFriendIds()) {
                                    User friend = database.getUser(friendId);
                                    if (friend != null) {
                                        friendsList.add(friend.getUsername());
                                    }
                                }
                            }
                            outputStream.writeObject(friendsList);
                            outputStream.flush();
                        }
                        case GET_FRIEND_REQUESTS -> {
                            String username = (String) inputStream.readObject();
                            Integer userId = database.getUserId(username);
                            List<String> friendRequests = new ArrayList<>();
                            if (userId != null) {
                                User user = database.getUser(userId);
                                for (Integer requestId : user.getFriendRequests()) {
                                    User requester = database.getUser(requestId);
                                    if (requester != null) {
                                        friendRequests.add(requester.getUsername());
                                    }
                                }
                            }
                            outputStream.writeObject(friendRequests);
                            outputStream.flush();
                        }
                        case GET_USER -> {
                            String username = (String) inputStream.readObject();
                            Integer userId = database.getUserId(username);
                            PlatformUser user = null;
                            if (userId != null) {
                                user = (PlatformUser) database.getUser(userId);
                            }
                            outputStream.writeObject(user);
                            outputStream.flush();
                        }

                        default -> {
                            throw new UnsupportedOperationException(String
                                    .format("Unimplemented operation %s!", operation.toString()));
                        }
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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
        System.out.println("CREATED USER");
        if (database.getUserId(username) != null) {
            return false;
        }

        int userId = userCount.getAndIncrement();
        System.out.println("new id is " + userId);
        User user = new PlatformUser(userId, username, password);
        database.addUser(user);
        return true;
    }

    @Override
    public boolean createPost(String content, Image image) {
        try {
            if (!isLoggedIn()) {
                System.out.println("User is not logged in.");
                return false;
            }
            int postId = postCount.getAndIncrement();
            System.out.println("Creating post with ID: " + postId);
            Post post = new PlatformPost(postId, loggedInId, content, image);
            System.out.println("Post created: " + post);
            database.addPost(post);
            System.out.println("Post added to database.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean sendFriendRequest(int userId) {
        if (!isLoggedIn()) {
            return false;
        }
        if (userId == loggedInId) { // can't be friends with yourself unfortunately
            return false;
        }
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
}
