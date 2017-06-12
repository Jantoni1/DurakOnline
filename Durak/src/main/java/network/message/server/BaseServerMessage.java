package main.java.network.message.server;


import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;

import java.io.Serializable;

public abstract class BaseServerMessage implements Serializable {

    abstract public void accept(BaseClientVisitor visitor);

}
