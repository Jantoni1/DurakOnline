package network.message.client;


import controller.Visitor;

public class Add extends BaseClientMessage {


    public Add(int roomId) {
        this.roomId = roomId;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 2L;

    public int roomId;
}
