import java.util.List;
import java.awt.Image;

/**
 * ClientHandler
 * 
 * <p>
 * Defines an interface for a client handler.
 * </p>
 * 
 * @author Ropan Datta, L22
 * @version November 16, 2024
 */
public interface ClientHandler extends Runnable {

    /**
     * Checks whether a user is currently logged in.
     * 
     * @return {@code true} if a user is logged in, {@code false} otherwise
     */
    boolean isLoggedIn();

    /**
     * Attempts to log in.
     * 
     * @param username the username to log in with
     * @param password the user's password
     * @return {@code true} if the login is successful, {@code false} if the user does not exist or
     *         if the password is incorrect
     */
    boolean logIn(String username, String password);

    /**
     * Logs out if a user is logged in.
     * 
     * @return {@code true} if the user was logged out, {@code false} if no user was logged in
     */
    boolean logOut();

    /**
     * Creates a new User.
     * 
     * @param username the username of the new user
     * @param password the password of the new user
     * @return {@code true} if the user was created successfully, {@code false} if user creation
     *         failed (e.g. if the username was already taken)
     */
    boolean createUser(String username, String password);

    /**
     * Creates a new post.
     * 
     * @param content the text content of the post
     * @param image an optional image added to the post
     * @return {@code true} if the post was created successfully, {@code false} if post creation
     *         failed
     */
    boolean createPost(String content, Image image);

    /**
     * Retrieves the IDs of the blocked users.
     * 
     * @return the list of blocked user IDs
     */
    List<Integer> getBlockedUserIds();

    /**
     * Blocks a user.
     * 
     * @param userId the ID of the user to block
     * @return {@code true} if the user was blocked successfully
     */
    boolean blockUser(int userId);

    /**
     * Unblocks a user.
     * 
     * @param userId the ID of the user to unblock
     * @return {@code true} if the user was unblocked successfully
     */
    boolean unblockUser(int userId);

    /**
     * Retrieves the follow requests sent to the user.
     * 
     * @return the list of follow requests
     */
    List<Integer> getFollowRequests();

    /**
     * Sends a follow request to another user.
     * 
     * @param userId the ID of the user to send a follow request to
     * @return {@code true} if the follow request was sent successfully, {@code false} otherwise
     */
    boolean sendFollowRequest(int userId);

    /**
     * Unsends a follow request.
     * 
     * @param userId the ID of the user to whom the follow request was sent
     * @return whether the follow request was unsent successfully
     */
    boolean cancelFollowRequest(int userId);

    /**
     * Accepts a follow request.
     * 
     * @param userId the ID of the user whose follow request to accept
     * @return whether the follow request was accepted successfully
     */
    boolean acceptFollowRequest(int userId);

    /**
     * Rejects a follow request.
     * 
     * @param userId the ID of the user whose follow request to reject
     * @return whether the follow request was rejected successfully
     */
    boolean rejectFollowRequest(int userId);

    /**
     * Unfollows a user.
     * 
     * @param userId the ID of the user to unfollow
     * @return whether the user was unfollowed successfully
     */
    boolean unfollowUser(int userId);

    /**
     * Retrieves a post.
     * 
     * @param postId the ID of the post to fetch
     * @return the post associated with the post ID
     */
    Post fetchPost(int postId);

    /**
     * Upvotes a post.
     * 
     * @param postId the ID of the post to upvote
     * @return whether the post was upvoted successfully
     */
    boolean upvotePost(int postId);

    /**
     * Downvotes a post.
     * 
     * @param postId the ID of the post to downvote
     * @return whether the post was downvoted successfully
     */
    boolean downvotePost(int postId);

    /**
     * Comments on a post.
     * 
     * @param postId the ID of the post to add the comment to
     * @param content the text content of the comment
     */
    boolean createComment(int postId, String content);

    /**
     * Searches for a user by username.
     * 
     * @param search the text to search for in the username
     * @return the list of users whose username contains the search text
     */
    List<User> searchUsername(String search);

    /**
     * Generates a default feed based on followed users.
     * 
     * @return a list of posts from followed users, sorted by their score
     */
    List<Post> loadFeed();
}
