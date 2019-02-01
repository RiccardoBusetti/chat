package client;

import client.constants.Constants;
import client.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader( Main.class.getResource( "views/LoginApplication.fxml" ) );
        Parent root = loader.load();
        ClientSupporter client = new ClientSupporter(Constants.SERVER_HOST, Constants.SERVER_PORT);


        //Parent root = FXMLLoader.load(SampleApplication.class.getResource("views/ChatApplication2.fxml"));
        Scene scene = new Scene(root);
        LoginController rc = loader.getController();
        rc.setClient(client);

        Sound.playBoot();

        Stage stage = new Stage();
        stage.setTitle("");
        stage.setScene(scene);
        rc.setStage(stage);
        stage.show();

    }
}
