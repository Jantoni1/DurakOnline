package main.java.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.java.model.client.Player;
import main.java.model.server.Card;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Leave;
import main.java.network.message.client.Play;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomScene {
    private MessageBox mMessageBox;
    private Scene mRoomScene;
    private AnchorPane mRoot;
    private PlayersLayout mLeftPlayer;
    private PlayersLayout mRightPlayer;
    private PlayersLayout mTopPlayer;
    private PlayersLayout mBottomPlayer;
    private double epsilon = 0.0001;
    private ArrayList<PlayersLayout> mOtherPlayers;
    private ReadyPanel mReadyPanel;
    private final int mMaxNumberOfPlayers;
    private CardsOnTable mCardsOnTable;
    private Button mPass;
    private ImageView mTrumpCard;
    private GameOverPanel mGameOverPanel;
    private Model mModel;

    public RoomScene(Model pModel, int pNumberOfPlayers, MessageBox pMessageBox) {
        mModel = pModel;
        mMaxNumberOfPlayers = pNumberOfPlayers;
        mMessageBox = pMessageBox;
        mRoomScene = new Scene(createSceneRoot(), 1200, 800, Color.AZURE);
        createRoomSceneComponents();
    }

    private void createRoomSceneComponents() {
        createPassButton();
        createOtherPlayers();
        configurePlayersView();
        createReadyPanel();
        createCardsOnTable();
        createGameOverPanel();
    }

    private void createGameOverPanel() {
        mGameOverPanel = new GameOverPanel();
        mRoot.getChildren().add(mGameOverPanel);
        AnchorPane.setLeftAnchor(mGameOverPanel, 200.0);
        AnchorPane.setTopAnchor(mGameOverPanel, 275.0);
    }

    public void showEndGamePanel(String pPlayerNick) {
        mGameOverPanel.createLabelAndButton(pPlayerNick);

    }

    public void setTrumpCard(Card pCard) {
        mTrumpCard = new ImageView("main/resources/images/"+ pCard.mFigure.getFigure() + "_" + pCard.mSuit.getColor() + ".png");
        mRoot.getChildren().add(mTrumpCard);
        createmTrumpCard();
    }

    public void hideTrumpCard() {
        mTrumpCard.setVisible(false);
    }

    private void createmTrumpCard() {
        AnchorPane.setBottomAnchor(mTrumpCard, 20.0);
        AnchorPane.setRightAnchor(mTrumpCard, 120.0);
        mTrumpCard.setVisible(true);

    }

    private void createPassButton() {
        mPass = new Button("PASS");
        mRoot.getChildren().add(mPass);
        setPassButtonProperties();
        setPasButtonAction();
    }

    private void setPassButtonProperties() {
        AnchorPane.setBottomAnchor(mPass, 220.0);
        AnchorPane.setLeftAnchor(mPass, 330.0);
        mPass.setVisible(false);
        setPassButtonStyle();
    }

    private void setPassButtonStyle() {
        mPass.setStyle("-fx-background-radius: 8px; -fx-font: 20 Roboto; -fx-base: #768aa5; -fx-font-size: 30px;");
        mPass.setPrefWidth(140);
        mPass.setPrefHeight(60);
    }

    private void setPasButtonAction() {
        mPass.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                mMessageBox.sendMessage(new Play(-1));
            }
        });
    }

    private void createCardsOnTable() {
        mCardsOnTable = new CardsOnTable();
        mRoot.getChildren().add(mCardsOnTable);
        AnchorPane.setTopAnchor(mCardsOnTable, 300.0);
        AnchorPane.setLeftAnchor(mCardsOnTable, 180.0);
    }

    public synchronized void updateOtherPlayersViewProperty(Player pPlayer, boolean pFirstAttack) {
        mOtherPlayers.get(pPlayer.getmPositionOnTable()).updateView(pPlayer);
        if(pPlayer.getmPositionOnTable() == 0) {
            showPassButton(pPlayer.ismIsMyTurn() && !pFirstAttack);
        }
        setPlayersViewProperty();
    }

    private void showPassButton(boolean trueIfMyTurn) {
        if(trueIfMyTurn) {
            mPass.setVisible(true);
        }
        else
            mPass.setVisible(false);
    }



    public void resetPlayersViewProperty(CopyOnWriteArrayList<Player> pOtherPlayers, boolean pFirstAttack) {
        mOtherPlayers.forEach(player -> player.updateView(null));
        pOtherPlayers.forEach(player -> updateOtherPlayersViewProperty(player, pFirstAttack));
    }


    public void updateReadyPlayersProperty(boolean pTrueIfReadyFalseIfUnready) {
        mReadyPanel.playerReady(pTrueIfReadyFalseIfUnready);
        setPlayersViewProperty();
    }

    public void hideActivePanel() {
        mReadyPanel.reset();

    }

    public void activateReadyPanel(int pCurrentNumberOfPlayers) {
        if(pCurrentNumberOfPlayers == mMaxNumberOfPlayers) {
            mReadyPanel.activate();
        }
        else {
            mReadyPanel.reset();
        }
    }

    public void updateCardsOnTable(ArrayList<Card> pAttackingCards, ArrayList<Card> pDefendingCards) {
        mCardsOnTable.updateCardsOnTable(pAttackingCards, pDefendingCards);
    }

    private void configurePlayersView() {
        setNumberOfPlayersToShow();
        setActivePlayers();
        setPlayersViewProperty();
        addPlayersToRoot();
    }

    private void addPlayersToRoot() {
        mOtherPlayers.forEach(player -> mRoot.getChildren().add(player));
    }

    private void setNumberOfPlayersToShow() {
        mOtherPlayers.add(mBottomPlayer);
        if(mMaxNumberOfPlayers != 2) {
            mOtherPlayers.add(mLeftPlayer);
        }
        if(mMaxNumberOfPlayers != 3) {
            mOtherPlayers.add(mTopPlayer);
        }
        if(mMaxNumberOfPlayers != 2) {
            mOtherPlayers.add(mRightPlayer);
        }
    }

    private void setPlayersViewProperty() {
       setBottomPlayerViewProperty();
       setLeftPlayerViewProperty();
       setTopPlayerViewProperty();
       setRightPlayerViewProperty();
    }

    private void setActivePlayers() {
        for(PlayersLayout player : mOtherPlayers) {
            player.setUsed(true);
        }
    }

    private void setRightPlayerViewProperty() {
        if(mRightPlayer.isUsed()) {
            AnchorPane.setTopAnchor(mRightPlayer, 400.0);
            setRightPlayerSpacing();
            AnchorPane.setBottomAnchor(mRightPlayer, 400.0);
        }
    }

    private void setRightPlayerSpacing() {
        if(Math.abs(mRightPlayer.getmSpacing() - 0.0) < epsilon) {
            AnchorPane.setRightAnchor(mRightPlayer, 420.0);
        }
        else {
            AnchorPane.setRightAnchor(mRightPlayer, (-1)*mRightPlayer.getmSpacing()/2 + 500.0);
        }
    }


    private void setLeftPlayerViewProperty() {
        if(mLeftPlayer.isUsed()) {
            AnchorPane.setTopAnchor(mLeftPlayer, 400.0);
            setLeftPlayerSpacing();
            AnchorPane.setBottomAnchor(mLeftPlayer, 400.0);
        }
    }

    private void setLeftPlayerSpacing() {
        if(Math.abs(mLeftPlayer.getmSpacing() - 0.0) < epsilon) {
            AnchorPane.setLeftAnchor(mLeftPlayer, 20.0);
        }
        else {
            AnchorPane.setLeftAnchor(mLeftPlayer, (-1)*mLeftPlayer.getmSpacing()/2 + 100.0);
        }
    }

    private void setTopPlayerViewProperty() {
        if(mTopPlayer.isUsed()) {
            AnchorPane.setTopAnchor(mTopPlayer, 0.0);
            AnchorPane.setLeftAnchor(mTopPlayer, 400.0);
            AnchorPane.setRightAnchor(mTopPlayer, 800.0);
        }
    }

    private void setBottomPlayerViewProperty() {
        AnchorPane.setBottomAnchor(mBottomPlayer, 20.0);
        AnchorPane.setLeftAnchor(mBottomPlayer, 400.0);
        AnchorPane.setRightAnchor(mBottomPlayer, 800.0);
    }

    private void createOtherPlayers() {
        mOtherPlayers = new ArrayList<>();
        mLeftPlayer = new PlayersLayout(-90.0, mMessageBox, mModel);
        mRightPlayer = new PlayersLayout(90.0, mMessageBox, mModel);
        mTopPlayer = new PlayersLayout(0.0, mMessageBox, mModel);
        mBottomPlayer = new PlayersLayout(0.0, mMessageBox, mModel);
//        mRoot.getChildren().addAll(mLeftPlayer, mRightPlayer, mTopPlayer);
    }

    private AnchorPane createSceneRoot() {
        mRoot = new AnchorPane();
        mRoot.getChildren().addAll(createBackgroundImage());
        mRoot.setStyle("-fx-background-color: rgba(0, 255, 0, 0.3);");
        createChatBox();
        createLeaveButton();
        return mRoot;
    }
    private void createReadyPanel() {
        mReadyPanel = new ReadyPanel(mMessageBox, mOtherPlayers.size());
        mRoot.getChildren().add(mReadyPanel);
        setReadyPanelProperties();
    }

    private void setReadyPanelProperties() {
        AnchorPane.setTopAnchor(mReadyPanel, 200.0);
        AnchorPane.setLeftAnchor(mReadyPanel, 250.0);
    }

    private void createLeaveButton() {
        Button leaveButton = new Button("LEAVE");
        leaveButton.setStyle("-fx-background-radius: 16px; -fx-font: 16 Roboto; -fx-base: #f2f2f2; -fx-font-size: 30px;");
        setButtonAction(leaveButton);
        setLeaveButtonSize(leaveButton);
        addLeaveButtonToAnchorPane(leaveButton);
    }

    private void addLeaveButtonToAnchorPane(Button pButton) {
        mRoot.getChildren().add(pButton);
        AnchorPane.setTopAnchor(pButton, 20.0);
        AnchorPane.setRightAnchor(pButton, 50.0);
    }

    private void setLeaveButtonSize(Button pButton) {
        pButton.setMaxWidth(200);
        pButton.setPrefWidth(200);
//        pButton.setPrefHeight();
    }

    private void setButtonAction(Button pButton) {
        pButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                mMessageBox.sendMessage(new Leave(false));
            }
        });
    }

    private void createChatBox() {
        ChatBox chatBox = new ChatBox();
//        chatBox.setSendListener( new ChatBox.MessageSendListener() {
//                @Override
//                public void onMessageSend(String pMessage) {
//                    mClientManager.sendChatMessage(pMessage);
//                }
//        });

//        mRoot.getChildren().add(chatBox);
//        AnchorPane.setRightAnchor(chatBox, 10.0);
//        AnchorPane.setTopAnchor(chatBox, 50.0);
    }

    private ImageView createBackgroundImage() {
        ImageView backgroundImage = new ImageView("main/resources/background.jpg");
        backgroundImage.setVisible(true);
        return backgroundImage;
    }

    public Scene getRoomScene() {
        return mRoomScene;
    }

    public interface Model {
        ArrayList<Integer> getAvailableCards();
    }
}
