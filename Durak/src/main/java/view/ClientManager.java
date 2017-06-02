package main.java.view;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;
import main.java.model.RoomInfo;
import main.java.network.client.Client;
import main.java.network.message.client.HandShake;
import main.java.network.message.server.ExistingRooms;
import main.java.network.message.server.RoomUpdate;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import static javafx.application.Platform.exit;

/**
 * Created by Kuba on 30.05.2017.
 */
public class ClientManager {

    private Client mClient;
    private Stage mStage;
    private LobbyScene mLobbyScene;
    private LoginScene mLoginScene;
    private ClientConnectionVisitor mClientConnectionVisitor;
    private ClientGameplayVisitor mClientGameplayVisitor;

    public ClientManager(Stage pStage, String pIP, int pPort) throws IOException {
        mStage = pStage;
        mStage.setOnCloseRequest(event -> Platform.exit());
        mLoginScene = new LoginScene();
        mLobbyScene = new LobbyScene();
        initializeClient(pIP, pPort);
    }

    private void initializeClient(String pIP, int pPort) throws IOException {
            mClient = new Client(pIP, pPort);
            mClientConnectionVisitor = new ClientConnectionVisitor(mClient);
            mClientGameplayVisitor = new ClientGameplayVisitor(mClient, this, mLobbyScene, mLoginScene);
            mClient.registerListener(mClientConnectionVisitor);
            mClient.registerListener(mClientGameplayVisitor);
            mClient.start();
            showloginScene();
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

    public void showLobbyScene(ExistingRooms pExistingRooms) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mLobbyScene.createRoomList(pExistingRooms.existingRooms);
                mStage.setScene(mLobbyScene.getLobbyScene());
                mStage.show();
            }
        });
    }


    //TODO CHANGE TO REAL UPDATE MODE
    public void updateView(RoomUpdate pRoomUpdate) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mLobbyScene.updateRooms(pRoomUpdate);
                mStage.setScene(mLobbyScene.getLobbyScene());
                //mStage.setScene();
                mStage.show();
            }
        });
    }

    public void gameScene() {
//        mMainStage.setScene(new GameScene(pGame).getScene());
    }

}
