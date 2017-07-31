package main.java.view.room_scene.chat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Chat;


/**
 * Created by Kuba on 29.07.2017.
 */
public class InputPanel extends HBox {

    private TextField mMessageInput;
    private Button mSendButton;
    private MessageBox mMessageBox;
    private final double mLengthDivisionRatio = 0.75;

    public InputPanel(MessageBox pMessageBox, double width, double height) {
        mMessageBox = pMessageBox;
        setMessageInputProperties(width, height);
        setButtonActionProperties(width, height);
        getChildren().addAll(mMessageInput, mSendButton);
        setFocusTraversable(true);
    }

    private void setButtonActionProperties(double width, double height) {
        mSendButton = new Button("SEND");
        mSendButton.setPrefSize(width * (1.0 - mLengthDivisionRatio), height);
        mSendButton.setId("send");
        mSendButton.setFocusTraversable(false);
        mSendButton.setOnKeyReleased((event) -> {if(event.getCode() == KeyCode.ENTER &&!mMessageInput.getText().trim().isEmpty()) {
            mMessageInput.setId("");
            sendMessage();
        }});
        setButtonAction();
    }

    private void setButtonAction() {
        mSendButton.setOnMousePressed((event) -> {
            if(mMessageInput.getText() != "" ){
                mMessageInput.setId("test");
            }
        });
        mSendButton.setOnMouseReleased((event) -> {
            if(mMessageInput.getText() != "") {
                mMessageInput.setId("send");
                sendMessage();
            }
        });
    }

    private void setMessageInputProperties(double width, double height) {
        mMessageInput = new TextField();
        mMessageInput.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent");
        mMessageInput.setPromptText("Press TAB to write...");
        mMessageInput.setPrefSize(width * mLengthDivisionRatio, height);
        mMessageInput.setOnKeyPressed((event) -> {
            if(event.getCode() == KeyCode.ENTER && !mMessageInput.getText().trim().isEmpty()) {
                mSendButton.requestFocus(); mMessageInput.setId("test");
            }
            else if(event.getCode() == KeyCode.ENTER) {
                this.requestFocus();
            }
        });
    }

    private void sendMessage() {
        mMessageBox.sendMessage(new Chat(mMessageInput.getText()));
        mMessageInput.clear();
        mMessageInput.requestFocus();
    }

}
