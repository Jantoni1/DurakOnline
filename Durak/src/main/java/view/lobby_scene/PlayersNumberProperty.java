package main.java.view.lobby_scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Created by Kuba on 20.08.2017.
 */
public class PlayersNumberProperty extends HBox {

    private ArrayList<CheckBox> mPlayersNumber;

    public PlayersNumberProperty() {
        mPlayersNumber = new ArrayList<>();
        createInfoText();
        addCheckBoxes();
        addCheckBoxesOnEventBehavior();
        mPlayersNumber.get(0).setSelected(true);
        setAlignment(Pos.TOP_CENTER);
    }


    public int getNumberOfPlayers() {
        for(CheckBox checkBox : mPlayersNumber) {
            if(checkBox.isSelected()) {
                return Integer.parseInt(checkBox.getId());
            }
        }
        return -1;
    }

    private void createInfoText() {
        Text seats = new Text("Seats:");
        seats.setId("seats");
        HBox.setMargin(seats, new Insets(0.0, 20.0, 0.0, 10.0));
        getChildren().add(seats);
    }

    private void addCheckBoxes() {
        for(int i = 2; i < 5; ++i) {
            CheckBox checkBox = new CheckBox();
            checkBox.setId("" + i);
            mPlayersNumber.add(checkBox);
            Text text = generatePlayersNumberDisplay(i);
            VBox vBox = new VBox(mPlayersNumber.get(i - 2), text);
            vBox.setAlignment(Pos.CENTER);
            getChildren().add(vBox);
        }
    }

    private Text generatePlayersNumberDisplay(int pNumberOfPlayers) {
        Text text = new Text("" + pNumberOfPlayers);
        text.setId("player_label");
//        text.setFill(Color.WHITE);
        return text;
    }

    private void addCheckBoxesOnEventBehavior() {
        mPlayersNumber.forEach(box -> {
            box.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(box.isSelected()) {
                        mPlayersNumber.forEach(checkBox -> checkBox.setSelected(false));
                    }
                    box.setSelected(true);
                }
            });
        });
    }

    public void reset() {
        mPlayersNumber.forEach(checkBox -> checkBox.setSelected(false));
        mPlayersNumber.get(0).setSelected(true);
    }
}
