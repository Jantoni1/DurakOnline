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

    public ArrayList<Integer> getAvailableCards(ArrayList<Card> cardsOnTable) {
        ArrayList<Integer> availableCards = new ArrayList<>();
        for(Card card : playersDeck) {
            for(Card card1 : cardsOnTable) {
                if(card1.figure == card.figure) {
                    availableCards.add(playersDeck.indexOf(card1));
                }
            }
        }
        return availableCards;
    }
    public ArrayList<Card> playersDeck;
}
