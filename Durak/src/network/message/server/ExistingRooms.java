package network.message.server;


import controller.Visitor;
import model.Pair;

import java.util.ArrayList;

public class ExistingRooms extends BaseServerMessage {

    public ExistingRooms(ArrayList<Pair> existingRooms) {

        this.existingRooms = existingRooms;
    }

    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 18L;

    public ArrayList<Pair> existingRooms;
}
