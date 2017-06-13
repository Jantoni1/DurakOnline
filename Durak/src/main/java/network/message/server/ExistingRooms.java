package main.java.network.message.server;


import main.java.model.server.RoomInfo;
import main.java.controller.Visitor;
import main.java.network.message.Message;

import java.util.ArrayList;

public class ExistingRooms extends Message {

    public ExistingRooms(ArrayList<RoomInfo> existingRooms) {

        this.existingRooms = existingRooms;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "ExistingRooms " + existingRooms.size();
    }

    private static final long serialVersionUID = 18L;

    public ArrayList<RoomInfo> existingRooms;
}
