package controller;


import network.message.client.BaseClientMessage;
import network.message.server.BaseServerMessage;

public class Visitor {

    public BaseServerMessage visit(BaseClientMessage baseClientMessage) {
        return new BaseServerMessage();
    }

    /**
     *
     * @param baseServerMessage
     * @return
     */
    public BaseClientMessage visit(BaseServerMessage baseServerMessage) {
        return new BaseClientMessage();
    }
}
