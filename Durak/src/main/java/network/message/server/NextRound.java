package main.java.network.message.server;


import main.java.controller.Visitor;
import main.java.network.message.Message;

public class NextRound extends Message {

    public NextRound(boolean isTaking, int playerId, int pNumberOfCardsLeft) {
        this.isTaking = isTaking;
        this.playerId = playerId;
        mNumberOfCardsLeft = pNumberOfCardsLeft;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 14L;

    public boolean isTaking() {
        return isTaking;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getmNumberOfCardsLeft() {
        return mNumberOfCardsLeft;
    }

    private boolean isTaking;
    /**
     * @param used to describe who is taking cards if so
     */
    private int playerId;
    private int mNumberOfCardsLeft;


}
