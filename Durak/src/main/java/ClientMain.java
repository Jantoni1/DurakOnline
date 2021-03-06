package main.java;


        import javafx.application.Application;
        import javafx.application.Platform;
        import javafx.stage.Stage;
        import main.java.controller.client.ClientManager;
        import main.java.network.client.Client;

        import java.io.IOException;
        import java.util.Scanner;

public class ClientMain extends Application {
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
//        Scanner scanner = new Scanner(System.in);
//        String ip = scanner.next();
        try {
            Client client = new Client("127.0.0.1", 3000);
            ClientManager mClientManager = new ClientManager(primaryStage,client );
        }
        catch(IOException e) {
            System.out.println("Could not connect to server");
            Platform.exit();
        }
    }
}
