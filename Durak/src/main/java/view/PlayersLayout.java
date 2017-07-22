package main.java.view;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.java.model.client.Player;
import main.java.model.server.Card;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Play;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @brief class creating
 */
public class PlayersLayout extends VBox {

    private RoomScene.Model mModel;
    private HBox mCards;
    private Label mNick;
    private double mSpacing;
    private boolean isUsed;
    private static int MAX_CARDS_DISPLAY = 15;
    private int firstDisplayed;
    private int lastDisplayed;
    private MessageBox mMessageBox;
    private ArrayList<Integer> mAvailableCards;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public PlayersLayout(double pRotation, MessageBox pMessageBox, RoomScene.Model pModel) {
        mModel = pModel;
        mAvailableCards = new ArrayList<>();
        mCards = new HBox();
        mNick = new Label("");
        setDefaultProperties();
        getChildren().addAll(mNick, mCards);
        isUsed = false;
        setStyle("-fx-rotate: " + pRotation + ";");
        firstDisplayed = 0;
        lastDisplayed = 14;
        mMessageBox = pMessageBox;
    }

    public void setAvailableCards(ArrayList<Integer> pAvailableCards) {
        synchronized(mAvailableCards) {
            if(pAvailableCards != null) {
                mAvailableCards = pAvailableCards;
            }
            else {
                mAvailableCards.clear();
            }
        }
    }

    private void setNickProperties() {
        mNick.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        mNick.setFont(Font.font("Roboto", FontWeight.LIGHT, 26));
        mNick.setTextFill(Color.web("#ffcc66")); //#ffcc66
        mNick.setAlignment(Pos.CENTER);
//        mNick.setMinSize(300.0,Label.USE_PREF_SIZE);
    }

    private void setDefaultProperties() {
        setNickProperties();
        setAlignment(Pos.CENTER);
//        mCards.setStyle("-fx-rotate: 180;");
    }

    public void updateView(Player pPlayer) {
        if(pPlayer == null) {
            mCards.getChildren().clear();
            mNick.setVisible(false);
            return;
        }
        changeView(pPlayer);
    }

    private void fillDeckWithCardBacks(int numberOfCards) {
        if(mCards.getChildren().size() != numberOfCards) {
            mCards.getChildren().clear();
            addCardsToPlayer(numberOfCards);
        }
    }

    private synchronized void createPlayersDeck(CopyOnWriteArrayList<Card> pPlayersCard, boolean pIsMyTurn) {
        synchronized(pPlayersCard) {
            getChildren().remove(mCards);
            mCards = new HBox();
            mCards.setSpacing(-75.0 + (400.0 / (double) pPlayersCard.size() - 1));
            for(Card card : pPlayersCard) {
                mCards.getChildren().add(createCardBackImage(pPlayersCard.indexOf(card), card, pIsMyTurn));
            }
            getChildren().add(mCards);
        }

//        pPlayersCard.forEach(card -> mCards.getChildren().add(createCardBackImage(card, pIsMyTurn)));
    }

    private void createDeck(Player pPlayer) {
        if(pPlayer.getPlayerCards() != null) {
            createPlayersDeck(pPlayer.getPlayerCards(), pPlayer.ismIsMyTurn());
        }
        else {
            fillDeckWithCardBacks(pPlayer.getmNumberOfCards());
        }
    }

    private void changeView(Player pPlayer) {
        createDeck(pPlayer);
        if(!mNick.getText().equals(pPlayer.getmNick())) {
            mNick.setText(pPlayer.getmNick());
        }
        setNickEffect(pPlayer);
    }

    private void setNickEffect(Player pPlayer) {
        if(pPlayer.ismIsMyTurn()) {
            mNick.setEffect(new Glow(1));
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

    private void addCardsToPlayer(int numberOfCards) {
        if(numberOfCards == 0) {
            return;
        }
        mCards.setSpacing(-75.0 + (400.0 / (double) numberOfCards - 1));
        setOtherPlayersStackLength(numberOfCards);
        for(int i=0; i<numberOfCards; ++i) {
            mCards.getChildren().add(createCardBackImage(-1, null, false));
        }
    }

    private void setOtherPlayersStackLength(int numberOfCards) {
        if(numberOfCards == 0) {
            mSpacing = 0.0;
        }
        else {
            mSpacing = (80.0 + mCards.getSpacing())*(numberOfCards-1) + 80.0;
        }
    }

    private ImageView pickProperCardImage(Card pCard) {
        if(pCard == null) {
            return new ImageView("main/resources/images/back.png");
        }
        return new ImageView("main/resources/images/"+ pCard.mFigure.getFigure() + "_" + pCard.mSuit.getColor() + ".png");
    }

    private void setCardOnClickEvent(ImageView pCardBack, int pCardIndex, boolean pIsItMe) {
        if(pCardIndex != -1 && pIsItMe) {
            pCardBack.setOnMouseClicked((MouseEvent event) -> playACard(pCardIndex));
        }
        pCardBack.setPickOnBounds(pCardIndex != -1 && pIsItMe);
    }

    private void playACard(int pCardIndex) {
            if(mModel.getAvailableCards().contains(pCardIndex)) {
                mMessageBox.sendMessage(new Play(pCardIndex));
            }
    }

    private ImageView createCardBackImage(int pCardIndex, Card pCard, boolean pIsItMe) {
        ImageView cardBack = pickProperCardImage(pCard);
        setCardOnClickEvent(cardBack, pCardIndex, pIsItMe);
        return cardBack;
    }

    public double getmSpacing() {
        return mSpacing;
    }
}
