import org.junit.Test;
import java.io.*;
import java.net.Socket;
import static org.junit.Assert.*;
/**
 * Test case for the run methods in the
 * PlatformClientHandler class using input
 * output streams and mock socket to simulate
 * client and server communication.
 *
 * @author Navan Dendukuri, L22
 * @version November 17, 2024
 */
public class PlatformClientHandlerRunTestCase {

    // Tests if the run method from PlatformClientHandler works
    @Test
    public void testRun() throws IOException {
        // Create piped streams to test client-server
        PipedOutputStream clientOutput = new PipedOutputStream();
        PipedInputStream serverInput = new PipedInputStream(clientOutput);
        PipedOutputStream serverOutput = new PipedOutputStream();
        PipedInputStream clientInput = new PipedInputStream(serverOutput);
        Socket socket = new MockSocket(serverInput, serverOutput);
        PlatformClientHandler handler = new PlatformClientHandler(socket);
        Thread clientThread = new Thread(() -> {
            try (ObjectOutputStream out = new ObjectOutputStream(clientOutput);
                 ObjectInputStream in = new ObjectInputStream(clientInput)) {
                // Send a login request
                out.writeObject(OperationType.LOGIN);
                out.writeObject("testUser");
                out.writeObject("testPass");
                // Receive the login response
                boolean loginResult = (boolean) in.readObject();
                assertTrue("Login should succeed for valid credentials", loginResult);
                // Send a logout request
                out.writeObject(OperationType.LOGOUT);
                // Receive the logout response
                boolean logoutResult = (boolean) in.readObject();
                assertTrue("Logout should succeed after login", logoutResult);
            } catch (IOException | ClassNotFoundException e) {
                fail("Client testcases failed: " + e.getMessage());
            }
        });
        Thread serverThread = new Thread(handler);
        clientThread.start();
        serverThread.start();
        try {
            clientThread.join();
            serverThread.join();
        } catch (InterruptedException e) {
            fail("Threads interrupted: " + e.getMessage());
        }
    }

    // MockSocket class to simulate a socket with input/output streams
    private static class MockSocket extends Socket {
        private final InputStream inputStream;
        private final OutputStream outputStream;

        // Constructor to initialize the mock socket with input and output streams
        public MockSocket(InputStream inputStream, OutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        // Returns the input stream for the mock socket
        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        // Returns the output stream for the mock socket
        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }
    }
}