package app.messages;

public class ServerResultMessage implements CustomMessage{
    private String player1;
    private String player2;
    private short player1Decision;
    private short player2Decision;
    private String playerWinRound;

    public ServerResultMessage(String player1, String player2, short player1Decision, short player2Decision, String playerWinRound) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Decision = player1Decision;
        this.player2Decision = player2Decision;
        this.playerWinRound = playerWinRound;
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
        return playerWinRound;
    }

    /**
     * @return
     */
    @Override
    public short getMessageType() {
        return CustomMessage.SERVER_RESULT_MESSAGE;
    }
}
