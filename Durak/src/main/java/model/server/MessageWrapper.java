package main.java.model.server;

import main.java.network.message.Message;

import java.util.ArrayList;

public class MessageWrapper {

    public MessageWrapper(Message message, ArrayList<Integer> playerIdArrayList) {
        this.message = message;
        this.playerIdArrayList = playerIdArrayList;
    }

    Message message;
    ArrayList<Integer> playerIdArrayList;
}
