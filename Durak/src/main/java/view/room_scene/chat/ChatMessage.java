package main.java.view.room_scene.chat;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.java.view.room_scene.RightSidePanel;

/**
 * Created by Kuba on 29.07.2017.
 */
public class ChatMessage extends TextFlow {

    public ChatMessage(String pPlayersNick, String pMessageContent) {
        setId("text-flow");
        Text nick = new Text(pPlayersNick);
        nick.setFill(Color.WHITE);
        nick.setId("text-author");
        Text content = new Text(": " + pMessageContent);
        content.setId("text-content");
        content.setFill(Color.WHITE);
        getChildren().addAll(nick, content);
//        setLineSpacing();
       setMaxWidth(RightSidePanel.getChatWidth()- 3.0);
       setPrefWidth(RightSidePanel.getChatWidth()-3.0);
    }
}
