import java.awt.Image;
import java.util.List;

/**
 * UserInfo
 */
public interface UserInfo {
    Integer getId();

    String getUsername();

    Image getDisplayImage();

    List<Integer> getPostIds();
}
