package main.java.network.message.server;


import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;

public class Leave extends BaseServerMessage {

    public Leave(int playerId) {
        this.playerId = playerId;
    }

    private static final long serialVersionUID = 12L;

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    public int playerId;
}
