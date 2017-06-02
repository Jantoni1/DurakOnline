package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;


public class Add extends BaseServerMessage {

    public Add(String mUserName, int pUserId) {
        this.mUserName = mUserName;
        mUserId = pUserId;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    private static final long serialVersionUID = 20L;

    private final String mUserName;
    private final int mUserId;
}
