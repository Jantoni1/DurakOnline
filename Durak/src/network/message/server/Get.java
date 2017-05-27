package network.message.server;


import controller.Visitor;
import model.Card;

import java.util.ArrayList;

public class Get extends BaseServerMessage {


    public Get(ArrayList<Card> cardArrayList) {
        this.cardArrayList = cardArrayList;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 11L;

    public ArrayList<Card> cardArrayList;
}
