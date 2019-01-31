package server.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.MessagingService;

import java.io.IOException;

public class Main extends Application {

    private ControlPanelController controlPanelController;
    private Stage primaryStage;
    private MessagingService messagingService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        loadLayout();
    }

    private void loadLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ControlPanel.fxml"));
            AnchorPane rootLayout = loader.load();

            controlPanelController = loader.getController();
            controlPanelController.attachMain(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer(int port) {
        messagingService = new MessagingService.Builder()
                .onPort(port)
                .async()
                .build();

        messagingService.start();
    }

    public void stopServer() {
        messagingService.stop();
    }
}





