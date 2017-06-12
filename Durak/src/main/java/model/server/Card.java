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
        if(this.mSuit.equals(other.mSuit)) {
            return this.mFigure.isBigger(other.mFigure);
        }
        else if(other.mSuit == trump) {
            return true;
        }
        return false;
    }

    public boolean equals(Card other) {
        return mFigure == other.mFigure && mSuit == other.mSuit;
    }

    private static final long serialVersionUID = 98L;

    public Figures mFigure;
    public Suit mSuit;
    public static Suit trump;
}
