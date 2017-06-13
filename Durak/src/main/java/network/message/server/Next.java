package main.java.network.message.server;


import main.java.controller.Visitor;
import main.java.network.message.Message;

import java.util.ArrayList;

public class Next extends Message {

    private int mPlayerId;
    private ArrayList<Integer> mAvailableCards;
    private boolean mTrueIfAttackingFalseIfDefending;

    public Next(int pPlayerId, ArrayList<Integer> pAvailableCards, boolean pTrueIfAttackingFalseIfDefending) {
        mPlayerId = pPlayerId;
        mAvailableCards = pAvailableCards;
        mTrueIfAttackingFalseIfDefending = pTrueIfAttackingFalseIfDefending;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int getmPlayerId() {
        return mPlayerId;
    }

    public boolean ismTrueIfAttackingFalseIfDefending() {
        return mTrueIfAttackingFalseIfDefending;
    }

    public ArrayList<Integer> getmAvailableCards() {
        return mAvailableCards;
    }

    private static final long serialVersionUID = 15L;

}
