package main.java.network.message.server;


import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;

import java.util.ArrayList;

public class Next extends BaseServerMessage {

    public Next(int pPlayerId, ArrayList<Integer> pAvailableCards, int pMaxCards) {
        mPlayerId = pPlayerId;
        mAvailableCards = pAvailableCards;
        mMaxCards = pMaxCards;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    private static final long serialVersionUID = 15L;

    int mPlayerId;
    ArrayList<Integer> mAvailableCards;
    int mMaxCards;
}
