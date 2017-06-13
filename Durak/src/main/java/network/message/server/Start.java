package main.java.network.message.server;


import main.java.controller.Visitor;
import main.java.model.server.Card;
import main.java.network.message.Message;

public class Start extends Message {

    public Start(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 17L;

    private Card card;

}
