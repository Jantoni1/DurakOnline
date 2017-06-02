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

/**
 * Created by Kuba on 31.05.2017.
 */
public class LoginScene {

    private Scene mLoginScene;
    private Boolean setLogin;
    private String mUsername;

    public  LoginScene() {
        createLoginScene();
    }


    public Scene getLoginScene() {
        return mLoginScene;
    }


    private void createLoginScene() {
        final StackPane root = new StackPane();
        ImageView backgroundImage = new ImageView("main/resources/background.jpg");
        root.getChildren().addAll(backgroundImage, createLoginFormBackground(), createLoginBox());
        mLoginScene = new Scene(root, 1200, 800, Color.AZURE);
        setLogin = true;
    }

    private Rectangle createLoginFormBackground() {
        Rectangle rectangle = new Rectangle(600, 300);
        rectangle.setFill(Color.GREY);
        rectangle.setStroke(Color.DEEPSKYBLUE);
        return rectangle;
    }


    private Label createLoginLabel() {
        Label label = new Label("CHOOSE YOUR NAME");
        label.setFont(Font.font("Roboto", FontWeight.LIGHT, 24));
        label.setTextFill(Color.web("#12a6ee"));
        return label;
    }


    
    private TextField createLoginTextField() {
        TextField textField = new TextField ();
        textField.setMaxWidth(400);
        textField.setMaxHeight(200);
        textField.setFont(Font.font("Roboto", FontWeight.BOLD, 36));
        return textField;
    }

    public synchronized void sendLogin() {
        setLogin = true;
    }

    public void sendNick(String nick) {
        synchronized(setLogin) {
            mUsername = nick;
            setLogin.notify();
        }
    }

    public String getmUsername() throws InterruptedException{
        synchronized (setLogin) {
            while(mUsername == null) {
               setLogin.wait();
            }
            return mUsername;
        }
    }

    private Button createLoginButton(TextField pTextField, DropShadow pShadow) {
        Button mButton = new Button();
        mButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        mButton.setEffect(pShadow);
                    }
                });
        mButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                sendNick(pTextField.getText());
            }
        });

        mButton.setText("Login");
        mButton.setMaxWidth(400);
        mButton.setStyle("-fx-font: 30 Roboto; -fx-base: #12a6ee;");
        mButton.setDefaultButton(true);
        return mButton;
    }

    private Button createExitButton() {
        Button mButton = new Button();
        mButton.setOnAction(event -> Platform.exit());
        mButton.setText("Wyjscie");
        mButton.setStyle("-fx-font: 18 Roboto; -fx-base: #12a6ee;");
        mButton.setMaxHeight(60);
        mButton.setMaxWidth(100);
        return mButton;
    }

    private VBox createLoginBox() {
        VBox vBox = new VBox();
        DropShadow shadow = new DropShadow();
        TextField textField = createLoginTextField();
        vBox.getChildren().addAll(createLoginLabel() , textField, createLoginButton(textField, shadow));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
}
