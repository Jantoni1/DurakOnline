package main.java.view.lobby_scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.network.client.MessageBox;
import main.java.network.message.client.CreateRoom;

/**
 * Created by Kuba on 20.08.2017.
 */
public class NewRoomPanel extends StackPane {

    private Button mAcceptButton;
    private PlayersNumberProperty mPlayersNumberProperty;
    private VBox mTopBar;
    private BorderPane mBottomBar;
//    private VBox mAllElementsContainer;
    private Label mBackground;
    private TextField mRoomName;
    private ImageView mCancel;
    private BorderPane mBorderPane;


    public NewRoomPanel(MessageBox pMessageBox, Button pButton) {
        createElements(pMessageBox, pButton);
        createBackground();
        getChildren().addAll(mBackground, mBorderPane);
        setVisible(false);
    }

    private void createBackground() {
        mBackground = new Label("");
        mBackground.setPrefSize(500, 200);
        mBackground.setId("newgame");
    }

    private void createElements(MessageBox pMessageBox, Button pButton) {
        createTopBar(pButton);
        createBottomBar(pMessageBox, pButton);
//        mAllElementsContainer = new VBox(mTopBar, mBottomBar);
        createBorderPane();
    }

    private void createBorderPane() {
        mBorderPane = new BorderPane();
        mBorderPane.setPrefSize(500, 200);
        mBorderPane.setTop(mCancel);
        mBorderPane.setCenter(mTopBar);
        mBorderPane.setBottom(mBottomBar);
        BorderPane.setMargin(mCancel, new Insets(5.0, 5.0, 0.0, 0.0));
        BorderPane.setAlignment(mCancel, Pos.TOP_RIGHT);
    }

    private void createBottomBar(MessageBox pMessageBox, Button pButton) {
        mPlayersNumberProperty = new PlayersNumberProperty();
        createAcceptButton(pMessageBox, pButton);
        mBottomBar = new BorderPane();
        mBottomBar.setPrefWidth(500.0);
        mBottomBar.setRight(mAcceptButton);
        mBottomBar.setLeft(mPlayersNumberProperty);
        BorderPane.setMargin(mAcceptButton, new Insets(0.0, 50.0, 0.0, 0.0));
        BorderPane.setMargin(mPlayersNumberProperty, new Insets(0.0, 0.0, 0.0, 40.0));
    }
    
    private void createAcceptButton(MessageBox pMessageBox, Button pButton) {
        mAcceptButton = new Button("CREATE");
        mAcceptButton.setStyle("-fx-font: 16 Roboto;");
//        mAcceptButton.setPrefSize(10.0, 1.0);
        mAcceptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!mRoomName.getText().isEmpty()) {
                    pMessageBox.sendMessage(new CreateRoom(mRoomName.getText(), mPlayersNumberProperty.getNumberOfPlayers()));
                    resetNewRoomProperties(pButton);
                }
            }
        });
    }

    private void createTopBar(Button pButton) {
        createTextField();
        createCancelButton(pButton);
        Text text = new Text("Choose room's name:");
        text.setStyle("-fx-text-fill: white; -fx-font: 28 Roboto;");
        text.setFill(Color.WHITE);
        mTopBar = new VBox(text, mRoomName);
        mTopBar.setAlignment(Pos.CENTER);
//        mTopBar.setSpacing(200.0);
    }

    private void createCancelButton(Button pButton) {
        mCancel = new ImageView("main/resources/cancel-mark.png");
        mCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resetNewRoomProperties(pButton);
            }
        });

    }

    private void resetNewRoomProperties(Button pButton) {
        mRoomName.clear();
        mPlayersNumberProperty.reset();
        setVisible(false);
        pButton.setVisible(true);
        pButton.setPickOnBounds(true);
    }

    private void createTextField() {
        mRoomName = new TextField();
        mRoomName.setPromptText("Room name...");
        mRoomName.setMaxWidth(400.0);
    }
}
