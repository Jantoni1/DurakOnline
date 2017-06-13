package main.java.network.message.server;


import main.java.controller.Visitor;
import main.java.network.message.Message;

public class Leave extends Message {

    public Leave(int playerId) {
        this.playerId = playerId;
    }

    private static final long serialVersionUID = 12L;

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int playerId;
}
