package network.message.client;


import controller.Visitor;

public class HandShake extends BaseClientMessage{


    void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private static final long serialVersionUID = 8L;

    public String getUserName() {
        return userName;
    }

    private String userName;
}
