package main.java.view.room_scene;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameOverPanel extends StackPane {

    public GameOverPanel(GameScene pGameScene) {
        createPanelBackground();
        createAcceptButton(pGameScene);
//        setStyle("-fx-background-color: #f2f2f2");
    }

    private void createAcceptButton(GameScene pGameScene) {
        mButton = new Button();
        mButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pGameScene.resetView();
                setVisible(false);
            }
        });
        mButton.setText("OK");
        mButton.setStyle("-fx-background-radius: 8px; -fx-font: 30 Roboto; -fx-base: #009933; -fx-font-size: 30px;");
        mButton.setPrefWidth(300.0);
    }

    private void setGameOverPanel(String pPlayerNick) {
        mLabel = new Label("Game over. Player " + pPlayerNick + " lost!");
        mLabel.setFont(Font.font("Roboto", FontWeight.LIGHT, 30));
        mLabel.setTextFill(Color.web("#009933"));
    }

    private void createPanelBackground() {
        mBackground = new Rectangle(400, 150);
        mBackground.setFill(Color.web("#f2f2f2"));
        mBackground.setStroke(Color.web("#768aa5"));
        mBackground.setArcWidth(64.0);
        mBackground.setArcHeight(64.0);
    }

    public void createLabelAndButton(String pPlayersNick) {
        mLabelAndButton = new VBox();
        setGameOverPanel(pPlayersNick);
        mLabelAndButton.getChildren().addAll(mLabel, mButton);
        mLabelAndButton.setAlignment(Pos.CENTER);
        getChildren().clear();
        getChildren().addAll(mBackground, mLabelAndButton);
        setAlignment(Pos.CENTER);
        setVisible(true);
    }

    private Label mLabel;
    private Button mButton;
    private VBox mLabelAndButton;
    private Rectangle mBackground;

}
