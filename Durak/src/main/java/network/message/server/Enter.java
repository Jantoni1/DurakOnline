package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;
import main.java.model.client.AnotherPlayer;

import java.util.ArrayList;


public class Enter extends BaseServerMessage {
    public Enter(String pRoomName, int pMaxPlayers, boolean pIfFailed ) {
        mRoomName = pRoomName;
        mMaxPlyaers = pMaxPlayers;
        mIfFailed = pIfFailed;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void setmPlayers(ArrayList<AnotherPlayer> mPlayers) {
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

    public ArrayList<AnotherPlayer> getmPlayers() {
        return mPlayers;
    }

    private static final long serialVersionUID = 21L;

    private final int mMaxPlyaers;
    private final String mRoomName;
    private boolean mIfFailed;
    private ArrayList<AnotherPlayer> mPlayers;
}
