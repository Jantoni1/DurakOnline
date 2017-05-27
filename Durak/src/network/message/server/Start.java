package network.message.server;


import controller.Visitor;

public class Start extends BaseServerMessage {

    public Start() {
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 17L;

}
