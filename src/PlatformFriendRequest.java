import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * PlatformFriendRequest
 * <p>
 * This class represents a friend request on the platform, containing the ID of the user who sent
 * the request and an optional message. The class implements the FriendRequest interface and is
 * serializable to allow for persistence.
 *
 * @author Rachel Rafik, L22
 * @version November 1, 2024
 */
public class PlatformFriendRequest implements FriendRequest, Serializable {

    private int fromId;

    // FIELDS
    private String user; // ID of the user who sent the friend request
    private String message; // Optional message included with the friend request

    // CONSTRUCTORS

    /**
     * Constructs a PlatformFriendRequest with a specified user ID and message.
     *
     * @param user The ID of the user who sent the friend request
     * @param message The message accompanying the friend request
     */
    public PlatformFriendRequest(String user, String message) {
        this.user = user;
        this.message = message;
    }

    // GETTERS

    /**
     * Retrieves the ID of the user who sent the friend request.
     *
     * @return The user ID of the sender
     */
    public String getUser() {
        return user;
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

    public static boolean sendFriendRequest(String to, String from, String message,
            FoundationDatabase fd) {
        LinkedHashMap<String, PlatformUser> users = fd.getAllUsers();

        if (!users.containsKey(to)) {
            System.out.println("User '" + to + "' not found.");
            return false;
        }

        PlatformUser recipient = users.get(to);
        PlatformFriendRequest friendRequest = new PlatformFriendRequest(from, message);

        recipient.getFriendRequests().add(friendRequest); // Add friend request to recipient's list
        fd.saveUsers(); // Save the updated users list to persistent storage

        System.out.println("Friend request sent from '" + from + "' to '" + to + "'.");
        return true;
    }

    public PlatformUser accept(FoundationDatabase fd) {
        LinkedHashMap<String, PlatformUser> users = fd.getAllUsers();

        // Retrieve and return the sender's PlatformUser object
        if (users.containsKey(user)) {
            fd.saveUsers();
            return users.get(user);
        } else {
            System.out.println("User '" + user + "' not found in the database.");
            return null;
        }


    }
}
