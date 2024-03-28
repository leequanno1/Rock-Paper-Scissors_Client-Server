package app.game_server;
import app.messages.CustomMessage;
import app.messages.PlayTurnMessage;
import app.messages.ServerPlayerDisconnectMessage;
import app.messages.ServerResultMessage;
import ocsf.server.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer extends ObservableServer {
    /**
     * An <code>USERNAME_EXISTED</code> is a username Error message
     */
    public static final String USERNAME_EXISTED = "Your username already exist";

    /**
     * An <code>ROOM_FULL</code> is a room full Error message
     */
    public static final String ROOM_FULL = Room.ERR_OUT_OF_CAPACITY;
    /**
     * 
     */
    public static final String WAIT_PLAYER = "Wait for another player...";

    /**
     * 
     */
    public static final String PLAYER_READY = "Player is ready.";

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

    private boolean isUserExist(String username){
        return userToRoomMap.containsKey(username);
    }

    private boolean isRoomExist(Short room){
        return roomToPlayerMap.containsKey(room);
    }

    @Override
    protected void handleMessageFromClient(Object message, ConnectionToClient client) {
        // Checking for userName if it's exist
        if(client.getInfo("username") == null){
            String username = (String) message;
            // If user is available : and userToRoom(value is null) and set info("username") and send back username
            try {
                if(!isUserExist(username)){
                    userToRoomMap.put(username, null);
                    client.setInfo("username", username);
                    String mess = "Your username is: " + username +
                            "\nPlease choose one of the given rooms below or a new room (1 - 100).\n" +
                            "\u001B[33mAvailable room: ";
                    for(Short rNB : roomToPlayerMap.keySet()){
                        if(!roomToPlayerMap.get(rNB).isFull()){
                            mess += rNB + ", ";
                        }
                    }
                    if(roomToPlayerMap.keySet().isEmpty()){
                        mess += "No room available.";
                    }
                    mess += "\u001B[0m";
                    client.sendToClient(mess);
                } else {
                // If user exist: send to client an error message
                    client.sendToClient(USERNAME_EXISTED);
                }
                return;
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        // Checking for room if it's full
        if (client.getInfo("room") == null){
            Short roomNumber = (Short) message;
            try {
                // If room does not exist? create a room and add Player to room:
                if(!isRoomExist(roomNumber)){
                    Room room = new Room(roomNumber);
                    room.addPlayer((String)client.getInfo("username"), client);
                    roomToPlayerMap.put(roomNumber,room);
                    client.sendToClient("You have entered room "+ roomNumber);
                    client.sendToClient(WAIT_PLAYER);
                }
                // If room existed? 
                //      (check if room is not full? 
                //              add Player to roomToPlayerMap and add room to userToRoom : 
                //              send back an error message):
                else
                {
                    Room room = roomToPlayerMap.get(roomNumber);
                    if(!room.isFull()){
                        room.addPlayer((String)client.getInfo("username"), client);
                        roomToPlayerMap.replace(roomNumber, room);
                        String mess = "You have entered room "+ roomNumber +"\n";
                        client.sendToClient(mess);
                        // Notify to player the room is ready (has 2 player)
                        List<Player> players = new ArrayList<>(room.getPlayerMap().values());
                        for(Player player : players){
                            player.getConnectionToClient().sendToClient(PLAYER_READY);
                        }
                    } else {
                        client.sendToClient(ROOM_FULL);
                        return;
                    }
                }
                userToRoomMap.replace((String)client.getInfo("username"), roomNumber);
                client.setInfo("room", roomNumber);
                return;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        // Handle play turn message
        if(message instanceof PlayTurnMessage){
            System.out.println(message);
            PlayTurnMessage playTurnMessage;
            playTurnMessage = (PlayTurnMessage) message;
            String username = (String) client.getInfo("username");
            Short roomNumber = (Short) client.getInfo("room");
            Room room = roomToPlayerMap.get(roomNumber);
            room.setPlayerDecision(username,playTurnMessage.getPlayTurnDecision());
            // Check if 2 player submitted decision ? send back to client result message : do nothing.
            if(room.canEndTurn()){
                System.out.println(room.getPlayerWinRound());
                room.sendTurnResultToClient();
                room.endTurn();
                // check if 1 player's point is equal 3 ? send a winround message to both clienth : doing nothing
                if(room.canEndRound()){
                    room.sendRoundResultToClient();
                    room.resetPoint();
                }
            }
            roomToPlayerMap.replace(roomNumber,room);
        }
    }

    @Override
    protected synchronized void clientConnected(ConnectionToClient client) {
        System.out.println(client.getInetAddress() + " connected");
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        Short roomNumber = (Short) client.getInfo("room");
        String username = (String) client.getInfo("username");
        if(roomNumber != null){
            Room room = roomToPlayerMap.get(roomNumber);
            try {
                room.removePlayer((String) client.getInfo("username"));
                // if room is empty? remove room : modify room
                if(room.getPlayerMap().isEmpty()){
                    roomToPlayerMap.remove(roomNumber);
                }else {
                    Player player = (new ArrayList<Player>(room.getPlayerMap().values())).get(0);
                    player.getConnectionToClient().sendToClient(new ServerPlayerDisconnectMessage());
                    roomToPlayerMap.replace(roomNumber,room);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(username != null){
            userToRoomMap.remove(username);
        }
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