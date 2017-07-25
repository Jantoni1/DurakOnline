package main.java.view;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.model.client.Player;
import main.java.network.client.MessageBox;

import java.lang.reflect.Array;
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

    public PlayersPropertyDisplay(double rotation, ArrayList<Integer> pAvailableCards, MessageBox pMessageBox, boolean pIsMe) {
        createComponents(pAvailableCards, pMessageBox, pIsMe);
        addComponents();
        setRotate(rotation);
    }

    public void setPlayer(Player pPlayer) {
        mPlayersDeck.setPlayer(pPlayer);
        mNick.setText(pPlayer.getmNick());
    }

    public void updatePlayersCards() {
        mPlayersDeck.updatePlayersCards();
    }

    private void createComponents(ArrayList<Integer> pAvailableCards, MessageBox pMessageBox, boolean pIsMe) {
        mIsMe = pIsMe;
        mBackgroundShape = new Rectangle(800, 150, Color.TRANSPARENT);
        mCardsAndNick = new VBox(5.0);
        mNick = new Label("");
        mPlayersDeck = new PlayersDeck(pMessageBox, pAvailableCards, mIsMe);
    }

    private void addComponents() {
        if(mIsMe) {
            mCardsAndNick.getChildren().addAll(mPlayersDeck, mNick);
        }
        else {
            mCardsAndNick.getChildren().addAll(mNick, mPlayersDeck);
        }
        getChildren().addAll(mBackgroundShape, mCardsAndNick);
        mBackgroundShape.setPickOnBounds(false);
    }
}
