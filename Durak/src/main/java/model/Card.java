package main.java.model;

public class Card {


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
    public Figures mFigure;
    public Suit mSuit;
    public static Suit trump;
}
