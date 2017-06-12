package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;


public class End extends BaseServerMessage {


    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 24L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public End(int playerId) {

        this.playerId = playerId;
    }

    private int playerId;
}
