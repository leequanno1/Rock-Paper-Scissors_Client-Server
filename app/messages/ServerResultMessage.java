package app.messages;

public class ServerResultMessage implements CustomMessage{
    public static final String DRAW = null;
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

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public short getPlayer1Decision() {
        return player1Decision;
    }

    public short getPlayer2Decision() {
        return player2Decision;
    }

    public String getPlayerWinRound() {
        switch (player1Decision){
            case PlayTurnMessage.NOT_SELECTED:
                if(player2Decision != PlayTurnMessage.NOT_SELECTED){
                    return player2;
                }
            case PlayTurnMessage.ROCK:
                if(player2Decision == PlayTurnMessage.PAPER){
                    return player2;
                }
                if(player2Decision == PlayTurnMessage.SCISSORS || player2Decision == PlayTurnMessage.NOT_SELECTED){
                    return player1;
                }
            case PlayTurnMessage.PAPER:
                if(player2Decision == PlayTurnMessage.SCISSORS){
                    return player2;
                }
                if(player2Decision == PlayTurnMessage.ROCK || player2Decision == PlayTurnMessage.NOT_SELECTED){
                    return player1;
                }
            case PlayTurnMessage.SCISSORS:
                if(player2Decision == PlayTurnMessage.ROCK){
                    return player2;
                }
                if(player2Decision == PlayTurnMessage.PAPER || player2Decision == PlayTurnMessage.NOT_SELECTED){
                    return player1;
                }
            default:
                return DRAW;
        }
    }

    /**
     * @return
     */
    @Override
    public short getMessageType() {
        return CustomMessage.SERVER_RESULT_MESSAGE;
    }
}
