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

    private static final long serialVersionUID = 14L;

    public boolean isTaking() {
        return isTaking;
    }

    public int getPlayerId() {
        return playerId;
    }

    boolean isTaking;
    /**
     * @param used to describe who is potentially taking cards
     */
    int playerId;
}
