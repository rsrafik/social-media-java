import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for the UserInfo interface, verifying its existence and implementation
 * by the PlatformUserInfo class.
 *
 * @author Navan Dendukuri, L22
 * @version Dec 8, 2024
 */
public class UserInfoTestCase {

    /**
     * Tests if the UserInfo interface exists.
     */
    @Test
    public void interfaceExists() {
        try {
            Class<?> userInfo = Class.forName("UserInfo");
            Assert.assertTrue("UserInfo interface should exist.", userInfo.isInterface());
        } catch (ClassNotFoundException e) {
            Assert.fail("UserInfo interface does not exist");
        }
    }

    /**
     * Tests if PlatformUserInfo implements the UserInfo interface.
     */
    @Test
    public void interfaceImplemented() {
        boolean isImplemented = UserInfo.class.isAssignableFrom(PlatformUserInfo.class);
        Assert.assertTrue("PlatformUserInfo should implement UserInfo", isImplemented);
    }
}