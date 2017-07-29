package main.java.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Play;

/**
 * Created by Kuba on 26.07.2017.
 */
public class PassButton extends Button {

    public PassButton(MessageBox pMessageBox) {
        super("PASS");
        setStyle("-fx-background-radius: 8px; -fx-font: 20 Roboto; -fx-base: #768aa5; -fx-font-size: 30px;");
        setPrefWidth(140);
        setPrefHeight(60);
//        setVisible(false);
        setPasButtonAction(pMessageBox);
        setVisible(false);
    }

    private void setPasButtonAction(MessageBox pMessageBox) {
        setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pMessageBox.sendMessage(new Play(-1));
            }
        });
    }
}
