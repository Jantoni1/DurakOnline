package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;



public class Enter extends BaseServerMessage {
    public Enter(String pUserName, int pServerId, int pNumberOfPlayers, boolean pIfFailed ) {
        mUserName = pUserName;
        mServerId = pServerId;
        mNumberOfPlayers = pNumberOfPlayers;
        mIfFailed = pIfFailed;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    public void accept(ClientGameplayVisitor pClientVisitor) {
        pClientVisitor.visit(this);
    }

    private static final long serialVersionUID = 21L;

    private final int mNumberOfPlayers;
    private final String mUserName;
    private final int mServerId;
    private boolean mIfFailed;
}
