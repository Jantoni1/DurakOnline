package main.java.view.chat;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.java.view.RightSidePanel;

/**
 * Created by Kuba on 29.07.2017.
 */
public class ChatMessage extends TextFlow {

    public ChatMessage(String pPlayersNick, String pMessageContent) {
        Text Nick = new Text(pPlayersNick + ": ");
        Text Content = new Text(pMessageContent);
        getChildren().addAll(Nick, Content);
//        setLineSpacing();
       setMaxWidth(RightSidePanel.getChatWidth() - 5.0);
    }
}
