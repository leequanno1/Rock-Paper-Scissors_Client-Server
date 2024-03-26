package app.messages;

public class PlayTurnMessage implements CustomMessage{
    public static final short NULL = -2;
    public static final short NOT_SELECTED = -1;
    public static final short ROCK = 0;
    public static final short PAPER = 1;
    public static final short SCISSORS = 2;

    private short decision;

    public PlayTurnMessage(short decision) {
        this.decision = decision;
    }

    /**
     * @return <type>short</type> MessageType
     */
    @Override
    public short getMessageType() {
        return CustomMessage.PLAY_TURN_MESSAGE;
    }

    /**
     * @return <type>short</type> PlayTurnDecision
     */
    public short getPlayTurnDecision() {
        return decision;
    }
}
