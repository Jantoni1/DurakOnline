package network.message.client;


import controller.Visitor;

public class Leave extends BaseClientMessage {

    public Leave() {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 6L;
}
