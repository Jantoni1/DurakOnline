package main.java.network.message.server;


import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.model.server.Card;

public class Start extends BaseServerMessage {

    public Start(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 17L;

    private Card card;

}
