import java.io.Serializable;

/**
 * PlatformFriendRequest
 * <p>
 * This class represents a friend request on the platform, containing the ID of the user
 * who sent the request and an optional message. The class implements the FriendRequest
 * interface and is serializable to allow for persistence.
 *
 * @author Rachel Rafik, L22
 * @version November 1, 2024
 */
public class PlatformFriendRequest implements FriendRequest, Serializable {

    // FIELDS

    private int userId;      // ID of the user who sent the friend request
    private String message;  // Optional message included with the friend request

    // CONSTRUCTORS

    /**
     * Constructs a PlatformFriendRequest with a specified user ID and message.
     *
     * @param userId  The ID of the user who sent the friend request
     * @param message The message accompanying the friend request
     */
    public PlatformFriendRequest(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    // GETTERS

    /**
     * Retrieves the ID of the user who sent the friend request.
     *
     * @return The user ID of the sender
     */
    @Override
    public int getUserId() {
        return userId;
    }

    /**
     * Retrieves the message accompanying the friend request.
     *
     * @return The message text
     */
    @Override
    public String getMessage() {
        return message;
    }
}
