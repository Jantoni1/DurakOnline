package main.java.view.room_scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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
    private ImageView mTrumpCard;
    private static double mChatWidth = 270.0;
    private final double mChatHeight = 500.0;

    public RightSidePanel(MessageBox pMessageBox) {
        super(10.0);
        createLeaveButton(pMessageBox);
        mChatBox = new ChatBox(pMessageBox, mChatWidth, mChatHeight);
        getChildren().addAll(mLeaveButton, mChatBox);
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
    }
    
    public void setTrumpCard(Card pCard) {
        mTrumpCard = new ImageView("main/resources/images/"+ pCard.mFigure.getFigure() + "_" + pCard.mSuit.getColor() + ".png");
        getChildren().add(mTrumpCard);
        mTrumpCard.setVisible(true);
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
