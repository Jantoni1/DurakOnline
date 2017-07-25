package main.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import main.java.controller.client.ClientManager;
import main.java.model.client.Player;
import main.java.model.client.Room;
import main.java.network.client.Client;
import main.java.network.client.ClientConnection;
import main.java.network.client.MessageBox;
import main.java.network.message.Message;
import main.java.view.RoomScene;

import java.io.IOException;
import java.util.ArrayList;
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
            public void sendMessage(Message pMesssage) {}
        };
        CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
        for(int i = 0; i < maxPlayers; ++i) {
            players.add(new Player("player" + i, i));
            players.get(i).setNumberOfCards(13);
        }
        Room room = new Room("testing room", maxPlayers, players, 0) ;
        RoomScene roomScene = new RoomScene(room, maxPlayers, messageBox);
        roomScene.resetPlayersViewProperty(room.getPlayers(), true);
        roomScene.activateReadyPanel(0);
        primaryStage.setScene(roomScene.getRoomScene());
        primaryStage.show();
    }
}

