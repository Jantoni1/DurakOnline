package main.java.model.server;


import java.util.ArrayList;

public class PlayersDeck {

    public ArrayList<Card> playersDeck;

    PlayersDeck() {
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

    private void lookForPossibleCards(ArrayList<Card> pCardsOnTable, ArrayList<Integer> pAvailableCards) {
        for(Card card : playersDeck) {
            for(Card card1 : pCardsOnTable) {
                if(card1.mFigure == card.mFigure) {
                    pAvailableCards.add(playersDeck.indexOf(card));
                }
            }
        }
    }

    private boolean noCardsOnTheTable(boolean trueIfNoCardsOnTable, ArrayList<Integer> pAvailableCards) {
        if(trueIfNoCardsOnTable) {
            for(Card card : playersDeck) {
                pAvailableCards.add(playersDeck.indexOf(card));
            }
            return true;
        }
        return false;
    }

    public ArrayList<Integer> getAvailableCards(ArrayList<Card> pAttackingCardsOnTable, ArrayList<Card> pDefendingCardsOnTable) {
        ArrayList<Integer> pAvailableCards = new ArrayList<>();
        if(!noCardsOnTheTable(pAttackingCardsOnTable.isEmpty() && pDefendingCardsOnTable.isEmpty(), pAvailableCards)) {
            lookForPossibleCards(pAttackingCardsOnTable, pAvailableCards);
            lookForPossibleCards(pDefendingCardsOnTable, pAvailableCards);
        }
        return pAvailableCards;
    }
}
