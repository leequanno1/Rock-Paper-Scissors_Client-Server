package app.game_server;
import ocsf.server.*;

import java.io.IOException;

public class ChatServer extends ObservableServer {

    public ChatServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object message, ConnectionToClient client) {
        // Broadcast the message to all clients
        if(client.getInfo("userName")==null){
            client.setInfo("userName", message.toString());
        }else{
            sendToAllClients(client.getInfo("userName") + ": " + message);
        }
    }

    @Override
    protected synchronized void clientConnected(ConnectionToClient client) {
        System.out.println(client.getInetAddress() + " connected");
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println(client.getInfo("userName") + " disconnected");
    }

    public static void main(String[] args) {
        int port = 1234; // Choose your port number
        ChatServer server = new ChatServer(port);
        try {
            server.listen();
            System.out.println("Server started on port " + port);
        } catch (Exception e) {
            System.err.println("Error: Could not start server.");
            e.printStackTrace();
        }
    }
}