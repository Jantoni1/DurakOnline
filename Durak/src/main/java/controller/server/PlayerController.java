package main.java.controller.server;


import main.java.model.Card;
import main.java.model.Player;

public class PlayerController {

    public PlayerController(Player player) {
        this.player = player;
    }

    public Card playCard(int index) {
        return player.mPlayersDeck.playersDeck.remove(index);
    }

    public Player player;
}
