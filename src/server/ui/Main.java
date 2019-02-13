package server.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.MessagingService;

import java.awt.*;
import java.io.IOException;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;

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
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("../../client/assets/icon.png")));
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





