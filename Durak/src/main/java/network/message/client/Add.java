package main.java.network.message.client;


import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.server.ClientThread;

public class Add extends Message {


    public Add(int roomId) {
        this.roomId = roomId;
    }

    public void accept(ClientThread pClientThread, Visitor visitor) { visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 2L;

    public int getRoomId() {
        return roomId;
    }

    private final int roomId;
}
