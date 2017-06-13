package main.java.network.message.client;


import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.server.ClientThread;

public class Chat extends Message {

    public Chat(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void accept(ClientThread pClientThread, Visitor visitor) { visitor.visit(pClientThread, this);}

    public String getChatMessage() {
        return chatMessage;
    }

    private static final long serialVersionUID = 3L;

    private String chatMessage;

}
