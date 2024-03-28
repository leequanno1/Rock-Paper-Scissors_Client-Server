package app.game_client;

import app.game_server.GameServer;
import app.messages.CustomMessage;
import app.messages.PlayTurnMessage;
import app.messages.ServerResultMessage;
import ocsf.client.*;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GameClient extends ObservableClient {

    private String userName;
    private Short roomNumber;
    private Short point;
    private boolean isEnterOk;
    private boolean isServerRespone;
    private boolean isRoomReady;

    public Short getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Short roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isRoomReady() {
        return isRoomReady;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setEnterOk(boolean isEnterOk) {
        this.isEnterOk = isEnterOk;
    }

    public boolean isEnterOk() {
        return isEnterOk;
    }

    /**
     * 
     * @param host
     * @param port
     */
    public GameClient(String host, int port) {
        super(host, port);
        isEnterOk = false;
        isServerRespone = false;
        isRoomReady = false;
        point = 0;
    }

    /**
     * the <code>isServerErrorMessage</code> method is return true if
     * the serverMessage match the errorType and return false if it's not
     * 
     * @param serverMessage
     * @param errorType
     */
    private boolean isServerErrorMessage(Object serverMessage, String errorType) {
        return ((String) serverMessage).equals(errorType);
    }

    /**
     * The <code>sendToServerAndWait</code> send message to server and
     * wait for the server response the return.
     * 
     * @param mess
     * @throws IOException
     */
    public void sendToServerAndWait(Object mess) throws IOException {
        this.isServerRespone = false;
        this.sendToServer(mess);
        while (!isServerRespone) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.isServerRespone = false;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        this.isServerRespone = true;
        setEnterOk(false);
        // Print message received from server
        System.out.println(msg);
        // Handle ServerResultMessage
        if (msg instanceof ServerResultMessage){
            String playerWinTurn = ((ServerResultMessage) msg).getPlayerWinRound();
            if(playerWinTurn.equals(userName)){
                System.out.println("You have win this turn");
                point++;
            } else {
                System.out.println("You have lose this turn");
            }
            return;
        }
        // Check if Username existed
        if (isServerErrorMessage(msg, GameServer.USERNAME_EXISTED)) {
            return;
        }
        // Check if room is full
        if (isServerErrorMessage(msg, GameServer.ROOM_FULL)) {
            return;
        }
        setEnterOk(true);
        if (isServerErrorMessage(msg, GameServer.PLAYER_READY)) {
            isRoomReady = true;
            return;
        }
        // End errror handle
        // Start message handle


    }

    @Override
    protected void connectionClosed() {
        System.out.println("Connection to server closed.");
    }

    public static void main(String[] args) {
        String host = "localhost"; // Server's host name
        int port = 1234; // Server's port number

        try {
            GameClient client = new GameClient(host, port);
            client.openConnection();

            // Set username
            // Handle set username
            ClientService.handleSetUsername(client);


            // Set room number
            // Handle set room number
            ClientService.handleSetRoomNumber(client);

            // Room entered
            while (!client.isRoomReady()) {
                Thread.sleep(200);
            }
            System.out.print("Welcome to the room " + client.getRoomNumber() + ", " + client.userName + "!\n");
            // in loop
            // Handle gameplay
           ClientService.handleGamePlay(client);
           System.exit(1);

        } catch (Exception e) {
            System.err.println("Error: Could not connect to server.");
        }
    }
}
