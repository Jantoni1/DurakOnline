package network.message.client;


import controller.Visitor;

public class Ready extends BaseClientMessage {

    public Ready() {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 5L;
}
