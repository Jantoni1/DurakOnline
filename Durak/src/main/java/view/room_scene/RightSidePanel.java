package main.java.view.room_scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.java.model.server.Card;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Leave;
import main.java.view.room_scene.chat.ChatBox;

/**
 * Created by Kuba on 29.07.2017.
 */
public class RightSidePanel extends VBox {
    
    private Button mLeaveButton;
    private ChatBox mChatBox;
    private HBox mTalon;
    private ImageView mTrumpCard;
    private Label mNumberOfCards;
    private static double mChatWidth = 270.0;
    private final double mChatHeight = 500.0;

    public RightSidePanel(MessageBox pMessageBox) {
        super(10.0);
        createLeaveButton(pMessageBox);
        mChatBox = new ChatBox(pMessageBox, mChatWidth, mChatHeight);
        createTalonProperties();
        getChildren().addAll(mLeaveButton, mChatBox, mTalon);
        setAlignment(Pos.CENTER);
    }

    public static double getChatWidth() {
        return mChatWidth;
    }

    public void addChatMessage(String pAuthor, String pContent) {
        mChatBox.addChatMessage(pAuthor, pContent);
    }

    public void hideTrumpCard() {
        mTrumpCard.setVisible(false);
        mNumberOfCards.setText("");
        mTalon.setVisible(false);
    }
    
    public void setTrumpCard(Card pCard) {
        mTrumpCard.setImage(new Image("main/resources/images/"+ pCard.mFigure.getFigure() + "_" + pCard.mSuit.getColor() + ".png"));
        mTrumpCard.setFitHeight(102.0);
        mTrumpCard.setFitWidth(70.0);
        mTrumpCard.setVisible(true);
        mTalon.setVisible(true);
    }

    public void setNumberOfCards(int pNumberOfCardsOnTalon) {
        mNumberOfCards.setText("" + pNumberOfCardsOnTalon);
    }

    private void createTalonProperties() {
        mTalon = new HBox();
        mTalon.setSpacing(2.0);
        ImageView imageView = new ImageView("main/resources/images/back.png");
        mNumberOfCards = new Label();
        mNumberOfCards.setStyle("-fx-text-fill: white; -fx-font: 16 Roboto; -fx-font-size: 30px;");
        StackPane stackPane = new StackPane(imageView, mNumberOfCards);
        mTrumpCard = new ImageView("main/resources/images/back.png");
//        mTrumpCard.setVisible(false);
        mTalon.setAlignment(Pos.CENTER);
        mTalon.getChildren().addAll(stackPane, mTrumpCard);
        mTalon.setVisible(false);
    }
    
    private void createLeaveButton(MessageBox pMessageBox) {
        mLeaveButton = new Button("LEAVE");
        mLeaveButton.setStyle("-fx-font: 16 Roboto; -fx-font-size: 30px;");
        mLeaveButton.setFocusTraversable(false);
        setButtonAction(pMessageBox);
        setLeaveButtonSize(mLeaveButton);
    }


    private void setLeaveButtonSize(Button pButton) {
        pButton.setMaxWidth(200);
        pButton.setPrefWidth(200);
//        pButton.setPrefHeight();
    }

    private void setButtonAction(MessageBox pMessageBox) {
        mLeaveButton.setOnMouseReleased((event) -> pMessageBox.sendMessage(new Leave(false)));
    }
}
