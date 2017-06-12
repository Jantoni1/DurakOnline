package main.java.network.message.server;


import main.java.model.server.Card;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.BaseClientVisitor;

import java.util.ArrayList;

public class Play extends BaseServerMessage {


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

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 13L;

    private int mPlayerId;
    private Card mCard;
    private boolean mIfAttacking;
}
