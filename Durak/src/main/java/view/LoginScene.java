package main.java.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.java.controller.client.ClientManager;

/**
 * Created by Kuba on 31.05.2017.
 */
public class    LoginScene {

    private Scene mLoginScene;
    private ClientManager mClientManager;

    public  LoginScene(ClientManager pClientManager) {
        createLoginScene();
        mClientManager = pClientManager;
    }


    public Scene getLoginScene() {
        return mLoginScene;
    }


    private void createLoginScene() {
        final StackPane root = new StackPane();
        ImageView backgroundImage = new ImageView("main/resources/background.jpg");
        root.getChildren().addAll(backgroundImage, createLoginFormBackground(),createLoginBox());
        mLoginScene = new Scene(root, 1200, 800, Color.AZURE);
        mLoginScene.getStylesheets().add("main/resources/LoginScene.css");
    }

    private Label createLoginFormBackground() {
        Label label = new Label("");
        label.setId("bgroundlabel");
        label.setPrefSize(600, 300);
        return label;
    }


    private Label createLoginLabel() {
        Label label = new Label("CHOOSE YOUR NAME");
        label.setFont(Font.font("Roboto", FontWeight.LIGHT, 24));
        label.setTextFill(Color.web("#ffffff"));
        return label;
    }


    
    private TextField createLoginTextField() {
        TextField textField = new TextField ();
        textField.setMaxWidth(400);
        textField.setMaxHeight(200);
        textField.setFont(Font.font("Roboto", FontWeight.BOLD, 36));
//        textField.setStyle("-fx-text-box-border: #768aa5");
        //        textField.setColor.web("#768aa5")
        return textField;
    }



    private Button createLoginButton(TextField pTextField) {
        Button mButton = new Button();
        mButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(!pTextField.getText().isEmpty()) {
                    mClientManager.getPlayerData().setmUserName(pTextField.getText());
                    mClientManager.sendHandshakeMessage(pTextField.getText());
                }
            }
        });
        mButton.setEffect(new DropShadow());
        mButton.setText("Login");
        mButton.setMaxWidth(400);
        mButton.setStyle("-fx-font: 30 Roboto; -fx-text-fill: white; -fx-base: #768aa5;");
        mButton.setDefaultButton(true);
        return mButton;
    }

    private Button createExitButton() {
        Button mButton = new Button();
        mButton.setOnAction(event -> Platform.exit());
        mButton.setText("Exit");
        mButton.setStyle("-fx-font: 18 Roboto; -fx-base: #768aa5;");
        mButton.setMaxHeight(60);
        mButton.setMaxWidth(100);
        return mButton;
    }

    private VBox createLoginBox() {
        VBox vBox = new VBox();
        TextField textField = createLoginTextField();
        vBox.getChildren().addAll(createLoginLabel() , textField, createLoginButton(textField));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
}
