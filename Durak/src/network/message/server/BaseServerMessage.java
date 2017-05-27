package network.message.server;


import controller.Visitor;

public class BaseServerMessage {

    public BaseServerMessage() {}

    void accept(Visitor visitor) {}

    private static final long serialVersionUID = 10L;

}
