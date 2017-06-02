package main.java.network.message.client;


import main.java.controller.server.BaseServerVisitor;
import main.java.controller.server.LobbyVisitor;
import main.java.controller.server.RoomVisitor;
import main.java.controller.server.ServerVisitor;
import main.java.network.server.*;

import java.io.Serializable;

public abstract class BaseClientMessage implements Serializable {

    public BaseClientMessage() {}

    abstract public void accept(BaseServerVisitor visitor);

    abstract public void accept(ClientThread pClientThread, BaseServerVisitor visitor);

    public void accept(ClientThread pClientThread, ServerVisitor visitor) {visitor.visit(pClientThread, this);}

    public void accept(ClientThread pClientThread, LobbyVisitor visitor) {visitor.visit(pClientThread, this);}

    public void accept(ClientThread pClientThread, RoomVisitor visitor) {visitor.visit(pClientThread, this);}

    private static final long serialVersionUID = 1L;
}
