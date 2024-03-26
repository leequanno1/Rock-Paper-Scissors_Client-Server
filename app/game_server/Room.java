package app.game_server;

import app.messages.PlayTurnMessage;
import app.messages.ServerResultMessage;
import ocsf.server.ConnectionToClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    /**
     * Error message that info the Room is out of capacity
     * */
    public static final String ERR_OUT_OF_CAPACITY = "ROOM IS OUT OF CAPACITY.";

    /**
     * Error message that info the Room is empty
     * */
    public static final String ERR_ROOM_EMPTY = "ROOM IS EMPTY.";
    public static final String OK_ENTERED = "PLAYER IS ENTERED.";

    /**
     * Variable saved info about room number
     * */
    private Short roomNumber;

    /**
     * playerMap is a Map that has
     * @key <type>String</type> username
     * @value <type>Player</type> player
     * The innit capacity is 2
     * */
    private Map<String,Player> playerMap;

    private String winGamePlayer;

    //CONSTRUCTOR====================================================
    /**
     * Constructor of the Room class
     * @param roomNumber the room number
     * */
    public Room(Short roomNumber) {
        this.roomNumber = roomNumber;
        playerMap = new HashMap<>(2);
        winGamePlayer = null;
    }

    public void setWinGamePlayer(String winGamePlayer) {
        this.winGamePlayer = winGamePlayer;
    }

    /**
     * The addPlayer method has
     * @param username the player's username
     * This method add a Player object into the playerMap.
     * If playerMap is full, it throws an exception.
     * */
    public void addPlayer(String username, ConnectionToClient conn) throws Exception {
        if(playerMap.size() < 2){
            Player player = new Player(username, conn);
            playerMap.put(username, player);
        } else {
            throw new Exception(ERR_OUT_OF_CAPACITY);
        }
    }

    /**
     * The removePlayer method has
     * @param username the player's username
     * This method remove a Player object into the playerMap.
     * If playerMap is empty, it throws an exception.
     * */
    public void removePlayer(String username) throws Exception {
        if(!playerMap.isEmpty()){
            playerMap.remove(username);
        } else {
            throw new Exception(ERR_ROOM_EMPTY);
        }
    }

    /**
     * The setPlayerDecision has
     * @param username the player's username
     * @param decision the player's decision
     * This method set the current player decision into a Player object
     * and update into a playerMap.
     * */
    public void setPlayerDecision(String username, Short decision){
        Player player = playerMap.get(username);
        player.setCurrentDecision(decision);
        playerMap.replace(username,player);
    }

    /**
     * Get the win round player name
     * */
    public String getPlayerWinRound(){
        String winPlayer = "";
        List<Player> playerList = new ArrayList<>(playerMap.values());
        Player player1 = playerList.getFirst();
        Player player2 = playerList.getLast();
        ServerResultMessage result = new ServerResultMessage(player1.getUsername(),
                player2.getUsername(),
                player1.getCurrentDecision(),
                player2.getCurrentDecision());
        winPlayer = result.getPlayerWinRound();
        if(!winPlayer.equals(ServerResultMessage.DRAW)){
            if(winPlayer.equals(player1.getUsername())){
                player1.increasePoint();
                playerMap.replace(winPlayer,player1);
                if(player2.getCurrentDecision() == PlayTurnMessage.NOT_SELECTED){
                    player2.decreasePoint();
                }
            } else {
                player2.increasePoint();
                playerMap.replace(winPlayer,player2);
                if(player1.getCurrentDecision() == PlayTurnMessage.NOT_SELECTED){
                    player1.decreasePoint();
                }
            }
        }
        if(player1.getPoint() == 3 || player2.getPoint() == 3){
            setWinGamePlayer(winPlayer);
        }
        return winPlayer;
    }

    public boolean isFull(){
        return this.playerMap.size() == 2;
    }

}
