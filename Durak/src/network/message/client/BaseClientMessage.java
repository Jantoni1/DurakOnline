package network.message.client;


import controller.Visitor;

import java.io.Serializable;

public class BaseClientMessage implements Serializable {

    public BaseClientMessage() {}

    void accept(Visitor visitor) {}

    private static final long serialVersionUID = 1L;

}
