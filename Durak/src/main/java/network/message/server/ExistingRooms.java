package main.java.network.message.server;


import main.java.model.RoomInfo;
import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;

import java.util.ArrayList;

public class ExistingRooms extends BaseServerMessage {

    public ExistingRooms(ArrayList<RoomInfo> existingRooms) {

        this.existingRooms = existingRooms;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "ExistingRooms " + existingRooms.size();
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    public void accept(ClientGameplayVisitor pGameplayVisitor) {
        pGameplayVisitor.visit(this); }

    private static final long serialVersionUID = 18L;

    public ArrayList<RoomInfo> existingRooms;
}
