package controller;


import model.Player;

import java.util.ArrayList;

public class PlayersCollectionController {
    public PlayersCollectionController() {
        this.players = new ArrayList<>();
        this.attackerIndex = 0;
    }

    public boolean addPlayer(int player_id, String nick) {
        if(players.size() >= MAX_PLAYERS) {
            return false;
        }
        return players.add(new PlayerController(new Player(player_id, nick)));
    }

    public boolean removePlayer(int player_id) {
        try {
            players.remove(findPlayer(player_id));
            return true;
        }
        catch (IndexOutOfBoundsException exception) {
            return false;
        }
    }

    public Player findPlayer(int player_id) {
        for (PlayerController player: players) {
            if(player.player.getPlayer(player_id)) {
                return player.player;
            }
        }
        throw new IndexOutOfBoundsException("No player with given id found.");
    }

    public int getNextPlayer(int player_id) {
        return players.indexOf(findPlayer(player_id)) % players.size();
    }

    public Player getAttacker(int shift) {
        int i = 0;
        while(i<shift) {
            if(players.get(++attackerIndex % MAX_PLAYERS).player.isPlaying()) {
                ++i;
            }
        }
        return players.get(attackerIndex).player;
    }

    private ArrayList<PlayerController> players;
    private int attackerIndex;
    private final static int MAX_PLAYERS;

    static {
        MAX_PLAYERS = 4;
    }

}
