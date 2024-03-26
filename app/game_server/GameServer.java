package app.game_server;
import ocsf.server.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServer extends ObservableServer {
    public static final String USERNAME_EXISTED = "Your username already exist";
    public static final String ROOM_FULL = Room.ERR_OUT_OF_CAPACITY;

    /**
     * The <code>userToRoomMap</code> is a Map that has
     * @key is username
     * @value is roomNumber
     * */
    private static Map<String,Short> userToRoomMap = new HashMap<>();
    /**
     * The <code>roomMap</code> is a Map that has
     * @key is Short roomNumber
     * @value is a Room Object
     * */
    private static Map<Short, Room> roomMap = new HashMap<>();

    public GameServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object message, ConnectionToClient client) {
        // Checking for userName
        if(client.getInfo("userName")==null){
            // Checking if username existed
            if(!handleIsExistClientInfo(client,userToRoomMap.get(message.toString()),USERNAME_EXISTED)){
                client.setInfo("userName", message.toString());
                try {
                    message = "Your user name is "+ message + " now!\n";
                    // add available RoomList to message.
                    client.sendToClient(message);
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // Checking for room if it's full
        if(client.getInfo("room")==null){
            Short roomNumber = (Short) message;
            if(roomMap.get(roomNumber) == null){
                // setInfo room for client
                client.setInfo("room", roomNumber);
                // create room and add player to room
                Room room = new Room(roomNumber);
                try {
                    room.addPlayer(client.getInfo("userName").toString(), client);
                    roomMap.put(roomNumber, room);
                    client.sendToClient(Room.OK_ENTERED);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                if(!roomMap.get(roomNumber).isFull()){
                    Room room = roomMap.get(roomNumber);
                    try {
                        room.addPlayer(client.getInfo("userName").toString(), client);
                        roomMap.replace(roomNumber,room);
                        client.sendToClient(Room.OK_ENTERED);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        client.sendToClient(Room.ERR_OUT_OF_CAPACITY);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return;
        }

        try {
            client.sendToClient(client.getInfo("userNam").toString()+": "+ message);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    /**
     * Function <code>handleExistClientInfo</code> has 4 param
     * @param client is a <type>ConnectionToClient</type>;
     * @param checkData is a <type>Object</type>;
     * @param sendBackErrorMessage is a <type>Object</type>;
     * */
    private boolean handleIsExistClientInfo(ConnectionToClient client,
                                       Object checkData,
                                       Object sendBackErrorMessage)
    {
        if(checkData!=null){
            try {
                client.sendToClient(sendBackErrorMessage);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
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