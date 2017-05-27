package network.message.server;


import controller.Visitor;

public class NextRound extends BaseServerMessage {

    public NextRound(boolean isTaking, int playerId) {
        this.isTaking = isTaking;
        this.playerId = playerId;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 14L;

    boolean isTaking;
    /**
     * @param used to describe who is potentially taking cards
     */
    int playerId;
}
