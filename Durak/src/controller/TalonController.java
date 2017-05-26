package controller;

import model.Card;
import model.Suit;
import model.Talon;
import model.Player;


import java.util.Collections;

public class TalonController {

    public TalonController() {
        this.talon = new Talon();
    }

    public boolean dealCards(Player player) {
        if(talon.deck.isEmpty()) {
            return false;
        }
        while (player.talon.size() <= MAX_CARDS) {
            player.talon.add(talon.remove());
        }
        return true;
    }
    public Suit shuffle() {
        if(talon.size() != FULL_DECK) {
            talon = new Talon();
        }
        Collections.shuffle(talon.deck);
        Card.trump = talon.get(0).color;
        return Card.trump;
    }

    private Talon talon;
    private final static int MAX_CARDS;
    private final static int FULL_DECK;

    static {
        MAX_CARDS = 5;
        FULL_DECK = 52;
    }
}

