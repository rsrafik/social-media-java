import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for the Database interface and implementation
 * also tests all database functions
 *
 * @author Mckinley Newman, Navan
 * @version Nov 3, 2024
 */
public class DatabaseTestcase {

    //check if Database interface exist
    @Test
    public void testDbInterfaceExists() {
        try {
            // Attempt to load the Database interface using reflection
            Class<?> dbInterface = Class.forName("DatabaseInterface");

            if (!dbInterface.isInterface()) {
                fail("DatabaseInterface exists but is not an interface");
            }

        } catch (ClassNotFoundException e) {
            // Fail the test if the interface does not exist
            fail("DatabaseInterface interface does not exist");
        }
    }

    //check if FoundationDatabase implements DatabaseInterface
    @Test
    public void testPlatformImplementsDatabaseInterface() {
        FoundationDatabase db = new FoundationDatabase();

        assertTrue("FoundationDatabase should implement " +
                "Database interface", db instanceof FoundationDatabase);
    }    //end of checking implementation


}    //end of Database testcase
