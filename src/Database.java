import java.io.IOException;
import java.util.List;

/**
 * Interface for FoundationDatabase, defining methods to manage users and posts in a persistent
 * storage system.
 *
 * @author Mckinley Newman, L22
 * @author Ropan Datta, L22
 * @version December 6, 2024
 */
public interface Database {

    /**
     * Reads users from a file on disk
     * 
     * @param filename the file to read from
     * @throws IOException if a file I/O error occurs
     * @throws ClassNotFoundException if there is bad data in the file
     */
    void readUsers(String filename) throws IOException, ClassNotFoundException;

    /**
     * Reads posts from a file on disk
     * 
     * @param filename the file to read from
     * @throws IOException if a file I/O error occurs
     * @throws ClassNotFoundException if there is bad data in the file
     */
    void readPosts(String filename) throws IOException, ClassNotFoundException;

    /**
     * Retrieves all users stored in the database.
     *
     * @return the list of users in the database
     */
    List<User> getUsers();

    /**
     * Checks whether a user exists.
     * 
     * @param userId the user ID to check for
     * @return whether the user ID exists in the database
     */
    boolean existsUser(int userId);

    /**
     * Retrieves the unique user associated with an integer ID.
     * 
     * @param userId the unique ID of the user
     * @return the user associated with the integer ID
     */
    User getUser(int userId);

    /**
     * Retrieves a copy of a user.
     * 
     * @param userId the ID of the user to fetch
     * @return a copy of the user
     * @throws Exception
     */
    User fetchUser(int userId) throws Exception;

    /**
     * Retrieves the unique integer ID associated with a username.
     * 
     * @param username the username to query
     * @return the unique integer ID associated witht the username or null if the user doesn't exist
     */
    Integer getUserId(String username);

    /**
     * Adds a new user to the database.
     *
     * @param user the User to add to the database
     */
    void addUser(User user);

    /**
     * Retrieves the number of users in the database.
     * 
     * @return the total number of users
     */
    int userCount();

    /**
     * Retrieves all posts stored in the database.
     *
     * @return A LinkedHashMap of PlatformPost objects with post IDs as keys.
     */
    List<Post> getPosts();

    /**
     * Checks whether a post exists.
     * 
     * @param postId the post ID to check for
     * @return whether the post ID exists in the database
     */
    boolean existsPost(int postId);

    /**
     * Retrieves the post associated with a post ID.
     * 
     * @param postId the unique ID of the post
     * @return the post associated with the integer ID
     */
    Post getPost(int postId);

    /**
     * Retrieves a copy of a user.
     * 
     * @param postId the ID of the post to fetch
     * @return a copy of the post
     */
    Post fetchPost(int postId) throws Exception;

    /**
     * Retrieves the ID of the user who created the post.
     * 
     * @param postId the ID of the post
     * @return the user ID of the post's creator
     */
    int getCreatorId(int postId);

    /**
     * Adds a new post to the database.
     *
     * @param post the PlatformPost to add to the database
     */
    void addPost(Post post);

    /**
     * Adds an upvote to a post.
     * 
     * @param postId the ID of the post to upvote
     * @param userId the ID of the user who upvoted the post
     */
    void addPostUpvote(int postId, int userId);

    /**
     * Removes an upvote from a post.
     * 
     * @param postId the ID of the post to remove the upvote from
     * @param userId the ID of the user removing their upvote
     */
    void removePostUpvote(int postId, int userId);

    /**
     * Adds a downvote to a post.
     * 
     * @param postId the ID of the post to downvote
     * @param userId the ID of the user who downvoted the post
     */
    void addPostDownvote(int postId, int userId);

    /**
     * Removes a downvote from the post.
     * 
     * @param postId the ID of the post to remove the downvote from
     * @param userId the ID of the user removing their downvote
     */
    void removePostDownvote(int postId, int userId);

    /**
     * Adds a comment to the post.
     * 
     * @param postId the ID of the post to which the comment is added
     * @param comment the comment to be added
     */
    void addComment(int postId, Comment comment);

    /**
     * Adds a follower to a user's followers.
     * 
     * @param userId the ID of the user being followed
     * @param followerId the ID of the follower
     * @return whether the follower could be added
     */
    boolean addFollower(int userId, int followerId);

    /**
     * Removes a follower from a user's followers.
     * 
     * @param userId the ID of the user being unfollowed
     * @param followerId the ID of the user unfollowing
     */
    void removeFollower(int userId, int followerId);

    /**
     * Adds a follow request to a user.
     * 
     * @param userId the ID of the user to add the follow request to
     * @param fromId the ID of the user sending the follow request
     * @return whether the follow request could be added
     */
    boolean addFollowRequest(int userId, int fromId);

    /**
     * Removes a follow request from a user.
     * 
     * @param userId the ID of the user to remove the follow request from
     * @param fromId the ID of the user who had sent the follow request
     */
    void removeFollowRequest(int userId, int fromId);

    /**
     * Checks whether a user has been blocked.
     * 
     * @param userId the ID of the user who has potentially initiated the block
     * @param blockedId the ID of the user who has potentially been blocked
     * @return whether {@code userId} has blocked {@code blockedId}
     */
    boolean hasBlockedUser(int userId, int blockedId);

    /**
     * Adds a user to the list of blocked users.
     * 
     * @param userId the ID of the user adding the block
     * @param blockedId the ID of the user being blocked
     */
    void addBlockedUser(int userId, int blockedId);

    /**
     * Removes a user from the list of blocked users.
     * 
     * @param userId the ID of the user removing the block
     * @param blockedId the ID of the user being unblocked
     */
    void removeBlockedUser(int userId, int blockedId);

    /**
     * Searches for users by username.
     * 
     * @param search the search term
     * @return the list of users containing the search term in their username.
     * @throws IOException if an error occurs while deep copying a User object
     * @throws ClassNotFoundException should not happen
     */
    List<UserInfo> searchUsername(String search) throws IOException, ClassNotFoundException;

    /**
     * Saves user information to disk.
     * 
     * @param filename the file to save to
     * @throws IOException if a file I/O error occurs
     */
    void saveUsers(String filename) throws IOException;

    /**
     * Saves post information to disk.
     * 
     * @param filename the file to save to
     * @throws IOException if a file I/O error occurs
     */
    void savePosts(String filename) throws IOException;
}
