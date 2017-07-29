package main.java.view.chat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Chat;


/**
 * Created by Kuba on 29.07.2017.
 */
public class InputPanel extends HBox {

    private TextField mMessageInput;
    private Button mSendButton;
    private final double mLengthDivisionRatio = 0.75;

    public InputPanel(MessageBox pMessageBox, double width, double height) {
        mMessageInput = new TextField("Talk to other players...");
        mMessageInput.setPrefSize(width * mLengthDivisionRatio, height);
        mSendButton = new Button("SEND");
        mSendButton.setPrefSize(width * (1.0 - mLengthDivisionRatio), height);
        setButtonAction(pMessageBox);
        getChildren().addAll(mMessageInput, mSendButton);
    }

    private void setButtonAction(MessageBox pMessageBox) {
        mSendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                pMessageBox.sendMessage(new Chat(mMessageInput.getText()));
                mMessageInput.clear();
            }
        });
    }
}
