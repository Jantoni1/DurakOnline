package network.server;

import model.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Kuba on 27.05.2017.
 */
public class Lobby {

    private final Server server;
    private final int maxGames;

    private LinkedList<ClientThread> mClients;
    private ArrayList<GameLobby> mGameLobbies;

    public ArrayList<Pair> returnExistingRooms() {
        ArrayList<Pair> existingRooms = new ArrayList<>();
        mGameLobbies.forEach(gameLobby -> {if(gameLobby.ifFull()) {
            existingRooms.add(new Pair(gameLobby.getLobbyName(), gameLobby.getLobbyId()));
        }});
        return existingRooms;
    }

    public void addClient(ClientThread pClientThread) {}

    public Lobby(Server server, int maxGames) {
        this.server = server;
        this.maxGames = maxGames;
    }
}
