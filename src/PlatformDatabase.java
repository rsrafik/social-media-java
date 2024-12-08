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
 * @version December 6, 2024
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
    private HashMap<Integer, Integer> commentMap; // Map of comment IDs to post IDs

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
    public boolean existsUser(int userId) {
        synchronized (USER_LOCK) {
            return userMap.containsKey(userId);
        }
    }

    @Override
    public User getUser(int userId) {
        synchronized (USER_LOCK) {
            return userMap.get(userId);
        }
    }

    @Override
    public User fetchUser(int userId) throws IOException, ClassNotFoundException {
        synchronized (USER_LOCK) {
            User user = userMap.get(userId);
            return (User) deepCopy(user);
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
        return posts;
    }

    @Override
    public boolean existsPost(int postId) {
        synchronized (POST_LOCK) {
            return postMap.containsKey(postId);
        }
    }

    @Override
    public Post getPost(int postId) {
        synchronized (POST_LOCK) {
            return postMap.get(postId);
        }
    }

    @Override
    public Post fetchPost(int postId) throws IOException, ClassNotFoundException {
        synchronized (POST_LOCK) {
            Post post = postMap.get(postId);
            return (Post) deepCopy(post);
        }
    }

    @Override
    public int getPosterId(int postId) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            return post.getCreatorId();
        }
    }

    @Override
    public void addPost(Post post) {
        int postId = post.getId();
        int creatorId = post.getCreatorId();
        synchronized (USER_LOCK) {
            User creator = userMap.get(creatorId);
            creator.addPost(post.getId());
        }
        synchronized (POST_LOCK) {
            posts.add(post);
            postMap.put(postId, post);
            for (Comment comment : post.getComments()) {
                commentMap.put(comment.getId(), postId);
            }
        }
    }

    @Override
    public void addPostUpvote(int postId, int userId) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            post.addUpvote(userId);
        }
    }

    @Override
    public void removePostUpvote(int postId, int userId) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            post.removeUpvote(userId);
        }
    }

    @Override
    public void addPostDownvote(int postId, int userId) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            post.addDownvote(userId);
        }
    }

    @Override
    public void removePostDownvote(int postId, int userId) {
        synchronized (POST_LOCK) {
            Post post = getPost(postId);
            post.removeDownvote(userId);
        }
    }

    // TODO: override
    public boolean existsComment(int commentId) {
        synchronized (POST_LOCK) {
            return commentMap.containsKey(commentId);
        }
    }

    // TODO: override
    public int getCommenterId(int commentId) {
        synchronized (POST_LOCK) {
            int postId = commentMap.get(commentId);
            Post post = getPost(postId);
            for (Comment comment : post.getComments()) {
                if (comment.getId() == commentId) {
                    return comment.getCreatorId();
                }
            }
        }
        throw new NullPointerException();
    }

    // TODO: override
    public int getPostIdOfComment(int commentId) {
        synchronized (POST_LOCK) {
            return commentMap.get(commentId);
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
    public void addCommentUpvote(int commentId, int userId) {
        synchronized (POST_LOCK) {
            int postId = commentMap.get(commentId);
            Post post = getPost(postId);
            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                if (comment.getId() == commentId) {
                    comment.addUpvote(userId);
                }
            }
        }
    }

    @Override
    public void addCommentDownvote(int commentId, int userId) {
        synchronized (POST_LOCK) {
            int postId = commentMap.get(commentId);
            Post post = getPost(postId);
            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                if (comment.getId() == commentId) {
                    comment.addDownvote(userId);
                }
            }
        }
    }

    @Override
    public void removeCommentUpvote(int commentId, int userId) {
        synchronized (POST_LOCK) {
            int postId = commentMap.get(commentId);
            Post post = getPost(postId);
            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                if (comment.getId() == commentId) {
                    comment.removeUpvote(userId);
                }
            }
        }
    }

    @Override
    public void removeCommentDownvote(int commentId, int userId) {
        synchronized (POST_LOCK) {
            int postId = commentMap.get(commentId);
            Post post = getPost(postId);
            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                if (comment.getId() == commentId) {
                    comment.removeDownvote(userId);
                }
            }
        }
    }

    @Override
    public void removeComment(int commentId) {
        synchronized (POST_LOCK) {
            int postId = commentMap.get(commentId);
            Post post = getPost(postId);
            post.removeComment(commentId);
            commentMap.remove(commentId);
        }
    }

    @Override
    public boolean addFollower(int userId, int followerId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            User follower = getUser(userId);
            if (!user.addFollower(followerId)) {
                return false;
            }
            follower.addFollowing(userId);
            return true;
        }
    }

    @Override
    public void removeFollower(int userId, int followerId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            User follower = getUser(followerId);
            follower.removeFollowing(userId);
            user.removeFollower(followerId);
        }
    }

    @Override
    public boolean addFollowRequest(int userId, int fromId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            return user.addFollowRequest(fromId);
        }
    }

    @Override
    public void removeFollowRequest(int userId, int fromId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            user.removeFollowRequest(fromId);
        }
    }

    @Override
    public boolean hasBlockedUser(int userId, int blockedId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            return user.hasBlockedUser(blockedId);
        }
    }

    @Override
    public void addBlockedUser(int userId, int blockedId) {
        synchronized (USER_LOCK) {
            User user = getUser(userId);
            User blocked = getUser(blockedId);
            blocked.removeFollowRequest(userId);
            blocked.removeFollowing(userId);
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

    @Override
    public List<UserInfo> searchUsername(String search) throws IOException, ClassNotFoundException {
        if (search.isEmpty()) {
            return null;
        }
        synchronized (USER_LOCK) {
            ArrayList<UserInfo> userInfos = new ArrayList<>();
            for (User user : users) {
                if (user.getUsername().contains(search)) {
                    UserInfo userInfo = new PlatformUserInfo(user);
                    userInfos.add(userInfo);
                }
            }
            return userInfos;
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

    private Object deepCopy(Object obj) throws IOException, ClassNotFoundException {
        // Serialize the object
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);) {
            out.writeObject(obj);
            // Deserialize the object
            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                    ObjectInputStream in = new ObjectInputStream(byteIn);) {
                return in.readObject();
            }
        }
    }
}
