import java.awt.Image;
import java.io.Serializable;
import java.util.List;

/**
 * Provides a class storing public information about a User.
 * 
 * @author Ropan Datta, L22
 * @version December 7, 2024
 */
public class PlatformUserInfo implements UserInfo, Serializable {
    private Integer id;
    private String username;
    private Image displayImage;
    private List<Integer> postIds;

    /**
     * Constructs an object which stores only public information about a user.
     */
    public PlatformUserInfo(User user) {
        id = user.getId();
        username = user.getUsername();
        displayImage = user.getDisplayImage();
        postIds = user.getPostIds();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Image getDisplayImage() {
        return displayImage;
    }

    @Override
    public List<Integer> getPostIds() {
        return postIds;
    }
}
