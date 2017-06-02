package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;

public class Show extends BaseClientMessage {

    public Show() {}

    @Override
    public void accept(BaseServerVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) { visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 4L;
}
