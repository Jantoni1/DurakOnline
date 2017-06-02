package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;


public class Disconnected extends BaseServerMessage {

    public Disconnected() {
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    private static final long serialVersionUID = 16L;


}
