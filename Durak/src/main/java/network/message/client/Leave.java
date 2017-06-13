package main.java.network.message.client;


import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.server.ClientThread;

public class Leave extends Message {

    public Leave(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void accept(ClientThread pClientThread, Visitor visitor) {
        visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 6L;

    private boolean isFinal;


}
