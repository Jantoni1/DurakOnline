package main.java.controller.server;


import main.java.network.message.client.*;
import main.java.network.server.ClientThread;

public abstract class BaseServerVisitor {

    public abstract void visit(BaseClientMessage baseClientMessage);

    public abstract void visit(ClientThread pClientThread, BaseClientMessage baseClientMessage);

}
