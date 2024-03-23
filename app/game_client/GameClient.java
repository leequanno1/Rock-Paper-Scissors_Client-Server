package app.game_client;
import app.game_server.GameServer;
import ocsf.client.*;

import java.io.*;

public class GameClient extends ObservableClient {

    private String userName;
    private short room;
    private short point;

    public String getUserName() {
        return userName;
    }
    public GameClient(String host, int port, String userName) {
        super(host, port);
        this.userName = userName;
    }

    private void handleServerErrorMessage(Object serverMessage,String errorType, Object sendBackData){
        if(serverMessage.toString().equals(errorType)){
            try {
                sendToServer(sendBackData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        // Print message received from server
        System.out.println(msg);
        // Check if Username existed
        handleServerErrorMessage(msg,GameServer.USERNAME_EXISTED,ClientService.getUserNameFormInput());
        // Check if room is full
        handleServerErrorMessage(msg,GameServer.ROOM_FULL,ClientService.getRoomFormInput());
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
            GameClient client = new GameClient(host, port, ClientService.getUserNameFormInput());
            client.openConnection();
            client.sendToServer(client.userName);
            Thread.sleep(500);
            client.sendToServer(ClientService.getRoomFormInput());
            System.out.println("Welcome to the chat, " + client.userName + "! Type your messages below.");
            String message;
            while (true) {
                message = reader.readLine();
                client.sendToServer(message);
            }
        } catch (Exception e) {
            System.err.println("Error: Could not connect to server.");
        }
    }
}

