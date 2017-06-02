package main.java.model;


import java.util.ArrayList;

public class PlayersDeck {
    public PlayersDeck() {
        playersDeck = new ArrayList<>();
    }

    public Card playCard(int cardIndex) {
            return playersDeck.remove(cardIndex);
    }

    public ArrayList<Integer> getAvailableCards(Card otherCard) {
        ArrayList<Integer> availableCards = new ArrayList<>();
        if(otherCard == null) {
            for(Card card : playersDeck) {
                availableCards.add(playersDeck.indexOf(card));
            }
            return availableCards;
        }
        for(Card card : playersDeck) {
            if(card.isBigger(otherCard)) {
                availableCards.add(playersDeck.indexOf(card));
            }
        }
        return availableCards;
    }

    public ArrayList<Integer> getAvailableCards(ArrayList<Card> cardsOnTable) {
        ArrayList<Integer> availableCards = new ArrayList<>();
        for(Card card : playersDeck) {
            for(Card card1 : cardsOnTable) {
                if(card1.mFigure == card.mFigure) {
                    availableCards.add(playersDeck.indexOf(card1));
                }
            }
        }
        return availableCards;
    }
    public ArrayList<Card> playersDeck;
}
