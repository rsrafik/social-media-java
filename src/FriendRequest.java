/**
 * FriendRequest
 * <p>
 * This interface defines the structure for a friend request on a platform,
 * including methods for retrieving the user ID of the sender and the message
 * associated with the request.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 1, 2024
 */
public interface FriendRequest {

    /**
     * Retrieves the ID of the user who sent the friend request.
     *
     * @return The user ID of the sender
     */
    int getUserId();

    /**
     * Retrieves the message associated with the friend request.
     *
     * @return The message associated with the friend request
     */
    String getMessage();
}
