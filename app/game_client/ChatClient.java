package app.game_client;
import ocsf.client.*;

import java.io.*;

public class ChatClient extends ObservableClient {

    private String userName;

    public String getUserName() {
        return userName;
    }
    public ChatClient(String host, int port, String userName) {
        super(host, port);
        this.userName = userName;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        // Print message received from server
        System.out.println(msg);
    }

    @Override
    protected void connectionClosed() {
        System.out.println("Connection to server closed.");
    }

    public static void main(String[] args) {
        String host = "localhost"; // Server's host name
        int port = 1234; // Server's port number

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your name: ");
            String userName = reader.readLine();

            ChatClient client = new ChatClient(host, port, userName);
            client.openConnection();

            System.out.println("Welcome to the chat, " + userName + "! Type your messages below.");
            String message;
            while (true) {
                message = reader.readLine();
                client.sendToServer(userName + ": " + message);
            }
        } catch (Exception e) {
            System.err.println("Error: Could not connect to server.");
            e.printStackTrace();
        }
    }
}

