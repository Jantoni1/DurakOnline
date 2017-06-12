package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.model.client.AnotherPlayer;


public class Add extends BaseServerMessage {

    public Add(String pUserName, int pUserId) {
        mAnotherPlayer = new AnotherPlayer(pUserName, pUserId);
    }

    @Override
    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 20L;

    public AnotherPlayer getmAnotherPlayer() {
        return mAnotherPlayer;
    }

    private final AnotherPlayer mAnotherPlayer;
}
