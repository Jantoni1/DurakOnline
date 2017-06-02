package main.java.network.message.server;


import main.java.model.Card;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.BaseClientVisitor;

import java.util.ArrayList;

public class Play extends BaseServerMessage {


    public Play(ArrayList<Card> pCards, boolean pIfAttacking, int pPlayerId) {
         mCards = pCards;
         mIfAttacking = pIfAttacking;
         mPlayerId = pPlayerId;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    private static final long serialVersionUID = 13L;

    public int mPlayerId;
    public ArrayList<Card> mCards;
    public boolean mIfAttacking;
}
