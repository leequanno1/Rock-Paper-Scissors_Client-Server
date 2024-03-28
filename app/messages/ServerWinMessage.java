package app.messages;

import java.io.Serializable;

@SuppressWarnings("unused")
public class ServerWinMessage extends CustomMessage implements Serializable{

    private String winPlayerName;

    public String getWinPlayerName() {
        return winPlayerName;
    }
    @Override
    public Short getMessageType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMessageType'");
    }
    
    public ServerWinMessage(String winPlayerName){
        this.winPlayerName = winPlayerName;
    }
}
