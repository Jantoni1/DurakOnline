package main.java.network.message;


import main.java.controller.Visitor;
import main.java.network.server.ClientThread;

import java.io.Serializable;

public abstract class Message implements Serializable {

    public void accept(Visitor visitor) {}

    public void accept(ClientThread pClientThread, Visitor visitor) {}
}
