package main.java.model.server;


public class Player {

    public Player(int id, String nick) {
        this.id = id;
        this.mPlayersDeck = new PlayersDeck();
        this.nick = nick;
    }

    public boolean getPlayer(int player_id) {
        return id == player_id;
    }

    public boolean isPlaying() {
        return !mPlayersDeck.playersDeck.isEmpty();
    }

    public final int id;
    public PlayersDeck mPlayersDeck;
    public String nick;
}
