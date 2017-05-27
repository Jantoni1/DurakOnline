package network.message.client;


import controller.Visitor;

public class Show extends BaseClientMessage {

    public Show() {}

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 4L;
}
