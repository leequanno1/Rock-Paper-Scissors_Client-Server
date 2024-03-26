package app.game_client;
import app.game_server.GameServer;
import ocsf.client.*;
import java.io.*;

public class GameClient extends ObservableClient {

    private String userName;
    private boolean isEnterOk;
    private boolean isServerRespone;

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
     * @param userName
     */
    public GameClient(String host, int port) {
        super(host, port);
        isEnterOk = false;
        isServerRespone = false;
    }

    /**
     * the <code>isServerErrorMessage</code> method is return true if
     * the serverMessage match the errorType and return false if it's not
     * @param serverMessage
     * @param errorType
     */
    private boolean isServerErrorMessage(Object serverMessage,String errorType){
        return ((String) serverMessage).equals(errorType);
    }

    public void sendToServerAndWait(Object mess) throws IOException{
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
        // Check if Username existed
        if(isServerErrorMessage(msg,GameServer.USERNAME_EXISTED)){
            return;
        }
        // Check if room is full
        if(isServerErrorMessage(msg,GameServer.ROOM_FULL)){
            return;
        }
        setEnterOk(true);
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
            String username;
            do {
                username = ClientService.getUserNameFormInput();
                client.sendToServerAndWait(username);
            } while (!client.isEnterOk());
            client.setUserName(username);

            Short roomNumber;
            do {
                roomNumber = ClientService.getRoomFormInput();
                client.sendToServerAndWait(roomNumber);
            } while (!client.isEnterOk());

            System.out.println("Welcome to the chat, " + client.userName + "! Type your messages below.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                client.sendToServer(reader.readLine());
            }
        } catch (Exception e) {
            System.err.println("Error: Could not connect to server.");
        }
    }
}

