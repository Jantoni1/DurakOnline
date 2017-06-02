package main.java.view;


        import com.sun.prism.paint.Color;
        import javafx.application.Application;
        import javafx.application.Platform;
        import javafx.geometry.Insets;
        import javafx.geometry.Pos;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.ScrollPane;
        import javafx.scene.layout.GridPane;
        import javafx.scene.layout.StackPane;
        import javafx.scene.layout.VBox;
        import javafx.scene.paint.Paint;
        import javafx.scene.shape.Circle;
        import javafx.stage.Stage;
        import main.java.network.client.Client;

        import java.io.IOException;
        import java.net.ConnectException;

public class Lobby extends Application {
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
        try {
            ClientManager mClientManager = new ClientManager(primaryStage, "127.0.0.1", 3000);

        }
        catch(IOException e) {
            System.out.println("Could not connect to server");
            Platform.exit();
        }
    }
}
