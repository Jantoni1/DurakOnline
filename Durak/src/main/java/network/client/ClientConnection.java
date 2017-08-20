package main.java.network.client;

/**
 * Created by Kuba on 21.07.2017.
 */
public interface ClientConnection extends MessageBox {
    void disconnect();
    void registerListener(Client.MessageListener pListener);
    void start();
}
