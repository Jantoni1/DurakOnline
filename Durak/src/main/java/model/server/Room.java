package main.java.model.server;



import java.util.ArrayList;

public class Room {

    public Talon mTalon;
    public ArrayList<Player> mPlayerArrayList;
    public CardsOnTable mCardsOnTable;
    public boolean isStarted;
    private static int mLobbyID = 0;
    public  final String mLobbyName;
    public final int mLobbyId;
    public final int mMaxPlayers;
    public final ArrayList<Integer> mPlayersReady;

    public Room(String pLobbyName, int pMaxPlayers) {
        this.isStarted = false;
        mTalon = new Talon();
        mPlayerArrayList = new ArrayList<>();
        mCardsOnTable = new CardsOnTable();
        isStarted = false;
        mLobbyName = pLobbyName;
        mMaxPlayers = pMaxPlayers;
        mLobbyId = mLobbyID;
        mLobbyID += 1;
        mPlayersReady = new ArrayList<>();
    }


}
