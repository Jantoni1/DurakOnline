package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;
import main.java.controller.server.RoomVisitor;

public class Leave extends BaseClientMessage {

    public Leave(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public void accept(BaseServerVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientThread pClientThread, RoomVisitor visitor) {
        visitor.visit( pClientThread, this);
    }

    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) {
        visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 6L;

    private boolean isFinal;


}