package main.java.controller.server;

import main.java.network.message.client.Add;
import main.java.network.message.client.BaseClientMessage;
import main.java.network.message.client.CreateRoom;
import main.java.network.message.server.Enter;
import main.java.network.server.ClientThread;
import main.java.network.server.GameRoom;
import main.java.network.server.Lobby;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Kuba on 28.05.2017.
 */
public class LobbyVisitor extends BaseServerVisitor {

    /**
     * Create visitor used exclusively by main Lobby. It processes messages meant for Lobby only
     * @param mLobby        lobby which uses visitor
     * @param mClients
     * @param mGameLobbies
     */

    public LobbyVisitor(Lobby mLobby, LinkedList<ClientThread> mClients, ArrayList<GameRoom> mGameLobbies) {
        this.mLobby = mLobby;
        this.mClients = mClients;
        this.mGameLobbies = mGameLobbies;
    }

    @Override
    public void visit(BaseClientMessage baseClientMessage) {}

    @Override
    public void visit(ClientThread pClientThread, BaseClientMessage baseClientMessage) {}

//    @Override
    public void visit(ClientThread pClient, CreateRoom pCreateRoom) {
        System.out.println("HALKO");

        GameRoom room = mLobby.createGame(pCreateRoom);
        if (room != null) {
            room.addClient(pClient);
            mClients.remove(pClient);
            pClient.removeListener(mLobby);
        }
        else {
            pClient.sendMessage(new Enter(room.getLobbyName(), room.getID(), room.getMaxPlayers(), true));
        }
    }

    public void visit(ClientThread pClient, Add pAdd) {
        System.out.println("eh xD");
        mGameLobbies.stream()
                .filter(gameRoom -> pAdd.getRoomId() == gameRoom.getID())
                .findFirst()
                .ifPresent(room -> {
                    room.addClient(pClient);
                    room.getGameLobbyChangeListener().onGameUpdate(room);
                    mClients.remove(pClient);
                    pClient.removeListener(mLobby);
                    return;
                });
        pClient.sendMessage(new Enter("failed", pAdd.getRoomId(), 4, true));
    }

    private Lobby mLobby;
    private LinkedList<ClientThread> mClients;
    private ArrayList<GameRoom> mGameLobbies;

}
