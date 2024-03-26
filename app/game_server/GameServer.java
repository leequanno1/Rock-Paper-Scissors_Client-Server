package app.game_server;
import ocsf.server.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServer extends ObservableServer {
    /**
     * An <code>USERNAME_EXISTED</code> is an username Error message
     */
    public static final String USERNAME_EXISTED = "Your username already exist";

    /**
     * An <code>ROOM_FULL</code> is an room full Error message
     */
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
    private static Map<Short, Room> roomToPlayerMap = new HashMap<>();

    /**
     * 
     * @param port
     */
    public GameServer(int port) {
        super(port);
    }

    /**
     * 
     * @param message
     * @param errMessage
     * @return true if <code>Object message</code> is an Error Message
     */
    private boolean isErorMessage(Object message, String errMessage){
        String messStr = (String) message;
        return messStr.equals(errMessage);
    }

    private boolean isUserExist(String username){
        return userToRoomMap.containsKey(username);
    }

    private boolean isRoomExist(Short room){
        return roomToPlayerMap.containsKey(room);
    }

    @Override
    protected void handleMessageFromClient(Object message, ConnectionToClient client) {
        // Checking for userName if it exist
        if(client.getInfo("username") == null){
            String username = (String) message;
            // If user is available : and userToRoom(value is null) and set info("username") and send back username
            try {
                if(!isUserExist(username)){
                    userToRoomMap.put(username, null);
                    client.setInfo("username", username);
                    client.sendToClient("Your username is: " + username);
                    return;
                } else {
                // If user is exist: send to client an error message
                    client.sendToClient(USERNAME_EXISTED);
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        // Checking for room if it's full
        if (client.getInfo("room") == null){
            Short roomNumer = (Short) message;
            try {
                // If room does not exist? create a room and add Player to room:
                if(!isRoomExist(roomNumer)){
                    Room room = new Room(roomNumer);
                    room.addPlayer((String)client.getInfo("username"), client);
                    roomToPlayerMap.put(roomNumer,room);
                }
                // If room existed? 
                //      (check if room is not full? 
                //              add Player to roomToPlayerMap and add room to userToRoom : 
                //              send back an error message):
                else
                {
                    Room room = roomToPlayerMap.get(roomNumer);
                    if(!room.isFull()){
                        room.addPlayer((String)client.getInfo("username"), client);
                        roomToPlayerMap.replace(roomNumer, room);
                    } else {
                        client.sendToClient(ROOM_FULL);
                        return;
                    }
                }
                userToRoomMap.replace((String)client.getInfo("username"), roomNumer);
                client.sendToClient("You have entered room "+ roomNumer);
                return;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    @Override
    protected synchronized void clientConnected(ConnectionToClient client) {
        System.out.println(client.getInetAddress() + " connected");
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println(client.getInfo("username") + " disconnected");
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