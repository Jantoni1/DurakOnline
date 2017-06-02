package main.java.network.server;


import main.java.network.message.client.BaseClientMessage;
import main.java.network.message.server.Welcome;
import main.java.network.message.client.Leave;
import main.java.network.message.server.BaseServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientThread extends Thread {
    private static int UID = 0;
    private final Socket mSocket;
    private final ClientConnectionListener mConnectionListener;
    private final List<ClientMessageListener> mListeners;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;
    private int mUID;
    private String mUserName;

    /**
     * Given socket create new client thread
     * Thread will manage connection and all the received and sent messages
     *
     * @param pSocket   client socket
     * @param pListener listener on connection status
     * @throws IOException Thrown when could not create object stream via socket
     */
    public ClientThread(Socket pSocket, ClientConnectionListener pListener) throws IOException {
        mSocket = pSocket;
        mConnectionListener = pListener;
        mUID = UID;
        UID += 1;
        mListeners = new CopyOnWriteArrayList<>();
        mUserName = "Player " + mUID;
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
    }

    /**
     * Server loop for reading message and passing it to every listener
     */

    @Override
    public void run() {
        while (!mSocket.isClosed() && mSocket.isConnected()) {
            try {
                final BaseClientMessage message = (BaseClientMessage) mObjectInputStream.readObject();
                System.out.println(message.toString());
                mListeners.forEach(l -> l.onClientMessage(this, message));

            } catch (SocketException e) {
                mConnectionListener.onDisconnect(this);
//                disconnect();
                break;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server crashed due to exception thrown in run() method.");
            }
        }
    }


    /**
     * During disconnect socket is being closed and all the data is erased
     * All listeners are sent leave message as well
     */
    public void disconnect() {
        try {
            mListeners.forEach(l -> l.onClientMessage(this, new Leave(true)));
            mSocket.close();
            mListeners.clear();
            mObjectInputStream.close();
            mObjectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send given message to client
     *
     * @param pServerMessage Message to be sent from server to client
     */
    public synchronized void sendMessage(BaseServerMessage pServerMessage) {
        try {
            mObjectOutputStream.writeObject(pServerMessage);
            mObjectOutputStream.reset();
        } catch (IOException e) {
            mConnectionListener.onDisconnect(this);
        }
    }

    /**
     * Register message listener
     *
     * @param pClientMessageListener listener
     */
    public void registerListener(ClientMessageListener pClientMessageListener) {
        mListeners.add(pClientMessageListener);
    }


    /**
     * Remove message listener
     *
     * @param pClientMessageListener listener
     */
    public void removeListener(ClientMessageListener pClientMessageListener) {
        mListeners.remove(pClientMessageListener);
    }



    /**
     * Set Client username
     *
     * @param pName username
     */
    public void setUserName(String pName) {
        mUserName = pName;
    }


    /**
     * Get clientID
     *
     * @return clientID
     */
    public int getID() {
        return mUID;
    }

    /**
     * Get username
     *
     * @return client's username
     */
    public String getUsername() {
        return mUserName;
    }

    public interface ClientMessageListener {

        void onClientMessage(ClientThread ClientThread, BaseClientMessage clientMessage);
    }

    public interface ClientConnectionListener {

        void onDisconnect(ClientThread clientThread);
    }
}
