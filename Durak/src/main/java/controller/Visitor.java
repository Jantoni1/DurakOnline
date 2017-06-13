package main.java.controller;


import main.java.network.message.Message;
import main.java.network.message.client.*;
import main.java.network.message.client.Add;
import main.java.network.message.client.Chat;
import main.java.network.message.client.Leave;
import main.java.network.message.client.Play;
import main.java.network.message.client.Ready;
import main.java.network.message.server.*;
import main.java.network.server.ClientThread;

public abstract class Visitor {

    public  void visit(ClientThread pClientThread, Message pBaseMessage) {}

    public void visit(ClientThread pClientThread, Add pAdd) {}

    public void visit(ClientThread pClientThread, Chat pChat) {}

    public void visit(ClientThread pClientThread, CreateRoom pCreateRoom) {}

    public void visit(ClientThread pClientThread, HandShake pHandShake) {}

    public void visit(ClientThread pClientThread, Leave pLeave) {}

    public void visit(ClientThread pClientThread, Play pPlay) {}

    public void visit(ClientThread pClientThread, Ready pReady) {}

    public void visit(Welcome pWelcome) {
    }

    public void visit(ExistingRooms pExistingRooms) {
    }

    public void visit(Enter pEnter) {
    }

    public void visit(main.java.network.message.server.Add pAdd) {

    }

    public void visit(main.java.network.message.server.Chat pChat) {

    }

    public void visit(Disconnected pDisconnected) {

    }

    public void visit(End pEnd) {

    }

    public void visit(Get pGet) {

    }

    public void visit(main.java.network.message.server.Leave pLeave) {

    }

    public void visit(Next pNext) {

    }

    public void visit(NextRound pNextRound) {

    }

    public void visit(main.java.network.message.server.Play pPlay) {

    }

    public void visit(RoomUpdate pRoomUpdate) {

    }

    public void visit(Start pStart) {

    }

    public void visit(main.java.network.message.server.Ready pReady) {

    }

}
