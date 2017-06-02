package main.java.controller.client;

import main.java.network.message.client.BaseClientMessage;
import main.java.network.message.server.Enter;
import main.java.network.message.server.ExistingRooms;
import main.java.network.message.server.Welcome;
import main.java.network.message.server.BaseServerMessage;
import main.java.network.server.ClientThread;

/**
 * Created by Kuba on 28.05.2017.
 */
public abstract class BaseClientVisitor {

    public abstract void visit(BaseServerMessage pBaseServerMessage);

    public void visit(Welcome pWelcome) {
    }

    public void visit(ExistingRooms pExistingRooms) {
    }

    public void visit(Enter pEnter) {
    }

    abstract public void onClientMessage(BaseServerMessage pBaseServerMessage);

    public void onClientMessage(Welcome pWelcome) {
    }
}
