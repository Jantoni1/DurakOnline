package model;

import java.util.ArrayList;


public class cardsOnTable {
    public cardsOnTable() {
        attackingCards = new ArrayList<>();
        defendingCards = new ArrayList<>();
    }

    public boolean addAttackingCard(Card card) {
        if(attackingCards.size() < MAX_CARDS) {
            attackingCards.add(card);
            return true;
        }
        return false;
    }

    public boolean addDefendingCard(Card card) {
        if(attackingCards.size() > defendingCards.size() &&
                card.isBigger(attackingCards.get(defendingCards.size()))) {
            defendingCards.add(card);
            return true;
        }
        return false;
    }

    public void giveCardsToLosingDefender(Player player) {
        player.talon.deck.addAll(defendingCards);
        player.talon.deck.addAll(attackingCards);
    }

    public void endTurn() {
        attackingCards.clear();
        defendingCards.clear();
    }

    boolean canAttack() {
        return attackingCards.size() < MAX_CARDS;
    }

    public ArrayList<Card> attackingCards;
    public ArrayList<Card> defendingCards;
    private final static int MAX_CARDS;

    static {
        MAX_CARDS = 5;
    }
}
