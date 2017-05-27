package network.message.server;

import controller.Visitor;

/**
 * Created by Kuba on 27.05.2017.
 */
public class Welcome extends BaseServerMessage {

    public Welcome(int playerId) {
        this.playerId = playerId;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 19L;

    private int playerId;

}
