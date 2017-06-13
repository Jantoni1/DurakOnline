package main.java.network.message.client;

import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.server.ClientThread;


public class CreateRoom extends Message {


    public void accept(ClientThread pClientThread, Visitor visitor) { visitor.visit(pClientThread, this);}

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
