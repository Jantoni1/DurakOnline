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
        createLabelAndButton();
        setVisible(false);
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
        mButton.setStyle("-fx-background-radius: 3%; -fx-font: 30 Roboto; -fx-base: #009933; -fx-font-size: 30px;");
        mButton.setPrefWidth(400.0);
    }

    private void setGameOverPanel(String pPlayerNick) {
        mLabel = new Label("Game over. Player " + pPlayerNick + " lost!");
        mLabel.setFont(Font.font("Roboto", FontWeight.LIGHT, 30));
        mLabel.setStyle(" -fx-text-fill: #ffffff;");
        setVisible(true);
    }

    private void createPanelBackground() {
        mBackground = new Rectangle(580, 150);
        mBackground.setFill(Color.TRANSPARENT);//web("#f2f2f2")
//        mBackground.setStroke(Color.web("#768aa5"));
//        mBackground.setStyle("-fx-background-color: rgba(128, 159, 255, 0.3); -fx-border-radius: 10%");
    }

    public void showEndPanel(String pPlayersNick) {
        setGameOverPanel(pPlayersNick);
        setVisible(true);
    }

    public void createLabelAndButton() {
        mLabelAndButton = new VBox();
        setGameOverPanel("");
        mLabelAndButton.getChildren().addAll(mLabel, mButton);
        mLabelAndButton.setAlignment(Pos.CENTER);
        getChildren().clear();
        getChildren().addAll(mBackground, mLabelAndButton);
        setAlignment(Pos.CENTER);
    }

    private Label mLabel;
    private Button mButton;
    private VBox mLabelAndButton;
    private Rectangle mBackground;

}
