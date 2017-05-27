package network.message.client;


import controller.Visitor;

public class Play extends BaseClientMessage {

    public Play(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 7L;
    int cardNumber;
}
