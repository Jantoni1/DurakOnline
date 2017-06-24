package main.java.network.client;

import main.java.network.message.Message;
import main.java.network.message.Message;
import main.java.network.message.server.Disconnected;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kuba on 28.05.2017.
 */

/**
 * Running thread for interacting wt server-side va Socket connection
 */
public class Client extends Thread {
    private Socket mSocket;
    private final ObjectOutputStream mObjectOutputStream;
    private final ObjectInputStream mObjectInputStream;
    private final CopyOnWriteArrayList<MessageListener> mListeners;

    /**
     * Create connector connected to given server
     *
     * @param pIP   Server IP
     * @param pPort Server Port
     * @throws IOException thrown on connection error
     */
    public Client(String pIP, int pPort) throws IOException {
        mSocket = new Socket(pIP, pPort);
        mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
        mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());
        mListeners = new CopyOnWriteArrayList<>();
    }

    /**
     * Send message to server
     *
     * @param pClientMessage message to sent
     */
    public synchronized void sendMessage(Message pClientMessage) {
        try {
            mObjectOutputStream.writeObject(pClientMessage);
            mObjectOutputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register listener on messages received from server
     *
     * @param pListener listener
     */
    public void registerListener(MessageListener pListener) { mListeners.add(pListener);
    }

    /**
     * Remove registered listener
     *
     * @param pListener listener to be removed
     */
    public void unregisterListener(MessageListener pListener) {
        mListeners.remove(pListener);
    }

    /**
     * Send handshake message to server
     *
     * @param pName     username
     * @param pUserID handshake callback function
     */


    /**
     * Thread run function
     * Receives messages from server
     */
    @Override
    public void run() {
        while (!mSocket.isClosed()) {
            try {
                Message message = (Message) mObjectInputStream.readObject();
                Thread thread = new Thread() {
                    public void run() {
                        for (MessageListener listener : mListeners){
                            listener.onClientMessage(message);
                        }
                    }
                };
                thread.start();
            }
            catch (ClassNotFoundException | IOException e) {
//                System.out.println("Client infinite loop crashed for some reason.");
//                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Disconnects fromm the server
     */
    public synchronized void disconnect() {
        mListeners.forEach(l -> l.onClientMessage(new Disconnected()));
        try {
            mSocket.close();
        } catch (Exception e) {
            System.out.println("Could not disconnect");
        }
    }

    /**
     * Client message listener
     */
    public interface MessageListener {
        /**
         * Invoked on message received
         *
         * @param pServerMessage received message
         */
        void onClientMessage(Message pServerMessage);
    }

//    /**
//     * Handshake result listener
//     */
////    public interface HandshakeResultListener {
////        /**
////         * Invoked on handshake message accept
////         *
////         * @param pID userID received from server
////         */
////        void onHandshakeResult(int pID);
////    }
}

