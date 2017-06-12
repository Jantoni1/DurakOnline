package main.java.view;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.java.controller.client.ClientManager;
import main.java.model.client.AnotherPlayer;
import main.java.model.server.Card;
import main.java.model.server.RoomInfo;
import main.java.network.message.server.Chat;

import java.util.ArrayList;
import java.util.Observable;

public class RoomScene {
    private ClientManager mClientManager;
    private Scene mRoomScene;
    private AnchorPane mRoot;
    private PlayersLayout mLeftPlayer;
    private PlayersLayout mRightPlayer;
    private PlayersLayout mTopPlayer;
    private PlayersLayout mBottomPlayer;
    private ArrayList<PlayersLayout> mOtherPlayers;
    private static double epsilon = 0.0001;
    private ReadyPanel mReadyPanel;
    private final int mMaxNumberOfPlayers;
    private CardsOnTable mCardsOnTable;
    private Button mPass;
    private ImageView mTrumpCard;

    public RoomScene(int pNumberOfPlayers, ClientManager pClientManager) {
        mMaxNumberOfPlayers = pNumberOfPlayers;
        mClientManager = pClientManager;
        mRoomScene = new Scene(createSceneRoot(), 1200, 800, Color.AZURE);
        createPassButton();
        createOtherPlayers();
        configurePlayersView();
        createReadyPanel();
        createCardsOnTable();
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
                mClientManager.pass();
            }
        });
    }

    private void createCardsOnTable() {
        mCardsOnTable = new CardsOnTable();
        mRoot.getChildren().add(mCardsOnTable);
        AnchorPane.setTopAnchor(mCardsOnTable, 300.0);
        AnchorPane.setLeftAnchor(mCardsOnTable, 180.0);
    }

    public synchronized void updateOtherPlayersViewProperty(AnotherPlayer pAnotherPlayer, boolean pFirstAttack) {
        mOtherPlayers.get(pAnotherPlayer.getmPositionOnTable()).updateView(pAnotherPlayer);
        if(pAnotherPlayer.getmPositionOnTable() == 0) {
            showPassButton(pAnotherPlayer.ismIsMyTurn() && !pFirstAttack);
        }
    }

    private void showPassButton(boolean trueIfMyTurn) {
        if(trueIfMyTurn) {
            mPass.setVisible(true);
        }
        else
            mPass.setVisible(false);
    }



    public void resetPlayersViewProperty(ArrayList<AnotherPlayer> pOtherPlayers, boolean pFirstAttack) {
        mOtherPlayers.forEach(player -> player.updateView(null));
        pOtherPlayers.forEach(player -> updateOtherPlayersViewProperty(player, pFirstAttack));
    }


    public void updateReadyPlayersProperty(boolean pTrueIfReadyFalseIfUnready) {
        mReadyPanel.playerReady(pTrueIfReadyFalseIfUnready);
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
            AnchorPane.setRightAnchor(mRightPlayer, (-1)*mRightPlayer.getmSpacing()/2+480.0);
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
        if(Math.abs(mRightPlayer.getmSpacing() - 0.0) < epsilon) {
            AnchorPane.setRightAnchor(mRightPlayer, 20.0);
        }
        else {
            AnchorPane.setRightAnchor(mRightPlayer, (-1)*mRightPlayer.getmSpacing()/2+80.0);
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
        mLeftPlayer = new PlayersLayout(-90.0, mClientManager);
        mRightPlayer = new PlayersLayout(90.0, mClientManager);
        mTopPlayer = new PlayersLayout(0.0, mClientManager);
        mBottomPlayer = new PlayersLayout(0.0, mClientManager);
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
        mReadyPanel = new ReadyPanel(mClientManager, mOtherPlayers.size());
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
                mClientManager.leaveGame();
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

}
