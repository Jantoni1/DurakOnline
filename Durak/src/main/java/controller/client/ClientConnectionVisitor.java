package main.java.controller.client;

import main.java.controller.Visitor;
import main.java.model.client.PlayerData;
import main.java.network.client.Client;
import main.java.network.client.ClientConnection;
import main.java.network.message.server.ExistingRooms;
import main.java.network.message.Message;
import main.java.network.message.server.RoomUpdate;
import main.java.network.message.server.Welcome;
import main.java.view.lobby_scene.LobbyScene;
import main.java.view.LoginScene;


public class ClientConnectionVisitor extends Visitor implements Client.MessageListener {

//    public void visit(Message pBaseServerMessage) {}

    public void visit(ExistingRooms pExistingRooms) {
        mClientManager.showLobbyScene(pExistingRooms);
    }

    public void visit(Welcome pWelcome) {
        mClientManager.setPlayerData(new PlayerData("PlayerData" + pWelcome.getPlayerId(), pWelcome.getPlayerId()));
    }

    public void visit(RoomUpdate pRoomUpdate) {
        mClientManager.updateView(pRoomUpdate);
    }

    @Override
    public void onClientMessage(Message pServerMessage) {
        pServerMessage.accept(this);
    }

    public ClientConnectionVisitor(ClientConnection pClient, ClientManager pClientManager, LobbyScene pLobbyScene, LoginScene pLoginScene) {
        mClient = pClient;
        mClientManager = pClientManager;
        mLobbyScene = pLobbyScene;
        mLoginScene = pLoginScene;
    }

    private PlayerData mPlayerDataInfo;
    private final ClientConnection mClient;
    private final ClientManager mClientManager;
    private final LobbyScene mLobbyScene;
    private final LoginScene mLoginScene;
}
