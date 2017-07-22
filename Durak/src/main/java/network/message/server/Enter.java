package main.java.network.message.server;

import main.java.controller.Visitor;
import main.java.model.client.Player;
import main.java.network.message.Message;

import java.util.concurrent.CopyOnWriteArrayList;


public class Enter extends Message {
    public Enter(String pRoomName, int pMaxPlayers, boolean pIfFailed ) {
        mRoomName = pRoomName;
        mMaxPlyaers = pMaxPlayers;
        mIfFailed = pIfFailed;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setmPlayers(CopyOnWriteArrayList<Player> mPlayers) {
        this.mPlayers = mPlayers;
    }

    public int getmMaxplyaers() {

        return mMaxPlyaers;
    }

    public String getmRoomName() {
        return mRoomName;
    }

    public boolean ismIfFailed() {
        return mIfFailed;
    }

    public CopyOnWriteArrayList<Player> getmPlayers() {
        return mPlayers;
    }

    private static final long serialVersionUID = 21L;

    private final int mMaxPlyaers;
    private final String mRoomName;
    private boolean mIfFailed;
    private CopyOnWriteArrayList<Player> mPlayers;
}
