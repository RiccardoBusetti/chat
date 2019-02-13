package client;

import client.controller.LoginController;
import client.handlers.ClientSupporter;
import client.handlers.Dialogs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.constants.Constants;

public class Main3 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(Main3.class.getResource("views/LoginApplicationV2.fxml"));
        Parent root = loader.load();
        ClientSupporter client = null;
        try {
            client = new ClientSupporter(Constants.SERVER_HOST, Constants.SERVER_PORT);

            Scene scene = new Scene(root);
            LoginController rc = loader.getController();
            rc.setClient(client);

            Stage stage = new Stage();
            stage.setTitle("");
            stage.setScene(scene);
            rc.setStage(stage);
            stage.show();
        } catch (Exception e) {
            Dialogs.showErrorDialog("Connection error", "Cannot connect to the server");
            System.exit(1);
        }



    }
}
