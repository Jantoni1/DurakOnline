package main.java.network.message.server;

import main.java.controller.Visitor;
import main.java.network.message.Message;


public class Disconnected extends Message {

    public Disconnected() {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 16L;


}
