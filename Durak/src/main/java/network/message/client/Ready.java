package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;
import main.java.controller.server.RoomVisitor;

public class Ready extends BaseClientMessage {

    public Ready(boolean pReadyIfTrueUnreadyOtherwise) {
        this.mReadyIfTrueUnreadyOtherwise = pReadyIfTrueUnreadyOtherwise;
    }

    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) { visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 5L;

    boolean mReadyIfTrueUnreadyOtherwise;

    public boolean ismReadyIfTrueUnreadyOtherwise() {
        return mReadyIfTrueUnreadyOtherwise;
    }


}
