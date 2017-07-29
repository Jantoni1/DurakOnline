package main.java.view.chat;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;


/**
 * Created by Kuba on 29.07.2017.
 */
public class MessageWindow extends ScrollPane {

    private VBox pMessageCollection;

    public MessageWindow(double width, double height)  {
        super();
        pMessageCollection = new VBox();
        setContent(pMessageCollection);
        setPrefSize(width, height);
    }

    public void addMessage(TextFlow pTextMessage) {
        pMessageCollection.getChildren().add(pTextMessage);
    }
}
