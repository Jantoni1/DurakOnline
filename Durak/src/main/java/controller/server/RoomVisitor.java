package main.java.controller.server;

import main.java.network.message.client.*;
import main.java.network.message.client.Chat;
import main.java.network.message.client.Leave;
import main.java.network.message.client.Play;
import main.java.network.message.client.Ready;
import main.java.network.message.server.*;
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
    public void visit(ClientThread pClientThread, BaseClientMessage baseClientMessage) {}

    public void onClientMessage(ClientThread pClientThread, BaseClientMessage pBaseClientMessage) {}

    public void visit(ClientThread pClientThread, main.java.network.message.client.Chat pChat) {
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Chat(pChat.getChatMessage())));
    }

    public void visit(ClientThread pClientThread, Ready pReady) {
        if(!mRoomController.mRoom.isStarted) {
            acceptReadyStatement(pClientThread.getID(), pReady.ismReadyIfTrueUnreadyOtherwise());
        }
    }

    private void acceptReadyStatement(int pClientID, boolean pReadyOrUnready) {
        if(pReadyOrUnready) {
            addReadyPlayer(pClientID);
        }
        else {
            removeUnreadyPlayer(pClientID);
        }
    }

    private void addReadyPlayer(int pClientID) {
        mRoomController.mRoom.mPlayersReady.add(pClientID);
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Ready(true)));
        if(mRoomController.mRoom.mPlayersReady.size() == mRoomController.getMaxPlayers()) {
            mRoomController.startGame();
        }
    }

    private void removeUnreadyPlayer(int pClientID) {
        mRoomController.mRoom.mPlayersReady.remove(pClientID);
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Ready(false)));
    }

    public void visit(ClientThread pClientThread, Play pPlay) {
        mRoomController.updateGameStatus(pClientThread, pPlay);
    }

    public void visit(ClientThread pClientThread, Leave leave) {
        if(leave.isFinal()) {
            mClients.remove(pClientThread);
        }
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
