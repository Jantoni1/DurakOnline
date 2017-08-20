package main.java.view.lobby_scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import main.java.model.server.RoomInfo;
import main.java.network.client.MessageBox;
import main.java.network.message.client.Add;
import main.java.network.message.server.RoomUpdate;

import java.util.ArrayList;

/**
 * Created by Kuba on 31.05.2017.
 */
public class LobbyScene {
    private MessageBox mMessageBox;
    private Scene mLobbyScene;
    private NewRoomPanel mNewRoomPanel;
    private ArrayList<RoomInfo> mRooms;
//    private ArrayList<Button> mButtons;
    private GridPane mGrid;
    private int column;
    private int row;
    private StackPane mNewRoomProperty;
    private static int BUTTONS_PER_LINE = 2;


    public LobbyScene(MessageBox pMessageBox) {
//        mButtons = new ArrayList<>();
        mMessageBox = pMessageBox;
        createNewRoomProperty(pMessageBox);
        column = 0;
        row = 0;
        createLobbyScene();
    }

    public Scene getLobbyScene() {
        return mLobbyScene;
    }

    public void createRoomList(ArrayList<RoomInfo> rooms) {
        updateRoomButtons(rooms);
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

    private void createNewRoomProperty(MessageBox pMessageBox) {
        Button button = createNewGameButton();
        mNewRoomPanel = new NewRoomPanel(pMessageBox, button);
        mNewRoomProperty = new StackPane(mNewRoomPanel, button);
    }

    private void addRoom(RoomUpdate pRoomUpdate) {
        mRooms.add(pRoomUpdate.getmRoomInfo());
        updateRoomButtons(mRooms);
    }

    private void removeRoom(RoomUpdate pRoomUpdate) {
        if(isItUpdatedRoom(pRoomUpdate.getmRoomInfo().getmRoomId())) {
            updateRoomButtons(mRooms);
        }
    }

    private boolean isItUpdatedRoom(int pRoomId) {
        return mRooms.removeIf(roomInfo -> roomInfo.getmRoomId() == pRoomId);
    }

    private void modifyRoom(RoomUpdate pRoomUpdate) {
        mRooms.forEach(room -> {
            if(isThisTheRoomToUpdate(room, pRoomUpdate)) {
                mRooms.set(mRooms.indexOf(room), pRoomUpdate.getmRoomInfo());
                updateRoomButtons(mRooms);
            }
        });
    }


    private boolean isThisTheRoomToUpdate(RoomInfo pCurrentRoom, RoomUpdate pRoomToUpdate) {
        return pCurrentRoom.getmRoomId() == pRoomToUpdate.getmRoomInfo().getmRoomId();
    }

    private void createLobbyScene() {
        column = 0;
        row = 0;
        mLobbyScene = new Scene(createSceneRoot(), 1200, 800, Color.AZURE);
        mLobbyScene.getStylesheets().add("main/resources/LobbyScene.css");
//        mLobbyScene.getStylesheets().add("main/resources/stylesheet.css");
    }

    private StackPane createSceneRoot() {
        final StackPane root = new StackPane();
        root.getChildren().addAll(createBackgroundImage(),createRoomScrollPane());
        root.setStyle("-fx-background-color: rgba(0, 255, 0, 0.3);");
        return root;
    }

    private ImageView createBackgroundImage() {
        ImageView backgroundImage = new ImageView("main/resources/background.jpg");
        backgroundImage.setVisible(true);
        return backgroundImage;
    }
//
//    private TextField createRoomNameTextField() {
//        textField = new TextField ();
//        textField.setMaxHeight(200);
//        textField.setFont(Font.font("Roboto", FontWeight.BOLD, 36));
//        textField.setMaxWidth(400);
//        return textField;
//    }

    private Button createNewGameButton() {
        Button button = createRoomButton(new RoomInfo("New Room", -1, 0, 4));
        button.setId("newgame");
        button.setText("CREATE NEW ROOM");
        return button;
    }

    private void setButtonAction(Button pButton, RoomInfo pRoomInfo) {
        pButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                    if(pRoomInfo.getmRoomId() != -1) {
                        mMessageBox.sendMessage(new Add(pRoomInfo.getmRoomId()));
                    }
                    else {
                        showCreateRoomPanel();
                        pButton.setVisible(false);
                        pButton.setPickOnBounds(false);
                    }
            }
        });
    }

    private Button createRoomButton(RoomInfo pRoomInfo) {
        Button button = new Button(pRoomInfo.toString());
        button.setStyle(" -fx-font: 30 Roboto; -fx-base: #768aa5; -fx-font-size: 30px;");
        setButtonAction(button, pRoomInfo);
        button.setMaxWidth(500);
        button.setPrefWidth(500);
        button.setPrefHeight(200);
        button.setFocusTraversable(false);
        return button;
    }

    private void setGridPaneProperties() {
        int BUTTON_PADDING = 40;
        mGrid = new GridPane();
        mGrid.setPadding(new Insets(BUTTON_PADDING));
        mGrid.setHgap(99);
        mGrid.setVgap(50);
        addCreateGameButtons();
    }

    private void addCreateGameButtons() {
        mGrid.add(mNewRoomProperty, 0, 0);
        column = 1;
    }

    private void updateRoomButtons(ArrayList<RoomInfo> rooms) {
        if(rooms != null) {
            if(mGrid.getChildren().size() > 1) {
                mGrid.getChildren().remove(1, mGrid.getChildren().size());
            }
            row = 0;
            column = 1;
            for(RoomInfo room : rooms) {
                mGrid.add(createRoomButton(room), column, row);
                row += column;
                column = (column + 1) % BUTTONS_PER_LINE;
            }
        }
    }


    private GridPane createRoomGridPane() {
        setGridPaneProperties();
        mGrid.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        return mGrid;
    }

    private void showCreateRoomPanel() {
        mNewRoomPanel.setVisible(true);
    }

    private ScrollPane createRoomScrollPane() {
        ScrollPane scrollPane = new ScrollPane(createRoomGridPane());
        return scrollPane;
    }
}
