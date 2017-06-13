package main.java.controller.client;

import main.java.controller.Visitor;
import main.java.model.client.Player;
import main.java.network.client.Client;
import main.java.network.message.client.HandShake;
import main.java.network.message.server.ExistingRooms;
import main.java.network.message.Message;
import main.java.network.message.server.RoomUpdate;
import main.java.network.message.server.Welcome;
import main.java.view.LobbyScene;
import main.java.view.LoginScene;


public class ClientConnectionVisitor extends Visitor implements Client.MessageListener {

//    public void visit(Message pBaseServerMessage) {}

    public void visit(ExistingRooms pExistingRooms) {
        mClientManager.showLobbyScene(pExistingRooms);
    }

    public void visit(Welcome pWelcome) {
        mClientManager.setPlayerData(new Player("Player" + pWelcome.getPlayerId(), pWelcome.getPlayerId()));
        handshake("Player" + pWelcome.getPlayerId(), pWelcome.getPlayerId() );
    }

    public void visit(RoomUpdate pRoomUpdate) {
        mClientManager.updateView(pRoomUpdate);
    }

    public void handshake(String playerNick, int playerId) {
        mLoginScene.sendLogin();
        try {
            mClientManager.getPlayerData().setmUserName(mLoginScene.getmUsername());
            mClient.sendMessage(new HandShake(mClientManager.getPlayerData().getmUserName()));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClientMessage(Message pServerMessage) {
        pServerMessage.accept(this);
    }

    public ClientConnectionVisitor(Client pClient, ClientManager pClientManager, LobbyScene pLobbyScene, LoginScene pLoginScene) {
        mClient = pClient;
        mClientManager = pClientManager;
        mLobbyScene = pLobbyScene;
        mLoginScene = pLoginScene;
    }

    private Player mPlayerInfo;
    private final Client mClient;
    private final ClientManager mClientManager;
    private final LobbyScene mLobbyScene;
    private final LoginScene mLoginScene;
}
