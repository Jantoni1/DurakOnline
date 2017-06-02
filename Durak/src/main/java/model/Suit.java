package main.java.model;

public enum Suit {
    NONE(-1), SPADES(0), CLUBS(1),
    DIAMONDS(2), HEARTS(3);

    private int color;
    Suit(int color) {
        this.color = color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public boolean equals(Suit other) {
        return this.color == other.color;
    }
    public int getColor() {
        return color;
    }
}

