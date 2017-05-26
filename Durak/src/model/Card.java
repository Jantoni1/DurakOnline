package model;

public class Card {


    Card(int figure, int color) {
        this.figure.setFigure(figure);
        this.color.setColor(color);
    }
    Card(Card other) {
        figure = other.figure;
        color = other.color;
    }
    boolean isBigger(Card other) {
        if(this.color.equals(other.color)) {
            return this.figure.isBigger(other.figure);
        }
        else if(other.color == trump) {
            return true;
        }
        return false;
    }
    public Figures figure;
    public Suit color;
    public static Suit trump;
}
