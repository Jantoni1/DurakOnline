package main.java.network.message.client;

import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;
import main.java.controller.server.LobbyVisitor;


public class CreateRoom extends BaseClientMessage {


    @Override
    public void accept(BaseServerVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) { visitor.visit(pClientThread, this);}

    public void accept(ClientThread pClientThread, LobbyVisitor pLobbyVisitor) {
        pLobbyVisitor.visit(pClientThread, this);}

    public String toString() {
        return mRoomName;
    }

    public String getmRoomName() {
        return mRoomName;
    }

    public int getmMaxPlayers() {
        return mMaxPlayers;
    }

    public CreateRoom(String mRoomName, int mMaxPlayers) {
        this.mRoomName = mRoomName;
        this.mMaxPlayers = mMaxPlayers;
    }

    private static final long serialVersionUID = 9L;

    private final String mRoomName;
    private final int mMaxPlayers;

}
