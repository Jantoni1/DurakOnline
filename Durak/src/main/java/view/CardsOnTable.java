package main.java.view;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.model.server.Card;

import java.util.ArrayList;

/**
 * Created by Kuba on 12.06.2017.
 */
class CardsOnTable extends VBox {

    private HBox mAttackingCards;
    private HBox mDefendingCards;
    ArrayList<Card> mModelAttackingCards;
    ArrayList<Card> mModelDefendingCards;

    public CardsOnTable(ArrayList<Card> pAttackingCards, ArrayList<Card> pDefendingCards) {
        mModelAttackingCards = pAttackingCards;
        mModelDefendingCards = pDefendingCards;
        mAttackingCards = new HBox();
        mDefendingCards = new HBox();
        setVBoxProperties();
    }

    private void setVBoxProperties() {
        mAttackingCards.setSpacing(10.0);
        mDefendingCards.setSpacing(10.0);
        setSpacing(-80.0);
        getChildren().addAll(mAttackingCards, mDefendingCards);
//        setAlignment(Pos.CENTER);
    }

    public void updateCardsOnTable() {
        mAttackingCards.getChildren().clear();
        mDefendingCards.getChildren().clear();
        updateCardsRow(mAttackingCards, mModelAttackingCards);
        updateCardsRow(mDefendingCards, mModelDefendingCards);
    }

    private ImageView addCardImage(Card pCard) {
        ImageView imageView = new ImageView("main/resources/images/"+ pCard.mFigure.getFigure() + "_" + pCard.mSuit.getColor() + ".png");
        return imageView;
    }

    private void updateCardsRow(HBox pCards, ArrayList<Card> pGivenCards) {
        for(Card card : pGivenCards) {
            pCards.getChildren().add(addCardImage(card));
        }
    }
}
