package network.message.server;


import controller.Visitor;

public class Leave extends BaseServerMessage {

    public Leave(int playerId) {
        this.playerId = playerId;
    }

    private static final long serialVersionUID = 12L;

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int playerId;
}
