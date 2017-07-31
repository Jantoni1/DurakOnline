package main.java.view.room_scene.chat;

import javafx.scene.layout.VBox;
import main.java.network.client.MessageBox;

/**
 * Created by Kuba on 29.07.2017.
 */
public class ChatBox extends VBox {

    MessageWindow mMessageWindow;
    InputPanel mInputPanel;
    private final double heightProportion = 0.95;

    public ChatBox(MessageBox pMessageBox, double pWidth, double pHeight) {
        mMessageWindow = new MessageWindow(pWidth, pHeight * heightProportion);
        mInputPanel = new InputPanel(pMessageBox, pWidth, pHeight * (1.0 - heightProportion));
        mMessageWindow.setFocusTraversable(false);
        getChildren().addAll(mMessageWindow, mInputPanel);
    }

    public void addChatMessage(String pAuthor, String pContent) {
        mMessageWindow.addMessage(new ChatMessage(pAuthor, pContent));
    }
}
