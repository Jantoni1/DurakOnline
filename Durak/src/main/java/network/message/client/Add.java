package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;
import main.java.controller.server.LobbyVisitor;

public class Add extends BaseClientMessage {


    public Add(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public void accept(BaseServerVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) { visitor.visit(pClientThread, this);}

    public void accept(ClientThread pClientThread, LobbyVisitor pLobbyVisitor) {pLobbyVisitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 2L;

    public int getRoomId() {
        return roomId;
    }

    private final int roomId;
}
