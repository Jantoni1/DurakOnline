package main.java.controller.client;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.controller.client.ClientConnectionVisitor;
import main.java.controller.client.ClientGameplayVisitor;
import main.java.model.client.AnotherPlayer;
import main.java.model.client.Player;
import main.java.model.server.Card;
import main.java.network.client.Client;
import main.java.network.message.client.*;
import main.java.network.message.server.Enter;
import main.java.network.message.server.ExistingRooms;
import main.java.network.message.server.RoomUpdate;
import main.java.view.LobbyScene;
import main.java.view.LoginScene;
import main.java.view.RoomScene;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kuba on 30.05.2017.
 */
public class ClientManager {

    private Client mClient;
    private Stage mStage;
    private LobbyScene mLobbyScene;
    private LoginScene mLoginScene;
    private RoomScene mRoomScene;
    private ClientConnectionVisitor mClientConnectionVisitor;
    private ClientGameplayVisitor mClientGameplayVisitor;
    private Player mPlayerData;

    public ClientManager(Stage pStage, String pIP, int pPort) throws IOException {
        mStage = pStage;
        mStage.setOnCloseRequest(event -> Platform.exit());
        mLoginScene = new LoginScene();
        mLobbyScene = new LobbyScene(this);
        initializeClient(pIP, pPort);
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

    private void initializeClient(String pIP, int pPort) throws IOException {
            mClient = new Client(pIP, pPort);
            mClientConnectionVisitor = new ClientConnectionVisitor(mClient, this, mLobbyScene, mLoginScene);
            mClientGameplayVisitor = new ClientGameplayVisitor(mClient, this, mLobbyScene, mLoginScene);
            mClient.registerListener(mClientConnectionVisitor);
            mClient.registerListener(mClientGameplayVisitor);
            mClient.start();
            showloginScene();
    }

    public void setPlayerData(Player pPlayerData) {
        mPlayerData = pPlayerData;
    }

    public Player getPlayerData() {
        return mPlayerData;
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
    public void createRoomScene(int pNumberOfPlayers) {
        mRoomScene = new RoomScene(pNumberOfPlayers, this);
    }

    public void leaveGame() {
        mClient.sendMessage(new Leave(false));
    }

    public void getReady(boolean trueIfReadyFalseIfUnready) {
        mClient.sendMessage(new Ready(trueIfReadyFalseIfUnready));
    }

    public void updateRoomScene(AnotherPlayer pAnotherPlayer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateOtherPlayersViewProperty(pAnotherPlayer, false);
//                mStage.show();
            }
        });
    }

    public void sendCreateRoomMessage(String pRoomName, int pMaxPlayers) {
        mClient.sendMessage(new CreateRoom(pRoomName, pMaxPlayers));
    }

    public void sendAddMessage(int pRoomID) {
        mClient.sendMessage(new Add(pRoomID));
    }

    public void pass() {
        mClient.sendMessage(new Play(-1));
    }

    public void playACard(Card pCard) {
        int cardsIndex = mClientGameplayVisitor.getmRoom().getCardsIndex(pCard);
        if(cardsIndex != -1) {
            mClient.sendMessage(new Play(cardsIndex));
            mClientGameplayVisitor.getmRoom().removeCard(cardsIndex);
            mRoomScene.resetPlayersViewProperty(mClientGameplayVisitor.getmRoom().getmOtherPlayers(), false);
        }
    }

    public void updateCardsOnTable(ArrayList<Card> pAttackingCards, ArrayList<Card> pDefendingCards) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateCardsOnTable(pAttackingCards, pDefendingCards);
            }
        });
    }

    public void updateMultiplePlayersView(ArrayList<AnotherPlayer> pOtherPlayers, boolean pFirstAttack) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.resetPlayersViewProperty(pOtherPlayers, pFirstAttack);
//                mStage.show();
            }
        });
    }

    public void setReadyPanelVisible(int pNumberOfPlayers) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.activateReadyPanel(pNumberOfPlayers);
//                mStage.show();
            }
        });
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

    public void showRoomScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mStage.setScene(mRoomScene.getRoomScene());
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

    public void setReady(boolean pTrueForReadyFalseForUnready) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mRoomScene.updateReadyPlayersProperty(pTrueForReadyFalseForUnready);
            }
        });
    }
//    public void gameScene() {
////        mMainStage.setScene(new RoomScene(pGame).getScene());
//    }
//    public interface Player {
//        public int getmPositionOnTable()
//    }

}
