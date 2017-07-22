package main.java.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.java.controller.client.ClientManager;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Ready;

public class ReadyPanel extends VBox {

    private MessageBox mMessageBox;
    private Button mReadyButton;
    private Button mUnreadyButton;
    private StackPane mStackPane;
    private Label mReadyPlayersCountLabel;
    private int mMaxPlayers;
    private int mReadyPlayersCounter;

    public ReadyPanel(MessageBox pMessageBox, int pMaxPlayers) {
        mMaxPlayers = pMaxPlayers;
        mReadyPlayersCounter = 0;
        mMessageBox = pMessageBox;
        createNodes();
        setVBoxProperties();
    }

    private void createNodes() {
        createPlayersCountLabel();
        createButtonStack();
        getChildren().addAll(mReadyPlayersCountLabel, mStackPane);
    }

    private void setVBoxProperties() {
        setPrefWidth(300);
        setPrefHeight(300);
        setAlignment(Pos.CENTER);
    }

    public void reset() {
        mReadyButton.setVisible(true);
        mUnreadyButton.setVisible(false);
        mReadyPlayersCounter = 0;
        setVisible(false);
    }

    public void activate() {
        setLabelText();
        setVisible(true);
    }

    public void playerReady(boolean trueIfReadyFalseIfUnready) {
        if(trueIfReadyFalseIfUnready) {
            ++mReadyPlayersCounter;
        }
        else {
            --mReadyPlayersCounter;
        }
        setLabelText();
    }

    private void setLabelText() {
        mReadyPlayersCountLabel.setText("Ready players: " + mReadyPlayersCounter + "/" + mMaxPlayers);
    }


    private void createPlayersCountLabel() {
        mReadyPlayersCountLabel = new Label("Ready players: " + mReadyPlayersCounter + "/" + mMaxPlayers);
        mReadyPlayersCountLabel.setFont(Font.font("Roboto", FontWeight.LIGHT, 30));
        mReadyPlayersCountLabel.setTextFill(Color.web("#768aa5"));
    }

    private void createButtonStack() {
        mStackPane = new StackPane();
        createReadyUnreadyButtons();
        mStackPane.getChildren().addAll(mReadyButton, mUnreadyButton);
    }

    private void createReadyUnreadyButtons() {
        createReadyButton();
        createUnreadyButton();
        setReadyButtonAction(mReadyButton, mUnreadyButton, true);
        setReadyButtonAction(mUnreadyButton, mReadyButton, false);
    }

    private void createReadyButton() {
        mReadyButton= new Button("READY");
        mReadyButton.setStyle("-fx-background-radius: 16px; -fx-font: 16 Roboto; -fx-base: #768aa5; -fx-font-size: 30px; -fx-text-fill: #f2f2f2;"); //330033
        setButtonSize(mReadyButton);
        mReadyButton.setVisible(true);
    }

    private void setReadyButtonAction(Button pActiveButton, Button pHiddenButton, boolean trueIfReadyFalseIfUnready) {
        pActiveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                mMessageBox.sendMessage(new Ready(trueIfReadyFalseIfUnready));
                pActiveButton.setVisible(false);
                pHiddenButton.setVisible(true);
            }
        });
    }

    public void setButtonSize(Button pButton) {
        pButton.setMaxWidth(300);
        pButton.setPrefWidth(300);
        pButton.setPrefHeight(100);
    }

    private void createUnreadyButton() {
        mUnreadyButton= new Button("UNREADY");
        mUnreadyButton.setStyle("-fx-background-radius: 16px; -fx-font: 16 Roboto; -fx-base: #f2f2f2; -fx-font-size: 30px; -fx-text-fill: #768aa5;");
        setButtonSize(mUnreadyButton);
        mUnreadyButton.setVisible(false);
    }
}
