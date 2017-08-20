package main.java.view.room_scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.java.model.server.Card;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Play;

import java.util.ArrayList;

/**
 * Created by Kuba on 24.07.2017.
 */
public class CardLayout extends ImageView {

    private ArrayList<Integer> mAvailableCards;
    private MessageBox mMessageBox;

    public CardLayout(MessageBox pMessageBox, ArrayList<Integer> pAvailableCards) {
        mAvailableCards = pAvailableCards;
        mMessageBox = pMessageBox;
    }

    public void updateCardView(boolean isVisible, int pCardIndex, Card pCard) {
        if(isVisible) {
            loadImage(pCard);
            setAction(pCardIndex, pCard != null);
        }
        setVisible(true);
    }

    private void loadImage(Card pCard) {
        if (pCard == null) {
            setImage(new Image("main/resources/images/back.png"));
        } else {
            setImage(new Image("main/resources/images/" + pCard.mFigure.getFigure() + "_" + pCard.mSuit.getColor() + ".png"));
        }
        setPickOnBounds(pCard != null);
    }

    private void setAction(int pCardIndex, boolean pIsItMe) {
        if(pCardIndex != -1 && pIsItMe) {
            setOnMouseClicked((MouseEvent event) -> playACard(pCardIndex));
        }
    }

    private void playACard(int pCardIndex) {
        if(mAvailableCards.contains(pCardIndex)) {
            mMessageBox.sendMessage(new Play(pCardIndex));
        }
    }

}
