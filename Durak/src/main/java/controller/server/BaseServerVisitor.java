package main.java.controller.server;


import main.java.network.message.client.*;
import main.java.network.server.ClientThread;

public   class BaseServerVisitor {

    public  void visit(ClientThread pClientThread, BaseClientMessage baseClientMessage) {}

    public void visit(ClientThread pClientThread, Add pAdd) {}

    public void visit(ClientThread pClientThread, Chat pChat) {}

    public   void visit(ClientThread pClientThread, CreateRoom pCreateRoom) {}

    public   void visit(ClientThread pClientThread, HandShake pHandShake) {}

    public   void visit(ClientThread pClientThread, Leave pLeave) {}

    public   void visit(ClientThread pClientThread, Play pPlay) {}

    public   void visit(ClientThread pClientThread, Ready pReady) {}

}
