package main.java.network.message.server;

import main.java.controller.Visitor;
import main.java.controller.Visitor;
import main.java.network.message.Message;


public class Welcome extends Message {

    public Welcome(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 19L;

    public int getPlayerId() {
        return playerId;
    }

    public String toString() {
        return "class welcome instance";
    }

    private int playerId;

}
