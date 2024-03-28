package app.messages;

import java.io.Serializable;

public class ServerPlayerDisconnectMessage extends CustomMessage implements Serializable {


    public ServerPlayerDisconnectMessage() {
    }

    /**
     * @return
     */
    @Override
    public Short getMessageType() {
        return null;
    }
}
