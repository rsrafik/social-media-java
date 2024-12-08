import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * PlatformServer
 * <p>
 * Entry point for the server.
 * <p>
 * Uses multi-threading to instantiate several client handlers to support multiple clients.
 * <p>
 * Runs on port 5002.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 13, 2024
 */

public class PlatformServer {
    public static void main(String[] args) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(8);
        int port = 5002;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("Waiting for client to connect...");
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Waiting for client handler...");
                    service.execute(new PlatformClientHandler(socket));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
