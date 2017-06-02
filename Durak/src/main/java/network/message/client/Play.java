package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.network.server.ClientThread;
import main.java.controller.server.RoomVisitor;

import java.util.ArrayList;

public class Play extends BaseClientMessage {


    @Override
    public void accept(BaseServerVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientThread pClientThread, BaseServerVisitor visitor) { visitor.visit(pClientThread, this);}

    public void accept(ClientThread pClientThread, RoomVisitor visitor) {
        visitor.visit( pClientThread, this);
    }

    private static final long serialVersionUID = 7L;

    public ArrayList<Integer> getCardNumber() {
        return cardNumber;
    }

    public Play(ArrayList<Integer> cardNumber) {

        this.cardNumber = cardNumber;
    }

    private ArrayList<Integer> cardNumber;

}
