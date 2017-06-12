package main.java.controller.server;

import main.java.network.message.client.BaseClientMessage;
import main.java.network.message.client.HandShake;
import main.java.network.server.ClientThread;
import main.java.network.server.Lobby;

/**
 * Class created to process possible Handshake message, used by Server class exclusively
 */
public class ServerVisitor extends BaseServerVisitor {

    /**
     * Create new  ServerVisitor
     * @param mLobby visitor needs access to information about existing lobbies
     */
    public ServerVisitor(Lobby mLobby) {
        this.mLobby = mLobby;
    }

    /** If message is not an instance  of HandShake, ServerVisitor
     *
     * @param pClientThread message's author's thread
     * @param pBaseClientMessage message to ignore
     */
    @Override
    public void visit(ClientThread pClientThread, BaseClientMessage pBaseClientMessage) {}



    /**
     * If message is an instance of HandShake, ServerVisitor sets username for client who sent information
     * and sends back welcome message as well as list of existing rooms
     * @param pClientThread message's author's thread
     * @param pHandShake message to process
     */
    public void visit(ClientThread pClientThread, HandShake pHandShake) {
        pClientThread.setUserName(pHandShake.getUserName());
        mLobby.addClient(pClientThread);
    }

    Lobby mLobby;
}
