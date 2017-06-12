package main.java.model.server;

public enum Suit {
    NONE(-1), SPADES(40), CLUBS(60),
    DIAMONDS(80), HEARTS(100);

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

