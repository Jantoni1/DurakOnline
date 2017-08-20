package main.java.controller.server;

import main.java.controller.Visitor;
import main.java.network.message.Message;
import main.java.network.message.client.*;
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
public class RoomVisitor extends Visitor {

    GameRoom mGameRoom;
    public RoomController mRoomController;
    private LinkedList<ClientThread> mClients;

    public RoomVisitor(RoomController pRoomController, LinkedList<ClientThread> pClients, GameRoom pGameRoom) {
        mRoomController = pRoomController;
        mClients = pClients;
        mGameRoom = pGameRoom;
    }

    @Override
    public void visit(ClientThread pClientThread, Message baseClientMessage) {}

    public void onClientMessage(ClientThread pClientThread, Message pMessage) {}

    public void visit(ClientThread pClientThread, main.java.network.message.client.Chat pChat) {
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Chat(pClientThread.getUsername(), pChat.getChatMessage())));
    }

    public void visit(ClientThread pClientThread, Ready pReady) {
        if(!mRoomController.mRoom.isStarted) {
            acceptReadyStatement(mClients.indexOf(pClientThread), pClientThread.getID(), pReady.ismReadyIfTrueUnreadyOtherwise());
        }
    }

    private void acceptReadyStatement(int pIndex, int pClientID, boolean pReadyOrUnready) {
        if(pReadyOrUnready) {
            addReadyPlayer(pIndex, pClientID);
        }
        else {
            removeUnreadyPlayer(pIndex);
        }
    }

    private void addReadyPlayer(int pIndex, int pClientID) {
        mRoomController.mRoom.mPlayersReady.set(pIndex, pClientID);
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Ready(true)));
        if(!mRoomController.mRoom.mPlayersReady.contains(-1)) {
            mRoomController.startGame();
        }
    }

    private void removeUnreadyPlayer(int pIndex) {
        mRoomController.mRoom.mPlayersReady.set(pIndex, -1);
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Ready(false)));
    }

    public void visit(ClientThread pClientThread, Play pPlay) {
        mRoomController.updateGameStatus(pClientThread.getID(), pPlay);
    }

    public void visit(ClientThread pClientThread, Leave leave) {
        if(leave.isFinal()) {
            mClients.remove(pClientThread);
        }
        if(mRoomController.mRoom.isStarted) {
            mRoomController.resetRoom();
            mClients.forEach(client ->  client.sendMessage(new End(pClientThread.getID(), pClientThread.getUsername())));
        }
        mClients.forEach(client -> client.sendMessage(new main.java.network.message.server.Leave(pClientThread.getID())));
        mGameRoom.leaveGame(pClientThread, !leave.isFinal());
    }


    public interface Client {
        void sendMessage(Message pServerMessage);
    }
}
