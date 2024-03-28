package app.messages;

import java.io.Serializable;

public abstract class CustomMessage implements Serializable {
    public static final Short ROOM_MESSAGE = 0;
    public static final Short PLAY_TURN_MESSAGE = 1;
    public static final Short SERVER_RESULT_MESSAGE = 2;
    public abstract Short getMessageType();
}
