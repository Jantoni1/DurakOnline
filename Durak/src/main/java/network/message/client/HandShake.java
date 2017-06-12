package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;
import main.java.controller.server.ServerVisitor;

public class HandShake extends BaseClientMessage{

    public HandShake() {}

    public HandShake(String userName) {
        this.userName = userName;
    }


    @Override
    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) {
        visitor.visit(pClientThread, this);
    }

    private static final long serialVersionUID = 8L;

    public String getUserName() {
        return userName;
    }

    public String userName;
}
