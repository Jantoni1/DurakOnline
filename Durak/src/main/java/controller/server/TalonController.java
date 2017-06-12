package main.java.controller.server;

import main.java.model.server.Card;
import main.java.model.server.Suit;
import main.java.model.server.Talon;
import main.java.model.server.Player;


import java.util.ArrayList;
import java.util.Collections;

class TalonController {

    public TalonController(Talon pTalon) {
        mTalon = pTalon;
    }

    public ArrayList<Card> dealCards(Player player) {
        ArrayList<Card> newCards = new ArrayList<>();
        while (player.mPlayersDeck.playersDeck.size() < MAX_CARDS && !mTalon.deck.isEmpty()) {
            newCards.add(mTalon.remove());
            player.mPlayersDeck.playersDeck.add(newCards.get(newCards.size() - 1));
        }
        return newCards;
    }

    public Suit shuffle() {
        if(mTalon.size() != FULL_DECK) {
            mTalon = new Talon();
        }
        Collections.shuffle(mTalon.deck);
        Card.trump = mTalon.get(0).mSuit;
        return Card.trump;
    }


    public Talon mTalon;
    private final static int MAX_CARDS;
    private final static int FULL_DECK;

    static {
        MAX_CARDS = 5;
        FULL_DECK = 52;
    }
}

