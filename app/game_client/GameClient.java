package app.game_client;

import app.game_server.GameServer;
import app.messages.PlayTurnMessage;
import app.messages.ServerPlayerDisconnectMessage;
import app.messages.ServerResultMessage;
import app.messages.ServerWinMessage;
import ocsf.client.*;
import java.io.*;

public class GameClient extends ObservableClient {

    private String userName;
    private Short roomNumber;
    private Short point;
    private boolean isEnterOk;
    private boolean isServerRespone;
    private boolean isRoomReady;
    private boolean isRoundEnd;

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

    public boolean isRoundEnd() {
        return isRoundEnd;
    }

    public void setRoundEnd(boolean roundEnd) {
        isRoundEnd = roundEnd;
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
        isRoundEnd = false;
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
        if(!msg.toString().contains("app.messages")){
            System.out.println(msg);
        }
        // Handle player disconnected
        if (msg instanceof ServerPlayerDisconnectMessage){
            System.out.println("\u001B[31m\nAnother player is disconnected.\u001B[0m");
            System.out.println("\u001B[33mThis play round end with your win.\u001B[0m");
            try {
                this.closeConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(1);
        }

        // Handle ServerWinMessage
        if (msg instanceof ServerWinMessage){
            String playerWinRound = ((ServerWinMessage)msg).getWinPlayerName();
            isRoundEnd = true;
            if(playerWinRound.equals(userName)){
                System.out.println("\u001B[33mYou win this round.\u001B[0m");
            } else {
                System.out.println("\u001B[33mPlayer "+ playerWinRound + " win this round.\nYou lose.\u001B[0m");
            }
            System.exit(1);
        }
        // Handle ServerResultMessage
        if (msg instanceof ServerResultMessage){
            String playerWinTurn = ((ServerResultMessage) msg).getPlayerWinTurn();
            if(playerWinTurn.equals(ServerResultMessage.DRAW)){
                System.out.println("\u001B[33mDRAW!!! Your score is: "+ point +" \u001B[0m");
                return;
            }
            if(playerWinTurn.equals(userName)){
                point++;
                System.out.println("\u001B[33mYou have win this turn. Your score is: "+ point +" \u001B[0m");
            } else {
                System.out.println("\u001B[33mYou have lose this turn. Your score is: "+ point +"\u001B[0m");
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
            System.out.print("\u001B[33mWelcome to the room " + client.getRoomNumber() + ", " + client.userName + "!\n\u001B[0m");
            // in loop
            // Handle gameplay
//           ClientService.handleGamePlay(client);
            int i = 0;
            while (true){
                System.out.println("\u001B[34m" + "\n>> Turn " + i +"\u001B[0m");
                ClientService.printDecisions();
                Short desision = null;
                do {
                    desision = ClientService.getDecisionsFromInput();
                } while (desision == null);
                PlayTurnMessage playTurnMessage = new PlayTurnMessage(desision);
                client.sendToServerAndWait(playTurnMessage);
                Thread.sleep(500);
                if(client.isRoundEnd()){
                    break;
                }
                i++;
            }
            client.closeConnection();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: Could not connect to server.");
        }
    }
}
