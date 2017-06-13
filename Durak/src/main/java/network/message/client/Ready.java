package main.java.network.message.client;


import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.server.ClientThread;

public class Ready extends Message {

    public Ready(boolean pReadyIfTrueUnreadyOtherwise) {
        this.mReadyIfTrueUnreadyOtherwise = pReadyIfTrueUnreadyOtherwise;
    }

    public void accept(ClientThread pClientThread, Visitor visitor) { visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 5L;

    boolean mReadyIfTrueUnreadyOtherwise;

    public boolean ismReadyIfTrueUnreadyOtherwise() {
        return mReadyIfTrueUnreadyOtherwise;
    }


}
