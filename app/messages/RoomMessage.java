package app.messages;

public class RoomMessage implements CustomMessage{
    public static final short ROOM_EXIT = 0;

    private short room;

    public RoomMessage(short room) {
        this.room = room;
    }

    /**
     * @return
     */
    @Override
    public short getMessageType() {
        return CustomMessage.ROOM_MESSAGE;
    }

    /**
     * @return
     */
    public short getRoom() {
        return room;
    }

    /**
     * @return
     */
}
