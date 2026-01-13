import java.io.*;
import java.net.*;

public class ChatServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT + ". Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress());

            // Streams for server to client communication
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from client
            Thread readFromClient = new Thread(() -> {
                String msgFromClient;
                try {
                    while ((msgFromClient = in.readLine()) != null) {
                        System.out.println("Client: " + msgFromClient);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by client.");
                }
            });
            readFromClient.start();

            // Main thread to send messages to client
            String msgToClient;
            while ((msgToClient = console.readLine()) != null) {
                out.println(msgToClient);
                if (msgToClient.equalsIgnoreCase("/quit")) {
                    break;
                }
            }

            System.out.println("Server closing connection.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
