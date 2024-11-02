/**
 * FriendRequest
 * 
 * The FriendRequest interface
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface FriendRequest {
    /**
     * Retrieves the id of the user who sent the request.
     * 
     * @return the id of the user who sent the friend request
     */
    int getUserId();

    /**
     * Retrieves the message associated with the request.
     * 
     * @return the message associated with the friend request
     */
    String getMessage();
}
