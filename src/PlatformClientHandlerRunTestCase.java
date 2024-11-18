import org.junit.Test;
import java.io.*;
import java.net.Socket;
import static org.junit.Assert.*;

public class PlatformClientHandlerRunTestCase {
    private PlatformClientHandler handler;
    @Test
    public void testRun() throws IOException, ClassNotFoundException {
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

        public MockSocket(InputStream inputStream, OutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }
    }
}