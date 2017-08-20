package main.java.network.message.server;

import main.java.controller.Visitor;
import main.java.network.message.Message;


/**
 * Created by Kuba on 21.07.2017.
 */
public class Ready extends Message {

    private static final long serialVersionUID = 71L;
    private boolean mReady;

    public Ready(boolean mReady) {
        this.mReady = mReady;
    }

    public boolean isReady() {
        return mReady;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
