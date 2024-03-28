package app.messages;

import java.io.Serializable;

public class PlayTurnMessage extends CustomMessage implements Serializable {
    public static final Short NULL = -2;
    public static final Short NOT_SELECTED = -1;
    public static final Short ROCK = 1;
    public static final Short PAPER = 2;
    public static final Short SCISSORS = 3;

    private Short decision;

    public PlayTurnMessage(Short decision) {
        this.decision = decision;
    }

    /**
     * @return <type>short</type> MessageType
     */
    @Override
    public Short getMessageType() {
        return CustomMessage.PLAY_TURN_MESSAGE;
    }

    /**
     * @return <type>short</type> PlayTurnDecision
     */
    public Short getPlayTurnDecision() {
        return decision;
    }
}
