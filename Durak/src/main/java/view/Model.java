package main.java.view;

import main.java.model.client.Player;
import main.java.model.server.Card;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kuba on 24.07.2017.
 */
public interface Model {
    ArrayList<Integer> getAvailableCards();
    CopyOnWriteArrayList<Player> getPlayers();
    ArrayList<Card> getAttackingCards();
    ArrayList<Card> getDefendingCards();
    Player getMe();
    int getNumberOfPlayersInRoom();
    void reset();
//    boolean getFirstAttack();
}
