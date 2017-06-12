package main.java.network.server;

import main.java.controller.server.ServerVisitor;
import main.java.network.message.client.BaseClientMessage;
import main.java.network.message.server.Welcome;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * ServerMain server thread - accepts new connections, and sends them to lobby
 */
public class Server extends Thread implements ClientThread.ClientMessageListener {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ServerSocket mServerSocket;
    private LinkedList<ClientThread> mClients;
    private Lobby mLobby;
    private ServerVisitor mServerVisitor;

    /**
     * Create new server thread
     *
     * @param pPort     server port
     * @param pMaxGames maximum count of games running simultaneously
     * @throws IOException thrown if could not create socket
     */
    public Server(int pPort, int pMaxGames) throws IOException {
        mServerSocket = new ServerSocket(pPort);
        mClients = new LinkedList<>();
        mLobby = new Lobby(this, pMaxGames);
        mServerVisitor = new ServerVisitor(mLobby);
    }

    /**
     * Stop accepting new connections and disconnect form all the clients
     */
    public void close() {
        try {
            mServerSocket.close();
            mClients.forEach(ClientThread::disconnect);
            System.out.println("Closed server socket");
        } catch (Exception e) {
            System.out.println("Could not stop server socket");
        }
    }

    /**
     * Thread main function - accept new connections, create client threads for them and register to Handshake message listener
     */
    @Override
    public void run() {
        while (!mServerSocket.isClosed()) {
            try {
                Socket clientSocket = mServerSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket, client -> {
                    client.disconnect();
                    mClients.remove(client);
                });
                clientThread.registerListener(this);
                clientThread.start();
                clientThread.sendMessage(new Welcome(clientThread.getID()));
                mClients.add(clientThread);
            } catch (IOException e) {
                System.out.println("Could not accept client: " + e.getMessage());
            }
        }
    }

    /**
     * Listener on client messages - if Handshake is sent, sends back Welcome message, otherwise ignores message
     * Calls visitor that checks the type of message
     * @param pClientThread Sender client thread
     * @param pClientMessage message from client to server
     */
    @Override
    public synchronized void onClientMessage(ClientThread pClientThread, BaseClientMessage pClientMessage) {
        pClientMessage.accept(pClientThread, mServerVisitor);
    }

}
