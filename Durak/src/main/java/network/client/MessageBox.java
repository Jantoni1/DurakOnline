package main.java.network.client;

import main.java.network.message.Message;

/**
 * Created by Kuba on 21.07.2017.
 */
public interface MessageBox {
    void sendMessage(Message pMessage);
}
