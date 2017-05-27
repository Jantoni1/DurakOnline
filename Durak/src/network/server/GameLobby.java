package network.server;


import controller.RoomController;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for specyfic GameLobby
 */
public class GameLobby implements ClientThread.ClientMessageListener {

    private static int lobbyID = 0;
    private LinkedList<ClientThread> mClients;
    private final RoomController mRoomController;
    private GameLobbyChangeListener mListener;
    private String mLobbyName;
    private int mLobbyId;

    private Lobby mRootLobby;

    /**
     * Create new lobby
     *
     * @param pLobby      Main (root) lobby
     * @param pName       Lobby name
     * @param pMaxPlayers max players in game
     */
    public GameLobby(Lobby pLobby, String pName, int pMaxPlayers) {
        mRootLobby = pLobby;
        mClients = new LinkedList<>();
        mRoomController = new RoomController(pMaxPlayers);
        mLobbyName = pName;
        mLobbyId = lobbyID;
        lobbyID += 1;
    }

    public String getLobbyName() {
        return mLobbyName;
    }

    public int getLobbyId() {
        return mLobbyId;
    }

    public boolean isFull() {
        return mRoomController.isFull();
    }

    /**
     * Add given client to lobby
     *
     * @param pClient client to be added
     */
    public synchronized void addClient(ClientThread pClient) {
        if(!isFull() && !mRoomController.room.isStarted) {
            mClients.forEach(client -> {  });
        }
        mClients.add(pClient);
        pClient.registerListener(this);
        mRoomController.getPlayers().forEach(player -> pClient.sendMessage(new GameUpdateMessage(mGame)));
    }

    public int getID() {
        return mGame.getID();
    }

    /**
     * Get game
     *
     * @return Game object
     */
    public Game getGame() {
        return mGame;
    }

    /**
     * Set listener
     *
     * @param pListener listener
     */
    public void setGameLobbyChangeListener(GameLobbyChangeListener pListener) {
        mListener = pListener;
    }

    /**
     * Add new player to Game
     *
     * @param pName player name
     * @param pID   owner clientID
     */
    private void addPlayer(String pName, int pID) {
        Player p = mGame.addPlayer(pName, pID);
        if (p != null) {
            p.setName(pName + p.getID());
            propagateUpdate();
        }
    }

    /**
     * Update given player (replace player with the same ID with the given one)
     *
     * @param pPlayer new version of Player
     */
    private void updatePlayer(Player pPlayer) {
        mGame.getPlayers().replaceAll(p -> p.getID() == pPlayer.getID() ? pPlayer : p);
        propagateUpdate();
    }

    /**
     * Delete player from the lobby
     *
     * @param pPlayer player to delete
     */
    private void deletePlayer(Player pPlayer) {
        mGame.getColorBank().returnColor(pPlayer.getColor());
        mGame.getPlayers().removeIf(p -> p.getID() == pPlayer.getID());
        propagateUpdate();
    }

    /**
     * Remove Client from the Lobby
     *
     * @param pClient client to be removed
     */
    private void leaveGame(ClientThread pClient, boolean pReturnToLobby) {
        mGame.getPlayers().removeIf(player -> player.getOwner() == pClient.getID());
        mClients.remove(pClient);
        pClient.removeListener(this);
        if (mGame.getHostID() == pClient.getID()) {
            if (mClients.isEmpty()) {
                if (mListener != null)
                    mListener.onLobbyEmpty();
            } else {
                mGame.setHostID(mClients.getFirst().getID());
            }
        }
        if (pReturnToLobby)
            mRootLobby.addClient(pClient);

        propagateUpdate();
    }

    /**
     * Send upadte message to all clients, and notify main lobby to update game list
     */
    private void propagateUpdate() {
        mClients.forEach(client -> client.sendMessage(new GameUpdateMessage(mGame)));
        if (mListener != null) {
            mListener.onGameUpdate(mGame);
        }
    }

    /**
     * Send server text message to all the clients
     *
     * @param pMessage text to be sent
     */
    private void broadcast(String pMessage) {
        mClients.forEach(client -> client.sendMessage(new ServerTextMessage(pMessage)));
    }


    /**
     * Listener on client messages
     *
     * @param pClient  source client
     * @param pMessage message
     */
    @Override
    public synchronized void onClientMessage(ClientThread pClient, Message pMessage) {
        switch (pMessage.getType()) {
            case TEXT:
                mClients.forEach(clientThread -> clientThread.sendMessage(pMessage));
                break;
            case UPDATE_REQUEST:
                pClient.sendMessage(new GameUpdateMessage(mGame));
                break;
            case NAME_UPDATE_REQUEST:
                mGame.setName(((GameUpdateNameRequest) pMessage).getName());
                broadcast("Game name changed to: " + mGame.getName());
                propagateUpdate();
                break;

            case POWERUP_UPDATE:
                GamePowerUpStateRequest req = (GamePowerUpStateRequest) pMessage;
                mGame.setPowerUpEnabled(req.getPowerType(), req.getState());
                propagateUpdate();
                break;

            case PLAYER_ADD_REQUEST:
                addPlayer(((NewPlayerRequest) pMessage).getName(), pClient.getID());
                break;
            case PLAYER_UPDATE_REQUEST:
                updatePlayer(((PlayerUpdateRequest) pMessage).getPlayer());
                break;

            case PLAYER_DELETE:
                deletePlayer(((PlayerDeleteRequest) pMessage).getPlayer());
                break;

            case DISCONNECT:
                leaveGame(pClient, false);
                break;
            case LEAVE_GAME:
                leaveGame(pClient, true);
                break;

            case GAME_START_REQUEST:
                if (mGame.getPlayers().isEmpty())
                    break;

                new Timer().schedule(new TimerTask() {
                    private int times = 1;

                    @Override
                    public void run() {
                        mClients.forEach(clientThread -> clientThread.sendMessage(new TextMessage("Server", String.format("Game will start in %d...", times))));
                        if (times == 0) {
                            this.cancel();
                            mGame.getPlayers().forEach(player -> player.setPoints(0));
                            mClients.forEach(clientThread -> clientThread.sendMessage(new GameStartMessage(mGame)));
                            mThread = new GameThread(mGame, mClients);
                            mClients.forEach(client -> client.registerListener(mThread));
                        }
                        times--;
                    }
                }, 0, 1000);
                break;
        }
    }

    /**
     * Listener on GameLobby state changes (Game changes, lobby is empty notification)
     */
    public interface GameLobbyChangeListener {
        /**
         * Invoked on Game update
         *
         * @param pGame game
         */
        void onGameUpdate(Game pGame);

        /**
         * Invoked when last client leaves the GameLobby
         */
        void onLobbyEmpty();
    }

}