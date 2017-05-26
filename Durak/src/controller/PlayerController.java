package controller;


import model.Card;
import model.Player;

public class PlayerController {

    public PlayerController(Player player) {
        this.player = player;
    }

    public Card playCard(int index) {
        return player.talon.get(index);
    }

    public Player player;
}
