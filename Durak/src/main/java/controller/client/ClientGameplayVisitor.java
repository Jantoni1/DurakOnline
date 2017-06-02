package main.java.controller.client;

import main.java.model.RoomInfo;
import main.java.network.client.Client;
import main.java.network.message.client.Add;
import main.java.network.message.client.CreateRoom;
import main.java.network.message.client.HandShake;
import main.java.network.message.server.*;
import main.java.view.ClientManager;
import main.java.view.LobbyScene;
import main.java.view.LoginScene;

/**
 * Created by Kuba on 28.05.2017.
 */
public class ClientGameplayVisitor extends BaseClientVisitor implements Client.MessageListener {

    public void visit(BaseServerMessage pBaseServerMessage) {
    }

    public void visit(Enter pEnter) {

    }

    public void visit(ExistingRooms pExistingRooms) {
        mClientManager.showLobbyScene(pExistingRooms);
        try {
            RoomInfo roomInfo = mLobbyScene.waitForLobbyAction();
            if(roomInfo.getmRoomId() != -1) {
                mClient.sendMessage((new Add(roomInfo.getmRoomId())));
            }
            else {
                mClient.sendMessage(new CreateRoom("NowyPokoj", 2));
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void visit(Welcome pWelcome) {
        mClient.setUserData("Player" + pWelcome.getPlayerId(), pWelcome.getPlayerId() );
        handshake("Player" + pWelcome.getPlayerId(), pWelcome.getPlayerId() );
//        mClient.unregisterListener(this);
        // ClientManager.getInstance().lobbyScene();
    }

    public void visit(RoomUpdate pRoomUpdate) {
        mClientManager.updateView(pRoomUpdate);
        System.out.println("CLIENT GAMEPLAY VISITOR VISIT ROOMUPDATE ELKO XDXD");
    }

    public void handshake(String playerNick, int playerId) {
        mLoginScene.sendLogin();
        try {
            System.out.println("HALKO 1");
            mClient.sendMessage(new HandShake(mLoginScene.getmUsername()));
            System.out.println("HALKO 2");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClientMessage(BaseServerMessage pServerMessage) {
        pServerMessage.accept(this);
    }

//    public void onClientMessage(Enter pEnter) {
//
//    }
//
//    public void onClientMessage(ExistingRooms pExistingRooms) {
//    }

    public ClientGameplayVisitor(Client pClient, ClientManager pClientManager, LobbyScene pLobbyScene, LoginScene pLoginScene) {
        mClient = pClient;
        mClientManager = pClientManager;
        mLobbyScene = pLobbyScene;
        mLoginScene = pLoginScene;
    }

    private final Client mClient;
    private final ClientManager mClientManager;
    private final LobbyScene mLobbyScene;
    private final LoginScene mLoginScene;
//    private final Lobby mLobby;
}
