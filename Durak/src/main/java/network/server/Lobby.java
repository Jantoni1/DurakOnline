package main.java.network.server;

import main.java.controller.server.LobbyVisitor;
import main.java.model.server.RoomInfo;
import main.java.network.message.Message;
import main.java.network.message.client.CreateRoom;
import main.java.network.message.server.ExistingRooms;
import main.java.network.message.server.RoomUpdate;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Kuba on 27.05.2017.
 */
public class Lobby implements ClientThread.ClientMessageListener {

    private final Server mServer;
    private final int mMaxGames;
    private LobbyVisitor mLobbyVisitor;

    private LinkedList<ClientThread> mClients;
    private ArrayList<GameRoom> mGameLobbies;

    /**
     * Create new lobby within a server
     *
     * @param pServer   server
     * @param pMaxGames maximum number of games
     */
    public Lobby(Server pServer, int pMaxGames) {
        mServer = pServer;
        mMaxGames = pMaxGames;
        mClients = new LinkedList<>();
        mGameLobbies = new ArrayList<>(mMaxGames);
        mLobbyVisitor = new LobbyVisitor(this, mClients, mGameLobbies);
    }


    public ArrayList<RoomInfo> returnExistingRooms() {
        ArrayList<RoomInfo> existingRooms = new ArrayList<>();
        mGameLobbies.forEach(gameRoom -> {
            existingRooms.add(new RoomInfo(gameRoom.getLobbyName(), gameRoom.getLobbyId(), gameRoom.getNumberOfPlayers(), gameRoom.getMaxPlayers()));
        });
        return existingRooms;
    }

    /**
     * Add new client to lobby
     *
     * @param pClientThread {@link ClientThread} of client
     */
    public void addClient(ClientThread pClientThread) {
        pClientThread.sendMessage(new ExistingRooms(returnExistingRooms()));
        mClients.add(pClientThread);
        pClientThread.registerListener(this);
    }

    /**
     * Try to create new game
     *
     * @param pCreateRoom message containing information about room name, number of players and size
     * @return GameRoom on success, null if failed
     */
    public GameRoom createGame(CreateRoom pCreateRoom) {
        if (mGameLobbies.size() < mMaxGames) {
            GameRoom room = new GameRoom(this, pCreateRoom.getmRoomName(), pCreateRoom.getmMaxPlayers());
            mGameLobbies.add(room);
            mClients.forEach(client ->
                    client.sendMessage(new RoomUpdate(new RoomInfo(room.getLobbyName(),room.getID(), 1, room.getMaxPlayers()),true, false))
            );
            room.setGameLobbyChangeListener(new GameRoom.GameLobbyChangeListener() {
                @Override
                public void onGameUpdate(GameRoom pGameRoom) {
                    mClients.forEach(client -> client.sendMessage(new RoomUpdate(
                            new RoomInfo(pGameRoom.getLobbyName(), pGameRoom.getID(), pGameRoom.getNumberOfPlayers(), pGameRoom.getMaxPlayers()),false,  false)));
                }

                @Override
                public void onLobbyEmpty() {
                    mClients.forEach(client -> client.sendMessage(new RoomUpdate(
                            new RoomInfo(room.getLobbyName(), room.getID(),  room.getNumberOfPlayers(),room.getMaxPlayers()), false, true)));
                    mGameLobbies.remove(room);
                }
            });
            return room;
        }
        return null;
    }

    /**
     * Client message Listener
     *
     * @param pClient        message author
     * @param pClientMessage message from client
     */
    @Override
    public synchronized void onClientMessage(ClientThread pClient, Message pClientMessage) {
        pClientMessage.accept(pClient, mLobbyVisitor);
    }


}
