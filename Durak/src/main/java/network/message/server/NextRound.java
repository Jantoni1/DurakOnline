package main.java.network.message.server;


import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;

public class NextRound extends BaseServerMessage {

    public NextRound(boolean isTaking, int playerId) {
        this.isTaking = isTaking;
        this.playerId = playerId;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    private static final long serialVersionUID = 14L;

    boolean isTaking;
    /**
     * @param used to describe who is potentially taking cards
     */
    int playerId;
}
