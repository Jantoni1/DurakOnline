package main.java.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import main.java.model.client.Player;
import main.java.model.server.Card;
import main.java.network.client.MessageBox;

import java.util.ArrayList;

/**
 * Created by Kuba on 24.07.2017.
 */
public class PlayersDeck extends HBox {

    private ArrayList<CardLayout> mCards;
    private HBox mCardsContainer;
    private int firstCardDisplayed;
    private int lastCardDisplayed;
    private ImageView mLeftArrow;
    private ImageView mRightArrow;
    private final int numberOfCards = 52;
    private Player mPlayer;
    private boolean mIsMe;

    public PlayersDeck(MessageBox pMessageBox, ArrayList<Integer> pAvailableCards, boolean pIsMe) {
        mIsMe = pIsMe;
        initializeCardsCollection(pMessageBox, pAvailableCards);
        setCardControlButtons();
    }

    private void setCardControlButtons() {
        if(mIsMe) {
            firstCardDisplayed = 0;
            lastCardDisplayed = 26;
            setupArrows();
        }
    }

    private void setupArrows() {
        mLeftArrow = new ImageView("main/resources/left_arrow.png");
        mRightArrow = new ImageView("main/resources/right_arrow.png");
        //TODO zrób strzałki
    }

    private void initializeCardsCollection(MessageBox pMessageBox, ArrayList<Integer> pAvailableCards) {
        mCards = new ArrayList<>();
        for(int i = 0; i < numberOfCards; ++i) {
            mCards.add(new CardLayout(pMessageBox, pAvailableCards));
        }
        mCardsContainer = new HBox();
        mCardsContainer.getChildren().addAll(mCards);
    }

    public void setPlayer(Player pPlayer) {
        mPlayer = pPlayer;
        updatePlayersCards();
    }

    public void updatePlayersCards() {
        if(mIsMe) {
            addCardsToMe();
        }
        else {
            addFakeCards();
        }
    }

    private void addCardsToMe() {
        for(int i = 0; i < numberOfCards; ++i) {
            boolean doesCardExist = i < mPlayer.getPlayerCards().size();
            Card card = doesCardExist ? mPlayer.getPlayerCards().get(i) : null;
            mCards.get(i).updateCardView(doesCardExist, i, card);
        }
    }

    private void addFakeCards() {
        for(int i = 0; i < numberOfCards; ++i) {
            mCards.get(i).updateCardView(i < mPlayer.getNumberOfCards(), -1, null);
        }
    }
}
