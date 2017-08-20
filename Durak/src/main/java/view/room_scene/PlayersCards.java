package main.java.view.room_scene;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import main.java.model.client.Player;
import main.java.network.client.MessageBox;
import main.java.view.Model;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kuba on 24.07.2017.
 */
public class PlayersCards extends BorderPane {

    private CardsOnTable mCardsOnTable;
    private PlayersPropertyDisplay mLeftPlayer;
    private PlayersPropertyDisplay mRightPlayer;
    private PlayersPropertyDisplay mTopPlayer;
    private PlayersPropertyDisplay mBottomPlayer;
    private ArrayList<PlayersPropertyDisplay> mPlayers;
    private Model mModel;
    private PassButton mPass;
    private MessageBox mMessageBox;
    private int mNumberOfPlayers;
    private ReadyPanel mReadyPanel;



    public PlayersCards(Model pModel, int pNumberOfPlayers, MessageBox pMessageBox, double width, double height) {
        mMessageBox = pMessageBox;
        mModel = pModel;
        mNumberOfPlayers = pNumberOfPlayers;
        setPickOnBounds(false);
        createReadyPanel();
        mPass = new PassButton(mMessageBox);
        createPlayersDisplay(pModel, pMessageBox, width, height);
        addPlayers();
//        getChildren().addAll(mCardsOnTable, mTopPlayer, mBottomPlayer, mLeftPlayer, mRightPlayer);
        setComponentsOnPane();
        nodesSettings(width, height);
    }

    private void createPlayersDisplay(Model pModel, MessageBox pMessageBox, double width, double height) {
        mCardsOnTable = new CardsOnTable(pModel.getAttackingCards(), pModel.getDefendingCards());
        mBottomPlayer = new PlayersPropertyDisplay(0.0, pModel.getAvailableCards(),  pMessageBox, true, width + 100.0, height);
        mLeftPlayer = new PlayersPropertyDisplay(-90.0, pModel.getAvailableCards(),  pMessageBox, false, width, height);
        mRightPlayer = new PlayersPropertyDisplay(90.0, pModel.getAvailableCards(),  pMessageBox, false, width, height);
        mTopPlayer = new PlayersPropertyDisplay(0.0, pModel.getAvailableCards(),  pMessageBox, false, width + 100.0, height);
    }

    private void setComponentsOnPane() {
        setTop(mTopPlayer);
        setBottom(mBottomPlayer);
        setLeft(mLeftPlayer);
        setRight(mRightPlayer);
//        setCenter(mCardsOnTable);
        BorderPane borderPane = new BorderPane(mCardsOnTable, null, null, mPass, null);
        StackPane stackPane = new StackPane(borderPane, mReadyPanel);
        mReadyPanel.setAlignment(Pos.TOP_CENTER);
        setMargin(stackPane, new Insets(160, -170, 10, -170));
        setCenter(stackPane);
        setAlignment(mPass, Pos.BOTTOM_CENTER);
    }

    private void nodesSettings(double width, double height) {
        setPrefWidth(width + height + 600.0);
        setPrefHeight(width + height);
        setPickOnBounds(false);
//        setMargin(mCardsOnTable, new Insets(160, -170, 0, -170));
    }

    private void createReadyPanel() {
        mReadyPanel = new ReadyPanel(mMessageBox, mNumberOfPlayers);
    }

    public void updateCardsOnTable() {
        mCardsOnTable.updateCardsOnTable();
        mPlayers.forEach(player -> player.updatePlayersCards());
    }

    public void resetPlayersViewProperty(CopyOnWriteArrayList<Player> pOtherPlayers, boolean pFirstAttack) {
//        mOtherPlayers.forEach(player -> player.updateView(null));
//        pOtherPlayers.forEach(player -> updateOtherPlayersViewProperty(player, pFirstAttack));
    }

    public void updateOnePlayerCards(int pPlayerID) {
        mModel.getPlayers().forEach(player -> {
            if(player.getUserID() == pPlayerID) {
                mPlayers.get(player.getPositionOnTable()).updatePlayersCards();
            }
        });
    }

    public void setPassButton(int pPlayerID) {
        mPass.setVisible(!mModel.getAttackingCards().isEmpty() && mModel.getMe().getUserID() == pPlayerID);
    }

    public void updatePlayersCards() {
        mPlayers.forEach(player -> player.updatePlayersCards());
    }

    public void updatePlayers() {
        mModel.getPlayers().forEach(player -> {
            mPlayers.get(player.getPositionOnTable()).setPlayer(player);
        });
    }

    public void setPlayer(int pPlayerID) {
        mModel.getPlayers().forEach(player -> {
            if(player.getUserID() == pPlayerID) {
                mPlayers.get(player.getPositionOnTable()).setPlayer(player);
            }
        });
    }

    private void addPlayers() {
        mPlayers = new ArrayList<>(mNumberOfPlayers);
        for(int i = 0; i < mNumberOfPlayers; ++i) {
            mPlayers.add(null);
        }
        mPlayers.set(0, mBottomPlayer);
        if(mNumberOfPlayers == 2) {
            mPlayers.set(1, mTopPlayer);
        }
        if(mNumberOfPlayers == 3) {
            mPlayers.set(1, mLeftPlayer);
            mPlayers.set(2, mRightPlayer);
        }
        if(mNumberOfPlayers == 4) {
            mPlayers.set(1, mLeftPlayer);
            mPlayers.set(2, mTopPlayer);
            mPlayers.set(3, mRightPlayer);
        }
        updatePlayers();
    }

    public void updateReadyPlayersProperty(boolean pTrueIfReadyFalseIfUnready) {
        mReadyPanel.playerReady(pTrueIfReadyFalseIfUnready);
//        setPlayersViewProperty();
    }

    public void hideActivePanel() {
        mReadyPanel.reset();

    }

    public void activateReadyPanel() {
        int counter = 0;
        for(Player player : mModel.getPlayers()) {
            if(player.getUserID() != -1) {
                ++counter;
            }
        }
        if(counter == mNumberOfPlayers) {
            mReadyPanel.activate();
        }
        else {
            mReadyPanel.reset();
        }
    }

    public void resetView() {
        mModel.reset();
        mPlayers.forEach(player -> {
            player.updatePlayersCards();
        });
        mCardsOnTable.updateCardsOnTable();
        mPass.setVisible(false);
        if(mModel.getNumberOfPlayersInRoom() == mNumberOfPlayers) {
            mReadyPanel.reset();
            mReadyPanel.setVisible(true);
        }
    }
}
