package model;


public class Player {

    public Player(int id, String nick) {
        this.id = id;
        this.nick = nick;
        this.talon = new Talon();
    }

    public boolean getPlayer(int player_id) {
        return id == player_id;
    }

    public boolean isPlaying() {
        return !talon.deck.isEmpty();
    }

    public String nick;
    public final int id;
    public Talon talon;
}
