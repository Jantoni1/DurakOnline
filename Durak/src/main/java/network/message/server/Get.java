package main.java.network.message.server;


import main.java.model.server.Card;
import main.java.controller.Visitor;
import main.java.network.message.Message;

import java.util.ArrayList;

public class Get extends Message {


    public Get(int pPlayerID, int pNumberOfCards, ArrayList<Card> cardArrayList) {
        mPlayerID = pPlayerID;
        mNumberOfCards = pNumberOfCards;
        this.cardArrayList = cardArrayList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 11L;

    public ArrayList<Card> getCardArrayList() {
        return cardArrayList;
    }

    public int getPlayerID() {
        return mPlayerID;
    }

    public int getNumberOfCards() {
        return mNumberOfCards;
    }

    private ArrayList<Card> cardArrayList;
    private int mPlayerID;
    private int mNumberOfCards;
}
