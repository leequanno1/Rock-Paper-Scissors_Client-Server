package app.game_server;

import app.messages.PlayTurnMessage;
import ocsf.server.ConnectionToClient;

public class Player {
    private String username;
    private Short currentDecision;
    private Short point;
    private ConnectionToClient conn;

    public Player(String username,ConnectionToClient conn) {
        this.username = username;
        this.currentDecision = PlayTurnMessage.NOT_SELECTED;
        this.point = 0;
        this.conn = conn;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Short getCurrentDecision() {
        return currentDecision;
    }
    public void setCurrentDecision(Short currentDecision) {
        this.currentDecision = currentDecision;
    }
    public Short getPoint() {
        return point;
    }
    public void setPoint(Short point) {
        this.point = point;
    }

    public void increasePoint(){
        this.point++;
    }

    public void decreasePoint(){
        this.point--;
    }

    public ConnectionToClient getConnectionToClient() {
        return conn;
    }

    public void setConnectionToClient(ConnectionToClient conn) {
        this.conn = conn;
    }

    public void resetDecision(){
        this.currentDecision = PlayTurnMessage.NOT_SELECTED;
    }

    public String toString(){
        return username;
    }
}
