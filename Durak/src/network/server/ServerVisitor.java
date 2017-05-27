package network.server;

import network.message.client.BaseClientMessage;
import network.message.client.HandShake;
import network.message.server.ExistingRooms;
import network.message.server.Welcome;

/**
 * Class created to process possible Handshake message, used by Server class exclusively
 */
public class ServerVisitor {

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
    public void visit(ClientThread pClientThread, BaseClientMessage pBaseClientMessage) {
    }

    /**
     * If message is an instance of HandShake, ServerVisitor sets username for client who sent information
     * and sends back welcome message as well as list of existing rooms
     * @param pClientThread message's author's thread
     * @param pHandShake message to process
     */
    public void visit(ClientThread pClientThread, HandShake pHandShake) {
        pClientThread.setUserName(pHandShake.getUserName());
        pClientThread.sendMessage(new Welcome(pClientThread.getID()));
        pClientThread.sendMessage(new ExistingRooms(mLobby.returnExistingRooms()));
        mLobby.addClient(pClientThread);
    }

    Lobby mLobby;
}
