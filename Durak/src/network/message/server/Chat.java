package network.message.server;


import controller.Visitor;

public class Chat extends BaseServerMessage {

    public Chat(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 16L;

    public String chatMessage;
}
