package client.controller;

import client.Main;
import client.handlers.ClientReader;
import client.handlers.ClientSupporter;
import client.handlers.Dialogs;
import client.handlers.Sound;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.entities.packets.AccessPacket;
import server.entities.packets.AccessResultPacket;
import server.entities.packets.ErrorPacket;
import server.entities.packets.Packet;
import server.packets.PacketsDecoder;
import server.packets.PacketsEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// ------------------------------------------------
// CLIENT IMPORTS
// ------------------------------------------------


public class LoginController implements Initializable {

    @FXML
    private TextField username, password;

    @FXML
    private Pane root;

    private ClientSupporter client;
    private Stage stage;

    @FXML
    private void makeOof(KeyEvent event) {
        Sound.playOof();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void makeLogin(ActionEvent event) {

        String user = username.getText();
        String pass = password.getText();

        if (user.equals("") || pass.equals("")) {
            String message = "";

            if (user.equals(""))
                message += "Username can't be blank\n";

            if (pass.equals(""))
                message += "Password can't be blank\n";

            Dialogs.showErrorDialog("Can't do the login", message);
        } else {

            PacketsEncoder pk = new PacketsEncoder();
            PacketsDecoder dpk = new PacketsDecoder();
            AccessPacket apk = new AccessPacket(true, user, pass);
            AccessResultPacket accessResultPacket1;
            try {
                String response = client.makeRequest(pk.encode(apk));
                if (!(dpk.decode(response) instanceof AccessResultPacket)) {
                    ErrorPacket error = (ErrorPacket) dpk.decode(response);
                    throw new Exception(error.getErrorMessage());
                } else {
                    accessResultPacket1 = (AccessResultPacket) dpk.decode(response);
                }
            } catch (IOException e) {
                Dialogs.showErrorDialog("Connection error", e.getMessage());
                return;
            } catch (Exception e) {
                Dialogs.showErrorDialog("Login error", e.getMessage());
                e.printStackTrace();
                return;
            }

            if (!accessResultPacket1.isAllowed()) {
                Dialogs.showErrorDialog("Can't do the login", "Wrong credentials");
            } else {
                try {
                    gotoChat(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void makeSignIn(ActionEvent event) {

        String user = username.getText();
        String pass = password.getText();

        if (user.equals("") || pass.equals("")) {
            String message = "";

            if (user.equals(""))
                message += "Username can't be blank\n";

            if (pass.equals(""))
                message += "Password can't be blank\n";

            Dialogs.showErrorDialog("Can't do the login", message);
        } else {
            //System.out.println("Username: " + user + "\nPassword: " + pass);

            // -----------------------------------------------------------------------------------------
            PacketsEncoder pk = new PacketsEncoder();
            PacketsDecoder dpk = new PacketsDecoder();
            AccessPacket apk = new AccessPacket(false, user, pass);
            AccessResultPacket accessResultPacket1;
            try {
                String response = client.makeRequest(pk.encode(apk));
                Packet accessResultPacket = dpk.decode(response);
                if (!(dpk.decode(response) instanceof AccessResultPacket)) {
                    ErrorPacket error = (ErrorPacket) dpk.decode(response);
                    throw new Exception(error.getErrorMessage());
                } else {
                    accessResultPacket1 = (AccessResultPacket) dpk.decode(response);
                }
            } catch (IOException e) {
                Dialogs.showErrorDialog("Connection error", e.getMessage());
                return;
            } catch (Exception e) {
                Dialogs.showErrorDialog("Login error", e.getMessage());
                return;
            }
            // ------------------------------------------------------------------------------------------

            if (!accessResultPacket1.isAllowed()) {
                Dialogs.showErrorDialog("Can't create account", "Account exists");
            } else {
                Dialogs.showInfoHeadlessDialog("Account created successfully");
                try {
                    gotoChat(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void gotoChat(String username) throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/ChatApplicationV2.fxml"));
        Parent root = loader.load();

        //Parent root = FXMLLoader.load(SampleApplication.class.getResource("views/ChatApplication.fxml"));
        Scene scene = new Scene(root);
        ChatController rc = loader.getController();
        rc.setUsername(username);
        rc.setClient(client);

        Sound.playBoot();

        Stage stage = new Stage();
        stage.setTitle("LilChat");
        stage.setScene(scene);
        stage.getIcons().add(new Image(LoginController.class.getResourceAsStream("../../client/assets/icon.png")));
        this.stage.hide();
        stage.show();
        ClientReader clientReader = new ClientReader(client, rc);
        clientReader.start();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                clientReader.close();
            }
        });


    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void checkEnterButton(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            makeLogin(null);
    }
}
