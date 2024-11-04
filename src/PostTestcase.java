import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for the Post interface, verifying its existence and implementation
 * by the PlatformPost class.
 *
 * @author Navan Dendukuri, L22
 * @version November 3, 2024
 */
public class PostTestcase {

    /**
     * Tests if the Post interface exists.
     */
    @Test
    public void interfaceExists() {
        try {
            Class<?> post = Class.forName("Post");
            Assert.assertTrue("Post interface should exist.", post.isInterface());
        } catch (ClassNotFoundException e) {
            Assert.fail("Post interface does not exist");
        }
    }

    /**
     * Tests if PlatformPost implements the Post interface.
     */
    @Test
    public void interfaceImplemented() {
        boolean isImplemented = Post.class.isAssignableFrom(PlatformPost.class);
        Assert.assertTrue("PlatformPost should implement Post", isImplemented);
    }
}
