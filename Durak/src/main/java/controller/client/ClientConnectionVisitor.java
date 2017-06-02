package main.java.controller.client;

import main.java.network.client.Client;
import main.java.network.message.server.Welcome;
import main.java.network.message.server.BaseServerMessage;


public class ClientConnectionVisitor extends BaseClientVisitor implements Client.MessageListener {

    public void visit(BaseServerMessage pBaseServerMessage) {}

    public void visit(Welcome pWelcome) {
        System.out.println("HALKO 3");

//        mClient.setUserData("Player" + pWelcome.getPlayerId(), pWelcome.getPlayerId() );
//        mClient.unregisterListener(this);
//       // ClientManager.getInstance().lobbyScene();
    }

    @Override
    public void onClientMessage(BaseServerMessage pServerMessage) {
        pServerMessage.accept(this);
    }

    public ClientConnectionVisitor(Client pClient) {
        mClient = pClient;
    }

    private Client mClient;
}
