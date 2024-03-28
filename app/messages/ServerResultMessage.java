package app.messages;

import java.io.Serializable;

public class ServerResultMessage extends CustomMessage implements Serializable {
    public static final String DRAW = "null";
    private String player1;
    private String player2;
    private Short player1Decision;
    private Short player2Decision;

    public ServerResultMessage(String player1, String player2, Short player1Decision, Short player2Decision) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Decision = player1Decision;
        this.player2Decision = player2Decision;
    }

    public ServerResultMessage(Object object){
        ServerResultMessage mess = (ServerResultMessage) object;
        this.player1 = mess.getPlayer1();
        this.player2 = mess.getPlayer2();
        this.player1Decision = getPlayer1Decision();
        this.player2Decision = getPlayer2Decision();
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public Short getPlayer1Decision() {
        return player1Decision;
    }

    public Short getPlayer2Decision() {
        return player2Decision;
    }

    public String getPlayerWinTurn() {
        if (player1Decision.equals(PlayTurnMessage.NOT_SELECTED)){
            if(!player2Decision.equals(PlayTurnMessage.NOT_SELECTED)){
                return player2;
            }
        }
        if (player1Decision.equals(PlayTurnMessage.ROCK)){
            if(player2Decision.equals(PlayTurnMessage.PAPER)){
                return player2;
            }
            if(player2Decision.equals(PlayTurnMessage.SCISSORS)  || player2Decision.equals(PlayTurnMessage.NOT_SELECTED) ){
                return player1;
            }
        }
        if (player1Decision.equals(PlayTurnMessage.PAPER)){
            if(player2Decision.equals(PlayTurnMessage.SCISSORS) ){
                return player2;
            }
            if(player2Decision.equals(PlayTurnMessage.ROCK) || player2Decision.equals(PlayTurnMessage.NOT_SELECTED)){
                return player1;
            }
        }
        if (player1Decision.equals(PlayTurnMessage.SCISSORS)){
            if(player2Decision.equals(PlayTurnMessage.ROCK)){
                return player2;
            }
            if(player2Decision.equals(PlayTurnMessage.PAPER) || player2Decision.equals(PlayTurnMessage.NOT_SELECTED)){
                return player1;
            }
        }
        return DRAW;
    }

    /**
     * @return
     */
    @Override
    public Short getMessageType() {
        return CustomMessage.SERVER_RESULT_MESSAGE;
    }
}
