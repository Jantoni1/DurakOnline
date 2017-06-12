package main.java.model.server;

import java.io.Serializable;

public class Card implements Serializable{

    Card() {}

    Card(Figures pFigure, Suit pSuit) {
        if(pFigure != null && pSuit != null) {
            mFigure = pFigure;
            mSuit = pSuit;
        }
    }
    Card(Card other) {
        mFigure = other.mFigure;
        mSuit = other.mSuit;
    }
    boolean isBigger(Card other) {
        if(this.mSuit.getColor() == other.mSuit.getColor()) {
            return this.mFigure.isBigger(other.mFigure);
        }
        else if(mSuit.getColor() == Card.trump.getColor()) {
            return true;
        }
        return false;
    }

    public static void setTrump(Suit trump) {
        Card.trump = trump;
    }

    public boolean equals(Card other) {
        return mFigure == other.mFigure && mSuit == other.mSuit;
    }

    private static final long serialVersionUID = 98L;

    public Figures mFigure;
    public Suit mSuit;
    public static Suit trump;
}
