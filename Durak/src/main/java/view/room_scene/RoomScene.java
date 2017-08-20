package main.java.view.room_scene;


import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.java.model.server.Card;
import main.java.network.client.MessageBox;
import main.java.view.Model;

public class RoomScene implements GameScene {
    private PlayersCards mPlayersCards;
    private MessageBox mMessageBox;
    private Scene mRoomScene;
    private AnchorPane mRoot;
    private final int mMaxNumberOfPlayers;
    private RightSidePanel mRightSidePanel;
    private Model mModel;
    private GameOverPanel mGameOverPanel;
    private final double width = 652.0;
    private final double height = 130.0;

    public RoomScene(Model pModel, int pNumberOfPlayers, MessageBox pMessageBox) {
        mModel = pModel;
        mMaxNumberOfPlayers = pNumberOfPlayers;
        mMessageBox = pMessageBox;
        mRightSidePanel = new RightSidePanel(mMessageBox);
        mPlayersCards = new PlayersCards(mModel, mMaxNumberOfPlayers, mMessageBox, width, height);
        mPlayersCards.setOnKeyPressed((event) -> mPlayersCards.requestFocus());
        mPlayersCards.setFocusTraversable(false);
        mRoomScene = new Scene(createSceneRoot(), 1200, 800, Color.AZURE);
        mRoot.getChildren().addAll(mPlayersCards, mRightSidePanel);
        AnchorPane.setTopAnchor(mPlayersCards, -9.0);
        AnchorPane.setLeftAnchor(mPlayersCards, (-9.0 -  width + height)/2 );
        AnchorPane.setRightAnchor(mRightSidePanel, 20.0);
        AnchorPane.setTopAnchor(mRightSidePanel, 50.0);
        createRoomSceneComponents();
        mRoomScene.getStylesheets().add("main/resources/room_scene.css");
    }

    private void createRoomSceneComponents() {
//        createReadyPanel();
        createGameOverPanel();
    }

    private void createGameOverPanel() {
        mGameOverPanel = new GameOverPanel(this);
        mRoot.getChildren().add(mGameOverPanel);
        AnchorPane.setLeftAnchor(mGameOverPanel, 150.0);
        AnchorPane.setTopAnchor(mGameOverPanel, 140.0);
    }

    public void showEndGamePanel(String pPlayerNick) {
        mGameOverPanel.showEndPanel(pPlayerNick);       //)createLabelAndButton(pPlayerNick);

    }

    public void setNumberOfCards(int pNumberOfCardsOnTalon) {
        mRightSidePanel.setNumberOfCards(pNumberOfCardsOnTalon);
    }

    public void setTrumpCard(Card pCard) {
        mRightSidePanel.setTrumpCard(pCard);
    }

    public void hideTrumpCard() {
        mRightSidePanel.hideTrumpCard();
    }

    public void addPlayer(int pPlayerID) {
        mPlayersCards.setPlayer(pPlayerID);
    }


    public void addChatMessage(String pAuthor, String pContent) {
        mRightSidePanel.addChatMessage(pAuthor, pContent);
    }

    public synchronized void updateOtherPlayersViewProperty(int playerID) {
        mPlayersCards.updateOnePlayerCards(playerID);
    }

    public void setPassButton(int pPlayerID) {
        mPlayersCards.setPassButton(pPlayerID);
    }


    public void resetPlayersViewProperty() {
        mPlayersCards.updatePlayersCards();
    }


    public void updateReadyPlayersProperty(boolean pTrueIfReadyFalseIfUnready) {
        mPlayersCards.updateReadyPlayersProperty(pTrueIfReadyFalseIfUnready);
    }

    public void hideActivePanel() {
        mPlayersCards.hideActivePanel();
    }

    public void activateReadyPanel() {
        mPlayersCards.activateReadyPanel();
    }

    public void updateCardsOnTable() {
        mPlayersCards.updateCardsOnTable();
    }

    public void matchPlayersWithTableSpots() {mPlayersCards.updatePlayers();}

    private AnchorPane createSceneRoot() {
        mRoot = new AnchorPane();
        mRoot.getChildren().addAll(createBackgroundImage());
        mRoot.setStyle("-fx-background-color: rgba(0, 255, 0, 0.3);");
        return mRoot;
    }

    private ImageView createBackgroundImage() {
        ImageView backgroundImage = new ImageView("main/resources/background.jpg");
        backgroundImage.setVisible(true);
        return backgroundImage;
    }


    public Scene getRoomScene() {
        return mRoomScene;
    }

    public void resetView() {
        mPlayersCards.resetView();
        mRightSidePanel.hideTrumpCard();
    }

}
