package main.java.network.message.server;


import main.java.model.server.Card;
import main.java.controller.Visitor;
import main.java.network.message.Message;

public class Play extends Message {


    public Play(Card pCard, boolean pIfAttacking, int pPlayerId) {
         mCard = pCard;
         mIfAttacking = pIfAttacking;
         mPlayerId = pPlayerId;
    }

    public int getmPlayerId() {
        return mPlayerId;
    }

    public Card getmCard() {
        return mCard;
    }

    public boolean ismIfAttacking() {
        return mIfAttacking;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 13L;

    private int mPlayerId;
    private Card mCard;
    private boolean mIfAttacking;
}
