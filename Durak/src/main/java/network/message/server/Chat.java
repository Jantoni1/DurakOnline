package main.java.network.message.server;


import main.java.controller.client.BaseClientVisitor;
import main.java.controller.client.ClientConnectionVisitor;

public class Chat extends BaseServerMessage {

    public Chat(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void accept(BaseClientVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionVisitor pClientVisitor) {}

    private static final long serialVersionUID = 16L;

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public Chat(String playerName, String playerId, String chatMessage) {
        this.playerName = playerName;
        this.playerId = playerId;
        this.chatMessage = chatMessage;
    }

    private String playerName;
    private String playerId;
    private String chatMessage;
}
