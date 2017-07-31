package main.java.view.room_scene;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.java.model.client.Player;
import main.java.network.client.MessageBox;

import java.util.ArrayList;

/**
 * @brief Class simplifies displaying player's cards and nick. Rotation is used to place each player on his position on the scene, and the mBackgroundShape
 * makes it easy to control group's position
 */
public class PlayersPropertyDisplay extends StackPane {

    private Rectangle mBackgroundShape;
    private VBox mCardsAndNick;
    private PlayersDeck mPlayersDeck;
    private Label mNick;
    private boolean mIsMe;
    private Player mPlayer;

    public PlayersPropertyDisplay(double rotation, ArrayList<Integer> pAvailableCards, MessageBox pMessageBox, boolean pIsMe, double width, double height) {
        setPickOnBounds(false);
        createComponents(pAvailableCards, pMessageBox, pIsMe, width, height);
        addComponents();
        setRotate(rotation);
    }

    public void setPlayer(Player pPlayer) {
        mPlayer = pPlayer;
        mPlayersDeck.setPlayer(pPlayer);
        mNick.setText(pPlayer.getmNick());
    }

    public void updatePlayersCards() {
        if(mPlayer != null) {
            mPlayersDeck.updatePlayersCards();
            setNickEffect();
        }
    }

    private void createComponents(ArrayList<Integer> pAvailableCards, MessageBox pMessageBox, boolean pIsMe, double width, double height) {
        mIsMe = pIsMe;
        mBackgroundShape = new Rectangle(width, height, Color.TRANSPARENT);
        mBackgroundShape.setPickOnBounds(false);
        mBackgroundShape.setVisible(false);
        mCardsAndNick = new VBox(0.0);
        mCardsAndNick.setPickOnBounds(false);
        mCardsAndNick.setAlignment(Pos.CENTER);
        mNick = new Label("");
        mNick.setPickOnBounds(false);
        setNickProperties();
        mPlayersDeck = new PlayersDeck(pMessageBox, pAvailableCards, mIsMe);
        mPlayersDeck.setPickOnBounds(false);
        setAlignment(Pos.CENTER);
    }

    private void setNickProperties() {
        mNick.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        mNick.setFont(Font.font("Roboto", FontWeight.LIGHT, 26));
        mNick.setTextFill(Color.web("#ffcc66")); //#ffcc66
//        mNick.setAlignment(Pos.CENTER);
    }

    private void addComponents() {
        if(mIsMe) {
            mCardsAndNick.getChildren().addAll(mPlayersDeck, mNick);
        }
        else {
            mCardsAndNick.getChildren().addAll(mNick, mPlayersDeck);
        }
        getChildren().addAll(mBackgroundShape, mCardsAndNick);
        setAlignment(mCardsAndNick, Pos.TOP_CENTER);
//        setAlignment(mBackgroundShape, Pos.TOP_CENTER);
    }

    private void setNickEffect() {
        if(mPlayer != null && mPlayer.ismIsMyTurn()) {
            mNick.setEffect(new Glow(1.0));
            mNick.setFont(Font.font("Roboto", FontWeight.EXTRA_BOLD, 26));
            mNick.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        }
        else
        {
            mNick.setEffect(null);
            mNick.setFont(Font.font("Roboto", FontWeight.LIGHT, 26));
            mNick.setStyle("-fx-strike-width: 0;");
        }
        mNick.setVisible(true);
    }
}
