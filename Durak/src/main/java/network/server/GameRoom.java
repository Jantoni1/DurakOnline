package main.java.network.server;


import main.java.controller.server.RoomController;
import main.java.controller.server.RoomVisitor;
import main.java.model.client.AnotherPlayer;
import main.java.model.server.Room;
import main.java.network.message.client.BaseClientMessage;
import main.java.network.message.server.Add;
import main.java.network.message.server.Enter;
import main.java.network.message.server.BaseServerMessage;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class for specyfic GameRoom
 */
public class GameRoom implements ClientThread.ClientMessageListener {

    private LinkedList<ClientThread> mClients;
    private Room mRoom;
    private final RoomController mRoomController;
    private GameLobbyChangeListener mListener;
    private RoomVisitor mRoomVisitor;

    private Lobby mRootLobby;

    /**
     * Create new lobby
     *
     * @param pLobby      ServerMain (root) lobby
     * @param pName       ClientMain name
     * @param pMaxPlayers max players in game
     */
    public GameRoom(Lobby pLobby, String pName, int pMaxPlayers) {
        mRootLobby = pLobby;
        mRoom = new Room(pName, pMaxPlayers);
        mClients = new LinkedList<>();
        mRoomController = new RoomController(mRoom, mClients);
        mRoomVisitor = new RoomVisitor(mRoomController, mClients, this);
    }

    public String getLobbyName() {
        return mRoomController.getRoomName();
    }

    public int getLobbyId() {
        return mRoomController.getRoomId();
    }

    public int getNumberOfPlayers() {
        return mClients.size();
    }

    public boolean isFull() {
        return mRoomController.mPCController.getmMaxPlayers() == mClients.size();
    }

    public int getMaxPlayers() {
        return mRoom.mMaxPlayers;
    }

    public void resetRoom() {
        String name = mRoom.mLobbyName;
        int id = mRoom.mLobbyId;
        mRoom = new Room(name, id);
        mRoomController.updateRoom(mRoom);
    }
    /**
     * Add given client to lobby
     *
     * @param pClient client to be added
     */
    public synchronized boolean addClient(ClientThread pClient) {
        if(!isFull() && !mRoomController.mRoom.isStarted) {
            mClients.add(pClient);
            pClient.sendMessage(createEnterMessage());
            mClients.forEach(client -> {
                if(client.getID() != pClient.getID())
                client.sendMessage(new Add(pClient.getUsername(), pClient.getID()));
            });
            pClient.registerListener(this);
            return true;
        }
        pClient.sendMessage(new Enter(mRoomController.getRoomName(), getNumberOfPlayers(), true));
        return false;
    }

    private Enter createEnterMessage() {
        Enter enterMessage = new Enter(mRoom.mLobbyName, mRoom.mMaxPlayers, false);
        ArrayList<AnotherPlayer> playersToAdd = new ArrayList<>();
        for(ClientThread client : mClients) {
            playersToAdd.add(new AnotherPlayer(client.getUsername(), client.getID()));
        }
        enterMessage.setmPlayers(playersToAdd);
        return enterMessage;
    }

    /** get room ID
     * @return room ID
     */
    public int getID() {
        return mRoomController.getRoomId();
    }

    /**
     * Get RoomController
     *
     * @return Game
     */
    public RoomController getGame() {
        return mRoomController;
    }

    /**
     * Set listener
     *
     * @param pListener listener
     */
    public void setGameLobbyChangeListener(GameLobbyChangeListener pListener) {
        mListener = pListener;
    }

    public GameLobbyChangeListener getGameLobbyChangeListener() {
        return mListener;
    }
//    /**
//     * Add new player to Game
//     *
//     * @param pName player name
//     * @param pID   owner clientID
//     */
//    private void addPlayer(String pName, int pID) {
//        Player p = mGame.addPlayer(pName, pID);
//        if (p != null) {
//            p.setName(pName + p.getID());
//            propagateUpdate();
//        }
//    }

//    /**
//     * Update given player (replace player with the same ID with the given one)
//     *
//     * @param pPlayer new version of Player
//     */
//    private void updatePlayer(Player pPlayer) {
//        mGame.getPlayers().replaceAll(p -> p.getID() == pPlayer.getID() ? pPlayer : p);
//        propagateUpdate();
//    }

//    /**
//     * Delete player from the lobby
//     *
//     * @param pPlayer player to delete
//     */
//    private void deletePlayer(Player pPlayer) {
//        mGame.getColorBank().returnColor(pPlayer.getColor());
//        mGame.getPlayers().removeIf(p -> p.getID() == pPlayer.getID());
//        propagateUpdate();
//    }

    /**
     * Remove Client from the Room
     *
     * @param pClient client to be removed
     */
    public void leaveGame(ClientThread pClient, boolean pReturnToLobby) {
        mClients.remove(pClient);
        pClient.removeListener(this);
            if (mListener != null) {
                if(mClients.isEmpty()) {
                    mListener.onLobbyEmpty();
                }
                else {
                    mListener.onGameUpdate(this);
                }
            }
        if (pReturnToLobby)
            mRootLobby.addClient(pClient);
//        propagateUpdate();
    }

    /**
     * Send upadte message to all clients, and notify main lobby to update game list
     */
    public void propagateUpdate(BaseServerMessage pBaseServeMessage, boolean ifUpdateLobby) {
        mClients.forEach(client -> client.sendMessage(pBaseServeMessage));
        if (mListener != null) {
            mListener.onGameUpdate(this);
        }
    }



    /**
     * Listener on client messages activates visitor to process the message
     *
     * @param pClient  source client
     * @param pMessage message
     */
    public synchronized void onClientMessage(ClientThread pClient, BaseClientMessage pMessage) {
        pMessage.accept(pClient, mRoomVisitor);
    }


    /**
     * Listener on GameRoom state changes (Game changes, lobby is empty notification)
     */
    public interface GameLobbyChangeListener {
        /**
         * Invoked on Game update
         *
         * @param pRoom game
         */
        void onGameUpdate(GameRoom pRoom);

        /**
         * Invoked when last client leaves the GameRoom
         */
        void onLobbyEmpty();
    }

}