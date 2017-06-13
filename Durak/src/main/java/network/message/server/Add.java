package main.java.network.message.server;

import main.java.controller.Visitor;
import main.java.model.client.AnotherPlayer;
import main.java.network.message.Message;


public class Add extends Message {

    public Add(String pUserName, int pUserId) {
        mAnotherPlayer = new AnotherPlayer(pUserName, pUserId);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 20L;

    public AnotherPlayer getmAnotherPlayer() {
        return mAnotherPlayer;
    }

    private final AnotherPlayer mAnotherPlayer;
}
