package main.java.view.room_scene.chat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import static java.lang.Thread.sleep;


/**
 * Created by Kuba on 29.07.2017.
 */
public class MessageWindow extends ScrollPane {

    private VBox pMessageCollection;

    public MessageWindow(double width, double height)  {
        super();
        setStyle("-fs-set-background-fx-focus-color: transparent; -fx-faint-focus-color: transparent");
        pMessageCollection = new VBox();
        pMessageCollection.setSpacing(0.1);
        setContent(pMessageCollection);
        setPrefSize(width, height);
        setHbarPolicy(ScrollBarPolicy.NEVER);
//        setVbarPolicy(ScrollBarPolicy.NEVER);
        setStyle("-fx-font-size: 5px;");
        pMessageCollection.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setVvalue(1.0);
            }
        });
    }

    public void addMessage(TextFlow pTextMessage) {
        pMessageCollection.getChildren().add(pTextMessage);
    }
}
