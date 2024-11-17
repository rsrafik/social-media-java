import java.io.*;
import java.net.*;

/**
 * PlatformServer
 * <p>
 * Entry point for the server.
 * 
 * Uses multi-threading to instantiate several client handlers to support multiple clients.
 * 
 * Runs on port 5000.
 * </p>
 * 
 * @author Ropan Datta, L22
 * @version November 13, 2024
 */

public class PlatformServer {
    public static void main(String[] args) throws IOException {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("Waiting for client to connect...");
                try {
                    Socket socket = serverSocket.accept();
                    Thread thread = new Thread(new PlatformClientHandler(socket));
                    thread.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
