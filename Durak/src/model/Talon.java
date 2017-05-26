package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Talon {

    public ArrayList<Card> deck;

    public Talon() {
        List<Figures> figures = Arrays.asList(Figures.values());
        List<Suit> colors = Arrays.asList(Suit.values());
        for(Suit color : colors) {
            for(Figures figure: figures) {
                deck.add(new Card(figure.getFigure(), color.getColor()));
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
