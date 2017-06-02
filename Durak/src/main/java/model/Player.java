package main.java.model;


public class Player {

    public Player(int id) {
        this.id = id;
        this.mPlayersDeck = new PlayersDeck();
    }

    public boolean getPlayer(int player_id) {
        return id == player_id;
    }

    public boolean isPlaying() {
        return !mPlayersDeck.playersDeck.isEmpty();
    }

    public final int id;
    public PlayersDeck mPlayersDeck;
}
