package app.messages;

public interface CustomMessage {
    public static final short ROOM_MESSAGE = 0;
    public static final short PLAY_TURN_MESSAGE = 1;
    public static final short SERVER_RESULT_MESSAGE = 2;
    public short getMessageType();
}
