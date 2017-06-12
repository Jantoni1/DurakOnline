package main.java.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import main.java.controller.client.ClientManager;
import main.java.model.client.AnotherPlayer;
import main.java.model.server.Card;

import java.util.ArrayList;

/**
 * @brief class creating
 */
public class PlayersLayout extends VBox {

    private HBox mCards;
    private Label mNick;
    private double mSpacing;
    private boolean isUsed;
    private static int MAX_CARDS_DISPLAY = 15;
    private int firstDisplayed;
    private int lastDisplayed;
    private ClientManager mClientManager;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public PlayersLayout(double pRotation, ClientManager pClientManager) {
        mCards = new HBox();
        mNick = new Label("");
        setDefaultProperties();
        getChildren().addAll(mNick, mCards);
        isUsed = false;
        setStyle("-fx-rotate: " + pRotation + ";");
        firstDisplayed = 0;
        lastDisplayed = 14;
        mClientManager = pClientManager;
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

    public void updateView(AnotherPlayer pAnotherPlayer) {
        if(pAnotherPlayer == null) {
            mCards.getChildren().clear();
            mNick.setVisible(false);
            return;
        }
        changeView(pAnotherPlayer);
    }

    private void fillDeckWithCardBacks(int numberOfCards) {
        if(mCards.getChildren().size() != numberOfCards) {
            mCards.getChildren().clear();
            addCardsToPlayer(numberOfCards);
        }
    }

    private synchronized void createPlayersDeck(ArrayList<Card> pPlayersCard, boolean pIsMyTurn) {
        synchronized(pPlayersCard) {
            getChildren().remove(mCards);
            mCards = new HBox();
            mCards.setSpacing(-75.0 + (400.0 / (double) pPlayersCard.size() - 1));
            for(Card card : pPlayersCard) {
                mCards.getChildren().add(createCardBackImage(card, pIsMyTurn));
            }
            getChildren().add(mCards);
        }

//        pPlayersCard.forEach(card -> mCards.getChildren().add(createCardBackImage(card, pIsMyTurn)));
    }

    private void createDeck(AnotherPlayer pAnotherPlayer) {
        if(pAnotherPlayer.getPlayerCards() != null) {
            createPlayersDeck(pAnotherPlayer.getPlayerCards(), pAnotherPlayer.ismIsMyTurn());
        }
        else {
            fillDeckWithCardBacks(pAnotherPlayer.getmNumberOfCards());
        }
    }

    private void changeView(AnotherPlayer pAnotherPlayer) {
        createDeck(pAnotherPlayer);
        if(!mNick.getText().equals(pAnotherPlayer.getmNick())) {
            mNick.setText(pAnotherPlayer.getmNick());
        }
        setNickEffect(pAnotherPlayer);
    }

    private void setNickEffect(AnotherPlayer pAnotherPlayer) {
        if(pAnotherPlayer.ismIsMyTurn()) {
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
            mCards.getChildren().add(createCardBackImage(null, false));
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

    private void setCardOnClickEvent(ImageView pCardBack, Card pCard, boolean pIsItMe) {
        if(pCard != null && pIsItMe) {
            pCardBack.setOnMouseClicked((MouseEvent event) -> {
                mClientManager.playACard(pCard);
            });
        }
        else {
            pCardBack.setDisable(true);
        }
    }

    private ImageView createCardBackImage(Card pCard, boolean pIsItMe) {
        ImageView cardBack = pickProperCardImage(pCard);
        setCardOnClickEvent(cardBack, pCard, pIsItMe);
        return cardBack;
    }

    public double getmSpacing() {
        return mSpacing;
    }
}
