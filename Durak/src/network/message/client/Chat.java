package network.message.client;


import controller.Visitor;

public class Chat extends BaseClientMessage {

    public Chat(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 3L;

    String chatMessage;

}
