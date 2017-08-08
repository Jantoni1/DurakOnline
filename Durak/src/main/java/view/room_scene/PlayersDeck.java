package main.java.view.room_scene;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    private final int maxCardsDisplayed = 26;
    private Player mPlayer;
    private boolean mIsMe;

    public PlayersDeck(MessageBox pMessageBox, ArrayList<Integer> pAvailableCards, boolean pIsMe) {
        setPickOnBounds(false);
        mIsMe = pIsMe;
        initializeCardsCollection(pMessageBox, pAvailableCards);
        setCardControlButtons();
        addComponents();
        mCardsContainer.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);
    }

    public void resetCards() {
        mCardsContainer.getChildren().clear();
    }

    private void addComponents() {
        ImageView fakeCard = new ImageView("main/resources/images/back.png");
        fakeCard.setVisible(false);
        fakeCard.setPickOnBounds(false);
        StackPane stackPane = new StackPane(fakeCard);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setPickOnBounds(false);
        stackPane.getChildren().add(mCardsContainer);
        if(mIsMe) {
            getChildren().addAll(mLeftArrow, stackPane, mRightArrow);
        }
        else {
            getChildren().add(stackPane);
        }
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
        mLeftArrow.setVisible(false);
        mRightArrow.setVisible(false);
        mRightArrow.setOnMouseClicked((MouseEvent event) -> {
            mRightArrow.setVisible(false);
            mLeftArrow.setVisible(true);
            lastCardDisplayed = mPlayer.getPlayerCards().size();
            firstCardDisplayed = lastCardDisplayed - 26;
            addCardsToMe();
        });
        mLeftArrow.setOnMouseClicked((MouseEvent event) -> {
            mRightArrow.setVisible(true);
            mLeftArrow.setVisible(false);
            lastCardDisplayed = 26;
            firstCardDisplayed = 0;
            addCardsToMe();
        });
    }

    private void initializeCardsCollection(MessageBox pMessageBox, ArrayList<Integer> pAvailableCards) {
        mCards = new ArrayList<>();
        for(int i = 0; i < numberOfCards; ++i) {
            mCards.add(new CardLayout(pMessageBox, pAvailableCards));
            mCards.get(i).setPickOnBounds(false);
        }
        mCardsContainer = new HBox();
        mCardsContainer.setPickOnBounds(false);
        setCardsSpacing();
    }

    private void setCardsSpacing() {
        int numberOfCards;
        if(mPlayer != null) {
            numberOfCards = (mPlayer.getPlayerCards() != null ? mPlayer.getPlayerCards().size() : mPlayer.getNumberOfCards());
        }
        else {
            numberOfCards = maxCardsDisplayed;
        }
        mCardsContainer.setSpacing(-78.0 + (410.0 / (double) (Math.max(numberOfCards, maxCardsDisplayed) - 1)));
    }

    public void setPlayer(Player pPlayer) {
        mPlayer = pPlayer;
        if(mPlayer != null) {
            updatePlayersCards();
        }
    }

    public void updatePlayersCards() {
        if(mIsMe) {
            addCardsToMe();
            setArrows();
        }
        else {
            addFakeCards();
        }
        setCardsSpacing();
    }

    private void setArrows() {
        if(mPlayer.getPlayerCards().size() > maxCardsDisplayed) {
                mRightArrow.setVisible(!mLeftArrow.isVisible());
        }
        else {
            mLeftArrow.setVisible(false);
            mRightArrow.setVisible(false);
        }
    }

    private void addCardsToMe() {
        mCardsContainer.getChildren().clear();
        if(mLeftArrow.isVisible()) {
            lastCardDisplayed = mPlayer.getPlayerCards().size();
            firstCardDisplayed = lastCardDisplayed - maxCardsDisplayed;
        }
        for(int i = 0; i < numberOfCards; ++i) {
            boolean doesCardExist = i < mPlayer.getPlayerCards().size();
            Card card = doesCardExist ? mPlayer.getPlayerCards().get(i) : null;
            mCards.get(i).updateCardView(doesCardExist, i, card);
            if(doesCardExist && i >= firstCardDisplayed && i < lastCardDisplayed) {
                mCardsContainer.getChildren().add(mCards.get(i));
            }
        }

    }

    private void addFakeCards() {
        mCardsContainer.getChildren().clear();
        for(int i = 0; i < numberOfCards; ++i) {
            mCards.get(i).updateCardView(i < mPlayer.getNumberOfCards(), -1, null);
            if(i < mPlayer.getNumberOfCards()) {
                mCardsContainer.getChildren().add(mCards.get(i));
            }
        }
    }
}
