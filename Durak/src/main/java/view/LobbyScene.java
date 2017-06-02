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
import main.java.model.RoomInfo;
import main.java.network.message.server.RoomUpdate;

import java.util.ArrayList;

/**
 * Created by Kuba on 31.05.2017.
 */
public class LobbyScene {
    private Scene mLobbyScene;
    private Boolean mLock;
    private RoomInfo mChosenRoom;
    private ArrayList<RoomInfo> mRooms;
//    private ArrayList<Button> mButtons;
    private GridPane mGrid;
    private int column;
    private int row;

    public LobbyScene() {
//        mButtons = new ArrayList<>();
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
        if(mRooms.remove(pRoomUpdate.getmRoomInfo())) {
            createLobbyScene(mRooms);
        }
        //TODO CHECK IF WORKS PROPERLY
    }

    private void modifyRoom(RoomUpdate pRoomUpdate) {

        mRooms.forEach(room -> {
            if(isThisTheRoomToUpdate(room, pRoomUpdate)) {
                mRooms.set(mRooms.indexOf(room), pRoomUpdate.getmRoomInfo());
                createLobbyScene(mRooms);
                System.out.println("LOBBY SCENE UPDATE ROOMS ELKO XD");
            }
        });
        //TODO CHECK IF WORKS PROPERLY
    }


    private boolean isThisTheRoomToUpdate(RoomInfo pCurrentRoom, RoomUpdate pRoomToUpdate) {
        return pCurrentRoom.getmRoomId() == pRoomToUpdate.getmRoomInfo().getmRoomId();
    }

    private void createLobbyScene(ArrayList<RoomInfo> pRoomInfo) {
        column = 0;
        row = 0;
        mLobbyScene = new Scene(createSceneRoot(pRoomInfo), 1200, 800, Color.AZURE);
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

    public RoomInfo waitForLobbyAction() throws InterruptedException {
        synchronized(mLock) {
            System.out.println("WAIT FOR LOBBY ACTION ENTER");
            while(mChosenRoom == null) {
                mLock.wait();
            }
            System.out.println("WAIT FOR LOBBY ACTION LEAVE");
            RoomInfo chosenRoom = mChosenRoom;
            mChosenRoom = null;
            return chosenRoom;
        }
    }

    private Button createNewGameButton() {
        Button button = createRoomButton(new RoomInfo("New Room", -1, 0, 4));
        button.setText("CREATE ROOM");
        button.setStyle("-fx-font: 30 Roboto; -fx-base: #d2a679;");
//        mButtons.add(button);
        return button;
    }

    private void setButtonAction(Button pButton, RoomInfo pRoom) {
        pButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                synchronized(mLock) {
                    mChosenRoom = pRoom;
                    mLock.notify();
                }
            }
        });
    }

    private Button createRoomButton(RoomInfo pRoomInfo) {
        Button button = new Button(pRoomInfo.toString());
        setButtonAction(button, pRoomInfo);
        button.setMaxWidth(500);
        button.setPrefWidth(500);
        button.setStyle("-fx-font: 30 Roboto; -fx-base: #12a6ee;");
        button.setPrefHeight(200);
        return button;
    }

    private GridPane createRoomGridPane(ArrayList<RoomInfo> rooms) {
        mGrid = new GridPane();
        int NUM_BUTTON_LINES = (rooms.size() + rooms.size() % 2) / 2 ;
        int BUTTON_PADDING = 40;
        int BUTTONS_PER_LINE = 2;
        mGrid.setPadding(new Insets(BUTTON_PADDING));
        mGrid.setHgap(100);
        mGrid.setVgap(50);
        mGrid.add(createNewGameButton(), column++, row);
        if(rooms != null) {
            for(RoomInfo room : rooms) {
                mGrid.add(createRoomButton(room), column, row);
                row += column;
                column = (column + 1) % 2;
            }
        }
        mGrid.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        return mGrid;
    }

    private ScrollPane createRoomScrollPane(ArrayList<RoomInfo> rooms) {
        ScrollPane scrollPane = new ScrollPane(createRoomGridPane(rooms));
        scrollPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        //scrollPane.setStyle("-fx-background: #00cc99");
        return scrollPane;
    }
}
