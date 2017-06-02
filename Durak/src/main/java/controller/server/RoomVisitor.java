package main.java.controller.server;

import main.java.network.message.client.*;
import main.java.network.message.server.End;
import main.java.network.server.ClientThread;
import main.java.network.server.GameRoom;

import java.util.LinkedList;

/**
 * Created by Kuba on 28.05.2017.
 */
public class RoomVisitor extends BaseServerVisitor {

    public RoomVisitor(RoomController pRoomController, LinkedList<ClientThread> pClients, GameRoom pGameRoom) {
        mRoomController = pRoomController;
        mClients = pClients;
        mGameRoom = pGameRoom;
    }

    @Override
    public void visit(BaseClientMessage baseClientMessage) {

    }

    @Override
    public void visit(ClientThread pClientThread, BaseClientMessage baseClientMessage) {}

    public void onClientMessage(ClientThread pClientThread, BaseClientMessage pBaseClientMessage) {}

    public void visit(ClientThread pClientThread, main.java.network.message.client.Chat pChat) {
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Chat(pChat.getChatMessage())));
    }

    public void visit(ClientThread pClientThread, Ready pReady) {
        if(!mRoomController.mRoom.isStarted) {
            mRoomController.mRoom.mPlayersReady.add(pClientThread.getID());
            if(mRoomController.mRoom.mPlayersReady.size() == mRoomController.getMaxPlayers()) {
                mRoomController.startGame();
            }
        }
    }

    public void visit(ClientThread pClientThread, Play pPlay) {
        mRoomController.updateDefenderGameStatus(pClientThread, pPlay);
    }

    public void visit(ClientThread pClientThread, Leave leave) {
        if(mRoomController.mRoom.isStarted) {
            mRoomController.resetRoom();
            mClients.forEach(client ->  client.sendMessage(new End(pClientThread.getID())));
        }
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Leave(pClientThread.getID())));
        mGameRoom.leaveGame(pClientThread, !leave.isFinal());
    }

    GameRoom mGameRoom;
    public RoomController mRoomController;
    private LinkedList<ClientThread> mClients;
}
