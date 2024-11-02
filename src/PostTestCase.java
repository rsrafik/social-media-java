import org.junit.Assert;
import org.junit.Test;

public class PostTestCase {
   @Test
    public void interfaceExists() {
        try {
            Class<?> post = Class.forName("Post");
            Assert.assertTrue("Post interface should exist.", post.isInterface());
        } catch (ClassNotFoundException e) {
            Assert.fail("Post interface does not exist");
        }
    }

    @Test
    public void interfaceImplemented() {
        // Check if MyClass implements MyInterface using reflection
        boolean isImplemented = Post.class.isAssignableFrom(PlatformPost.class);
        // Assert that MyClass implements MyInterface
        Assert.assertTrue("PlatformPost should implement Post", isImplemented);
    }
}


