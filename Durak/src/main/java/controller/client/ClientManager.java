package main.java.controller.client;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.model.client.PlayerData;
import main.java.network.client.ClientConnection;
import main.java.network.message.client.*;
import main.java.network.message.server.ExistingRooms;
import main.java.network.message.server.RoomUpdate;
import main.java.view.LobbyScene;
import main.java.view.LoginScene;
import main.java.view.RoomScene;

import java.io.IOException;

/**
 * Created by Kuba on 30.05.2017.
 */
public class ClientManager {

    private ClientConnection mClient;
    private Stage mStage;
    private LobbyScene mLobbyScene;
    private LoginScene mLoginScene;
    private RoomScene mRoomScene;
    private ClientConnectionVisitor mClientConnectionVisitor;
    private ClientRoomVisitor mClientRoomVisitor;
    private PlayerData mPlayerDataData;

    public ClientManager(Stage pStage, ClientConnection pClient) throws IOException {
        mStage = pStage;
        mStage.setOnCloseRequest(event -> Platform.exit());
        mLoginScene = new LoginScene(this);
        mLobbyScene = new LobbyScene(this);
        initializeClient(pClient);
        setClientThreadShutdownWhenWindowIsClosed(pStage);
    }

    private void setClientThreadShutdownWhenWindowIsClosed(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                mClient.disconnect();
            }
        });
    }

    private void initializeClient(ClientConnection pClient) throws IOException {
            mClient = pClient;
            mClientConnectionVisitor = new ClientConnectionVisitor(mClient, this, mLobbyScene, mLoginScene);
            mClientRoomVisitor = new ClientRoomVisitor(mClient, this, mLobbyScene, mLoginScene);
            mClient.registerListener(mClientConnectionVisitor);
            mClient.registerListener(mClientRoomVisitor);
            mClient.start();
            showloginScene();
    }

    public void setPlayerData(PlayerData pPlayerDataData) {
        mPlayerDataData = pPlayerDataData;
    }

    public PlayerData getPlayerData() {
        return mPlayerDataData;
    }

//    public void setManager(Stage pStage) {
//        if(mStage == null) {
//            mStage = pStage;
//            mStage.setOnCloseRequest(event -> Platform.exit());
//        }
//    }

    public void showloginScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mStage.setScene(mLoginScene.getLoginScene());
                mStage.show();
            }
        });
    }

    public void sendChatMessage(String pChatMessage) {
        mClient.sendMessage(new Chat(pChatMessage));
    }


    public void sendHandshakeMessage(String pPlayersNick) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mClient.sendMessage(new HandShake(pPlayersNick));
            }
        });
    }

    public void sendCreateRoomMessage(String pRoomName, int pMaxPlayers) {
        mClient.sendMessage(new CreateRoom(pRoomName, pMaxPlayers));
    }

    public void sendAddMessage(int pRoomID) {
        mClient.sendMessage(new Add(pRoomID));
    }

    public void showLobbyScene(ExistingRooms pExistingRooms) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mLobbyScene.createRoomList(pExistingRooms.existingRooms);
                mStage.setScene(mLobbyScene.getLobbyScene());
//                mStage.show();
            }
        });
    }

    public void showScene(Scene pScene) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mStage.setScene(pScene);
                mStage.show();
            }
        });
    }

    public void updateView(RoomUpdate pRoomUpdate) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mLobbyScene.updateRooms(pRoomUpdate);
                mStage.setScene(mLobbyScene.getLobbyScene());
                //mStage.setScene();
//                mStage.show();
            }
        });
    }
//    public void gameScene() {
////        mMainStage.setScene(new RoomScene(pGame).getScene());
//    }
//    public interface PlayerData {
//        public int getPositionOnTable()
//    }

}
