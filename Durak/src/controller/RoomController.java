package controller;


import model.Room;

public class RoomController {


    public void sampleMethod() {
        synchronized(lock) {

        }
    }

    static {
        lock = new Object();
    }

    public RoomController(int pMaxPlayers) {
        mMaxPlayers = pMaxPlayers;
        mPlayersCollection = new PlayersCollectionController();
        talon = new TalonController();
        room = new Room();
    }

    public boolean isFull() {
        return mPlayersCollection.numberOfPlayers() == mMaxPlayers;
    }

    public PlayersCollectionController mPlayersCollection;
    private TalonController talon;
    public Room room;
    private final static Object lock;
    private final int mMaxPlayers;
}
