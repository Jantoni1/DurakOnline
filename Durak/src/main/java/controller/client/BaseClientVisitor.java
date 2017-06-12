package main.java.controller.client;

import main.java.network.message.client.BaseClientMessage;
import main.java.network.message.server.*;
import main.java.network.server.ClientThread;


public abstract class BaseClientVisitor {

//    public abstract void visit(BaseServerMessage pBaseServerMessage);

     public void visit(Welcome pWelcome) {
     }
//
    public void visit(ExistingRooms pExistingRooms) {
    }
//
    public void visit(Enter pEnter) {
    }

    public void visit(Add pAdd) {

    }

    public void visit(Chat pChat) {

    }

    public void visit(Disconnected pDisconnected) {

    }

    public void visit(End pEnd) {

    }

    public void visit(Get pGet) {

    }

    public void visit(Leave pLeave) {

    }

    public void visit(Next pNext) {

    }

    public void visit(NextRound pNextRound) {

    }

    public void visit(Play pPlay) {

    }

    public void visit(RoomUpdate pRoomUpdate) {

    }

    public void visit(Start pStart) {

    }

    public void visit(Ready pReady) {

    }
    abstract public void onClientMessage(BaseServerMessage pBaseServerMessage);

    public void onClientMessage(Welcome pWelcome) {
    }
}
