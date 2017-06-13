package main.java.network.message.client;


import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.server.ClientThread;

public class Play extends Message {


    public void accept(ClientThread pClientThread, Visitor visitor) { visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 7L;


    public int getCardNumber() {
        return cardNumber;
    }

    public Play(int cardNumber) {

        this.cardNumber = cardNumber;
    }

    private int cardNumber;

}
