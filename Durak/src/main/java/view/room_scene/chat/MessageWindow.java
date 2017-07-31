package main.java.view.room_scene.chat;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;


/**
 * Created by Kuba on 29.07.2017.
 */
public class MessageWindow extends ScrollPane {

    private VBox pMessageCollection;

    public MessageWindow(double width, double height)  {
        super();
        setStyle("-fs-set-background-fx-focus-color: transparent; -fx-faint-focus-color: transparent");
        pMessageCollection = new VBox();
        setContent(pMessageCollection);
        setPrefSize(width, height);
    }

    public void addMessage(TextFlow pTextMessage) {
        pMessageCollection.getChildren().add(pTextMessage);
    }
}
