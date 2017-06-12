package main.java.model.client;


import main.java.model.server.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class AnotherPlayer implements Serializable {

    private int mNumberOfCards;
    private String mNick;
    private int mUserID;
    private int mPositionOnTable;
    private ArrayList<Card> mPlayerCards;
    private static final long serialVersionUID = 41L;

    //Add jest odpowiedzialne za dodawanie wszystkich z klientem włącznie, dzięki czemu rozwiązuje się problem kolejności dodwania użytkowników do pokoju
    boolean mIsMyTurn;

    public AnotherPlayer() {}

    public AnotherPlayer(String mNick, int mUserID) {
        this.mNick = mNick;
        this.mUserID = mUserID;
        mNumberOfCards = 0;
    }

    public ArrayList<Card> getPlayerCards() {
        return mPlayerCards;
    }

    public void setPlayerCards(ArrayList<Card> mPlayerCards) {
        this.mPlayerCards = mPlayerCards;
    }

    public void addCard(Card pCard) {
        mPlayerCards.add(pCard);
    }

    public void addMultipleCards(ArrayList<Card> pCards) {
        if(mPlayerCards == null) {
            mPlayerCards = new ArrayList<>();
        }
        mPlayerCards.addAll(pCards);
    }

    public String getmNick() {
        return mNick;
    }

    public int getmPositionOnTable() {
        return mPositionOnTable;
    }

    public void setmPositionOnTable(int mPositionOnTable) {
        this.mPositionOnTable = mPositionOnTable;
    }

    public int getmUserID() {
        return mUserID;
    }

    public int getmNumberOfCards() {

        return mNumberOfCards;
    }

    public void clearDeck() {
        mPlayerCards.clear();
    }

    public void setmNumberOfCards(int mNumberOfCards) {
        this.mNumberOfCards = mNumberOfCards;
    }

    public boolean ismIsMyTurn() {
        return mIsMyTurn;
    }

    public void setmIsMyTurn(boolean mIsMyTurn) {
        this.mIsMyTurn = mIsMyTurn;
    }
}
