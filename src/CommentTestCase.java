import org.junit.Test;
import org.junit.Assert;

/**
 * Test case for the Comment interface, verifying its existence and implementation
 * by the PlatformComment class.
 *
 * @author Navan Dendukuri, L22
 * @version November 3, 2024
 */
public class CommentTestCase {

    /**
     * Tests if the Comment interface exists.
     */
    @Test
    public void interfaceExists() {
        try {
            Class<?> comment = Class.forName("Comment");
            Assert.assertTrue("Comment interface should exist.", comment.isInterface());
        } catch (ClassNotFoundException e) {
            Assert.fail("Comment interface does not exist");
        }
    }

    /**
     * Tests if PlatformComment implements the Comment interface.
     */
    @Test
    public void interfaceImplemented() {
        boolean isImplemented = Comment.class.isAssignableFrom(PlatformComment.class);
        Assert.assertTrue("PlatformComment should implement Comment", isImplemented);
    }
}
