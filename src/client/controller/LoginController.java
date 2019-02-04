package client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
// ------------------------------------------------
// CLIENT IMPORTS
import client.handlers.ClientReader;
import client.handlers.ClientSupporter;
import client.handlers.Dialogs;
import client.Main;
import client.handlers.Sound;
import server.entities.packets.AccessPacket;
import server.entities.packets.AccessResultPacket;
import server.entities.packets.ErrorPacket;
import server.entities.packets.Packet;
import server.packets.PacketsEncoder;
import server.packets.PacketsDecoder;
// ------------------------------------------------
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.stage.*;



public class LoginController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private TextField username, password;

    @FXML
    private Pane root;

    private Media oof;
    private ClientSupporter client;
    private Stage stage;

    @FXML
    private void makeOof(KeyEvent event) {
        Sound.playOof();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    private boolean connectForLogin(String username, String password){
        boolean result = false;

        return result;
    }


    private boolean connectForSignin(String username, String password){
        boolean result = false;

        return result;
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
                if (! (dpk.decode(response) instanceof AccessResultPacket) ) {
                    ErrorPacket error = (ErrorPacket) dpk.decode(response);
                    throw new Exception(error.getErrorMessage());
                }else{
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
                Dialogs.showInfoHeadlessDialog("Login made successfully");
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
                if (! (dpk.decode(response) instanceof AccessResultPacket) ) {
                    ErrorPacket error = (ErrorPacket) dpk.decode(response);
                    throw new Exception(error.getErrorMessage());
                }else{
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

        FXMLLoader loader = new FXMLLoader( Main.class.getResource( "views/ChatApplication.fxml" ) );
        Parent root = loader.load();

        //Parent root = FXMLLoader.load(SampleApplication.class.getResource("views/ChatApplication.fxml"));
        Scene scene = new Scene(root);
        ChatController rc = loader.getController();
        rc.setUsername(username);
        rc.setClient(client);

        Sound.playBoot();

        Stage stage = new Stage();
        stage.setTitle("");
        stage.setScene(scene);
        this.stage.hide();
        stage.show();
        new ClientReader(client, rc).start();

    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
