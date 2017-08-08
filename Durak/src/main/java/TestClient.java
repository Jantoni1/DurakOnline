package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.model.client.Player;
import main.java.model.client.Room;
import main.java.model.server.Card;
import main.java.model.server.Figures;
import main.java.model.server.Suit;
import main.java.network.client.MessageBox;
import main.java.network.message.Message;
import main.java.network.message.client.Chat;
import main.java.view.room_scene.RoomScene;

import java.util.concurrent.CopyOnWriteArrayList;

public class TestClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            setUpStage(primaryStage);
            initializeClient(primaryStage);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    private void setUpStage(Stage primaryStage) {
        primaryStage.setTitle("DurakOnline");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
    }

    private void initializeClient(Stage primaryStage) {
        int maxPlayers = 4;
        MessageBox messageBox = new MessageBox() {
            @Override
            public void sendMessage(Message pMessage) {
                if(pMessage.getClass().equals(Chat.class)) {
                    mRoomScene.addChatMessage("player1", ((Chat)pMessage).getChatMessage());
                }
            }
        };
        CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
        for(int i = 0; i < maxPlayers; ++i) {
            players.add(new Player("player" + i, i));
            players.get(i).setNumberOfCards(52);
        }
        for(int i = 0; i < 26; ++i) {
            players.get(0).addCard(new Card(Figures.ACE, Suit.CLUBS));
        }
        players.get(0).addCard(new Card(Figures.ACE, Suit.DIAMONDS));
        Room room = new Room("testing room", maxPlayers, players, 0) ;
        room.getAttackingCards().add(new Card(Figures.ACE, Suit.CLUBS));
        room.getAttackingCards().add(new Card(Figures.FIVE, Suit.DIAMONDS));
        room.getAttackingCards().add(new Card(Figures.ACE, Suit.CLUBS));
        room.getAttackingCards().add(new Card(Figures.FIVE, Suit.DIAMONDS));
        room.getAttackingCards().add(new Card(Figures.ACE, Suit.CLUBS));
        room.getDefendingCards().add(new Card(Figures.SIX, Suit.DIAMONDS));
        mRoomScene = new RoomScene(room, maxPlayers, messageBox);
        mRoomScene.resetPlayersViewProperty();
        mRoomScene.activateReadyPanel(0);
        mRoomScene.updateCardsOnTable();
        for(int i = 0; i<30; ++i) {
            mRoomScene.addChatMessage("player1", "elko xDD");
        }
        primaryStage.setScene(mRoomScene.getRoomScene());
        primaryStage.show();
    }

    private RoomScene mRoomScene;
}

