package main.java.network.message.server;

import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;



public class Welcome extends BaseServerMessage {

    public Welcome(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(ClientConnectionVisitor pClientVisitor) {
        pClientVisitor.visit(this);
    }

    public void accept(ClientGameplayVisitor pClientGameplayVisitor) {pClientGameplayVisitor.visit(this); }

    private static final long serialVersionUID = 19L;

    public int getPlayerId() {
        return playerId;
    }

    public String toString() {
        return "class welcome instance";
    }

    private int playerId;

}
