package main.java.network.message.server;

import main.java.controller.Visitor;
import main.java.network.message.Message;


public class End extends Message {

    private String mPlayerNick;
    private int mPlayerID;

    public End(int mPlayerID, String pPlayersNick) {
        mPlayerNick = pPlayersNick;
        this.mPlayerID = mPlayerID;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getmPlayerNick() {
        return mPlayerNick;
    }

    public int getPlayerId() {
        return mPlayerID;
    }



    private static final long serialVersionUID = 24L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    

}
