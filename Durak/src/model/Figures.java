package model;

/**
 * Created by Kuba on 23.05.2017.
 */
public enum Figures  {
    TWO(2), THREE(3), FOUR(4), FIVE(5),
    SIX(6), SEVEN(7), EIGHT(8), NINE(9),
    TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

    private int figure;
    Figures(int figure) {
        this.figure = figure;
    }
    public boolean isBigger(Figures other) {
        return figure > other.figure;
    }
    public void setFigure(int figure) {
        this.figure = figure;
    }
    public int getFigure() {
        return figure;
    }
}
