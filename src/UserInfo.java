import java.awt.Image;
import java.util.List;

/**
 * Provides an interface which allows retrieving public information about a user.
 *
 * @author Ropan Datta, L22
 * @version December 7, 2024
 */
public interface UserInfo {
    /**
     * Retrieves the ID of the user.
     *
     * @return the user's ID
     */
    Integer getId();

    /**
     * Retrieves the username of the user.
     *
     * @return the user's username
     */
    String getUsername();

    /**
     * Retreives the profile picture of the user.
     *
     * @return the user's profile picture
     */
    Image getDisplayImage();

    /**
     * Retrieves the IDs of the posts made by the user.
     *
     * @return the IDs of the user's posts
     */
    List<Integer> getPostIds();
}
