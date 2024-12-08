import java.awt.Image;
import java.util.List;

/**
 * Provides an object storing public information about a User.
 * 
 * @author Ropan Datta, L22
 * @version December 7, 2024
 */
public class PlatformUserInfo implements UserInfo {
    private Integer id;
    private String username;
    private Image displayImage;
    private List<Integer> postIds;

    public PlatformUserInfo(User user) {
        id = user.getId();
        username = user.getUsername();
        displayImage = user.getDisplayImage();
        postIds = user.getPostIds();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Image getDisplayImage() {
        return displayImage;
    }

    public List<Integer> getPostIds() {
        return postIds;
    }
}
