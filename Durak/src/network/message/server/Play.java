package network.message.server;


import controller.Visitor;
import model.Card;

public class Play extends BaseServerMessage {


    public Play(Card card, int playerId) {
        this.card = card;
        this.playerId = playerId;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 13L;

    public Card card;
    public int playerId;
}
