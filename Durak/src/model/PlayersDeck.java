package model;


import java.util.ArrayList;

public class PlayersDeck {
    public PlayersDeck() {
        playersDeck = new ArrayList<Card>();
    }

    public Card playCard(int cardIndex) {
            return playersDeck.remove(cardIndex);

    }

    public ArrayList<Integer> getAvailableCards(Card otherCard) {
        ArrayList<Integer> availableCards = new ArrayList<Integer>();
        for(Card card : playersDeck) {
            if(card.isBigger(otherCard)) {
                availableCards.add(playersDeck.indexOf(card));
            }
        }
        return availableCards;
    }

    public ArrayList<Card> playersDeck;
}
