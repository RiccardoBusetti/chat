package client;

import client.controller.LoginController;
import client.handlers.ClientSupporter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/LoginApplicationV2.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        LoginController rc = loader.getController();
        rc.setClient(new ClientSupporter());

        Stage stage = new Stage();
        stage.setTitle("LilChat");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("../client/assets/icon.png")));
        stage.setScene(scene);
        rc.setStage(stage);
        stage.show();
    }
}
