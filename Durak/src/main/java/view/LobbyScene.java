package main.java.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import main.java.controller.client.ClientManager;
import main.java.model.server.RoomInfo;
import main.java.network.message.server.RoomUpdate;

import java.util.ArrayList;

/**
 * Created by Kuba on 31.05.2017.
 */
public class LobbyScene {
    private ClientManager mClientManager;
    private Scene mLobbyScene;
    private Boolean mLock;
    private RoomInfo mChosenRoom;
    private ArrayList<RoomInfo> mRooms;
//    private ArrayList<Button> mButtons;
    private GridPane mGrid;
    private int column;
    private int row;

    public LobbyScene(ClientManager pClientManager) {
//        mButtons = new ArrayList<>();
        mClientManager = pClientManager;
        mLock = new Boolean(true);
        column = 0;
        row = 0;
    }

    public Scene getLobbyScene() {
        return mLobbyScene;
    }

    public void createRoomList(ArrayList<RoomInfo> rooms) {
        createLobbyScene(rooms);
        mRooms = rooms;
    }

    public void updateRooms(RoomUpdate pRoomUpdate) {
        if(pRoomUpdate.isRoomCreated()) {
            addRoom(pRoomUpdate);
        }
        else if(pRoomUpdate.isRoomDeleted()) {
            removeRoom(pRoomUpdate);
        }
        else {
            modifyRoom(pRoomUpdate);
        }
    }

    private void addRoom(RoomUpdate pRoomUpdate) {
        mRooms.add(pRoomUpdate.getmRoomInfo());
        createLobbyScene(mRooms);
    }

    private void removeRoom(RoomUpdate pRoomUpdate) {
        if(isItUpdatedRoom(pRoomUpdate.getmRoomInfo().getmRoomId())) {
            createLobbyScene(mRooms);
        }
    }

    private boolean isItUpdatedRoom(int pRoomId) {
        return mRooms.removeIf(roomInfo -> roomInfo.getmRoomId() == pRoomId);
    }

    private void modifyRoom(RoomUpdate pRoomUpdate) {

        mRooms.forEach(room -> {
            if(isThisTheRoomToUpdate(room, pRoomUpdate)) {
                mRooms.set(mRooms.indexOf(room), pRoomUpdate.getmRoomInfo());
                createLobbyScene(mRooms);
            }
        });
    }


    private boolean isThisTheRoomToUpdate(RoomInfo pCurrentRoom, RoomUpdate pRoomToUpdate) {
        return pCurrentRoom.getmRoomId() == pRoomToUpdate.getmRoomInfo().getmRoomId();
    }

    private void createLobbyScene(ArrayList<RoomInfo> pRoomInfo) {
        column = 0;
        row = 0;
        mLobbyScene = new Scene(createSceneRoot(pRoomInfo), 1200, 800, Color.AZURE);
        mLobbyScene.getStylesheets().add("main/resources/LobbyScene.css");
//        mLobbyScene.getStylesheets().add("main/resources/stylesheet.css");
    }

    private StackPane createSceneRoot(ArrayList<RoomInfo> pRoomInfo) {
        final StackPane root = new StackPane();
        root.getChildren().addAll(createBackgroundImage(),createRoomScrollPane(pRoomInfo));
        root.setStyle("-fx-background-color: rgba(0, 255, 0, 0.3);");
        return root;
    }

    private ImageView createBackgroundImage() {
        ImageView backgroundImage = new ImageView("main/resources/background.jpg");
        backgroundImage.setVisible(true);
        return backgroundImage;
    }

    private Button createNewGameButton() {
        Button button = createRoomButton(new RoomInfo("New Room", -1, 0, 2));
        button.setId("newgame");
        button.setText("CREATE ROOM");
        return button;
    }

    private void setButtonAction(Button pButton, RoomInfo pRoomInfo) {
        pButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                synchronized(mLock) {
                    if(pRoomInfo.getmRoomId() != -1) {
                        mClientManager.sendAddMessage(pRoomInfo.getmRoomId());
                    }
                    else {
                        mClientManager.sendCreateRoomMessage(pRoomInfo.getmRoomName(), pRoomInfo.getmMaxPlayerNumber());
                    }
                }
            }
        });
    }

    private Button createRoomButton(RoomInfo pRoomInfo) {
        Button button = new Button(pRoomInfo.toString());
        button.setStyle("-fx-background-radius: 64px; -fx-font: 30 Roboto; -fx-base: #768aa5; -fx-font-size: 30px;");
        setButtonAction(button, pRoomInfo);
        button.setMaxWidth(500);
        button.setPrefWidth(500);
        button.setPrefHeight(200);
        return button;
    }

    private void setGridPaneProperties() {
        int BUTTON_PADDING = 40;
        mGrid = new GridPane();
        mGrid.setPadding(new Insets(BUTTON_PADDING));
        mGrid.setHgap(99);
        mGrid.setVgap(50);
        mGrid.add(createNewGameButton(), column++, row);
    }

    private GridPane createRoomGridPane(ArrayList<RoomInfo> rooms) {
        setGridPaneProperties();
        int BUTTONS_PER_LINE = 2;
        if(rooms != null) {
            for(RoomInfo room : rooms) {
                mGrid.add(createRoomButton(room), column, row);
                row += column;
                column = (column + 1) % BUTTONS_PER_LINE;
            }
        }
        mGrid.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        return mGrid;
    }

    private ScrollPane createRoomScrollPane(ArrayList<RoomInfo> rooms) {
        ScrollPane scrollPane = new ScrollPane(createRoomGridPane(rooms));
        return scrollPane;
    }
}
