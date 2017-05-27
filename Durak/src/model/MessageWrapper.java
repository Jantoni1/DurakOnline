package model;

import network.message.server.BaseServerMessage;

import java.util.ArrayList;

public class MessageWrapper {

    public MessageWrapper(BaseServerMessage baseServerMessage, ArrayList<Integer> playerIdArrayList) {
        this.baseServerMessage = baseServerMessage;
        this.playerIdArrayList = playerIdArrayList;
    }

    BaseServerMessage baseServerMessage;
    ArrayList<Integer> playerIdArrayList;
}
