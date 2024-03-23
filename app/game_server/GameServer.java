package app.game_server;
import ocsf.server.*;

import java.io.IOException;

public class GameServer extends ObservableServer {
    public static final String USERNAME_EXISTED = "Your username already exist";
    public static final String ROOM_FULL = "Your room is full";

    public GameServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object message, ConnectionToClient client) {
        // Checking for userName
        if(client.getInfo("userName")==null){
            client.setInfo("userName", message.toString());
            try {
                // Checking if username existed
                client.sendToClient("Your user name is "+ message + " now!");
                // sent to client "a list of room" message
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Checking for room
        if(client.getInfo("room")==null){
            client.setInfo("room", message.toString());
            try {
                client.sendToClient("You have chosen room " + message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        GameServer server = new GameServer(port);
        try {
            server.listen();
            System.out.println("Server started on port " + port);
        } catch (Exception e) {
            System.err.println("Error: Could not start server.");
        }
    }
}