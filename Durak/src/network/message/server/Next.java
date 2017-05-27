package network.message.server;


import controller.Visitor;

public class Next extends BaseServerMessage {

    public Next(int playerId) {
        this.playerId = playerId;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 15L;

    int playerId;
}
