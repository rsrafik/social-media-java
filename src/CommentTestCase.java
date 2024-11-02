import org.junit.Test;
import org.junit.Assert;


import static org.junit.Assert.*;

public class CommentTestCase {
    @Test
    public void interfaceExists() {
        try {
            Class<?> comment = Class.forName("Comment");
            Assert.assertTrue("Comment interface should exist.", comment.isInterface());
        } catch (ClassNotFoundException e) {
            Assert.fail("Comment interface does not exist");
        }
    }

    @Test
    public void interfaceImplemented() {
        // Check if MyClass implements MyInterface using reflection
        boolean isImplemented = Comment.class.isAssignableFrom(PlatformComment.class);
        // Assert that MyClass implements MyInterface
        Assert.assertTrue("PlatformComment should implement Comment", isImplemented);
    }
}



