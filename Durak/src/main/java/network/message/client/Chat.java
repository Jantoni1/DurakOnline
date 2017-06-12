package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;
import main.java.controller.server.RoomVisitor;

public class Chat extends BaseClientMessage {

    public Chat(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) { visitor.visit(pClientThread, this);}

    public String getChatMessage() {
        return chatMessage;
    }

    private static final long serialVersionUID = 3L;

    private String chatMessage;

}
