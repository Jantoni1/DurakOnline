package main.java.network.message.server;


import main.java.model.server.Card;
import main.java.controller.Visitor;
import main.java.network.message.Message;

import java.util.ArrayList;

public class Get extends Message {


    public Get(ArrayList<Card> cardArrayList) {
        this.cardArrayList = cardArrayList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 11L;

    public ArrayList<Card> getCardArrayList() {
        return cardArrayList;
    }

    private ArrayList<Card> cardArrayList;
}
