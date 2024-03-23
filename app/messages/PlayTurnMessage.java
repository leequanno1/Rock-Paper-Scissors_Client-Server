package app.messages;

public class PlayTurnMessage implements CustomMessage{
    public static final short ROCK = 0;
    public static final short PAPER = 1;
    public static final short SCISSORS = 3;

    private short decision;

    public PlayTurnMessage(short decision) {
        this.decision = decision;
    }

    /**
     * @return
     */
    @Override
    public short getMessageType() {
        return CustomMessage.PLAY_TURN_MESSAGE;
    }

    /**
     * @return
     */
    public short getPlayTurnDecision() {
        return decision;
    }
}
