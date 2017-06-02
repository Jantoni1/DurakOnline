package main.java.network.message.server;


import main.java.model.Card;
import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;

import java.util.ArrayList;

public class Get extends BaseServerMessage {


    public Get(ArrayList<Card> cardArrayList) {
        this.cardArrayList = cardArrayList;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    private static final long serialVersionUID = 11L;

    public ArrayList<Card> cardArrayList;
}
