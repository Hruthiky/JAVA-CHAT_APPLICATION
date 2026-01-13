import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_IP = "127.0.0.1"; // change if server is remote
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from server
            Thread readFromServer = new Thread(() -> {
                String msgFromServer;
                try {
                    while ((msgFromServer = in.readLine()) != null) {
                        System.out.println("Server: " + msgFromServer);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by server.");
                }
            });
            readFromServer.start();

            // Main thread to send messages to server
            String msgToServer;
            while ((msgToServer = console.readLine()) != null) {
                out.println(msgToServer);
                if (msgToServer.equalsIgnoreCase("/quit")) {
                    break;
                }
            }

            System.out.println("Client closing connection.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
