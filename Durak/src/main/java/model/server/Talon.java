package main.java.model.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Talon {

    public ArrayList<Card> deck;

    public Talon() {
        deck = new ArrayList<>();
        List<Figures> allFigures = Arrays.asList(Figures.values());
        List<Suit> allSuits = Arrays.asList(Suit.values());
        for(Suit color : allSuits) {
            if(color != Suit.NONE) {
                for(Figures figure: allFigures) {
                    if(deck.size() < 3) {
                        deck.add(new Card(figure, color));
                    }
                }
            }

        }
    }
    public int size() {
        return deck.size();
    }

    public Card get(int index) {
        return deck.get(index);
    }

    public boolean add(Card card) {
        return deck.add(card);
    }

    public Card remove() {
        return deck.remove(deck.size() - 1);
    }
}
