package main.java.network.message.client;


import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.server.ClientThread;

public class HandShake extends Message {

    public HandShake() {}

    public HandShake(String userName) {
        this.userName = userName;
    }


    @Override
    public void accept(ClientThread pClientThread, Visitor visitor) {
        visitor.visit(pClientThread, this);
    }

    private static final long serialVersionUID = 8L;

    public String getUserName() {
        return userName;
    }

    public String userName;
}
