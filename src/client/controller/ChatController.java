package client.controller;

import client.handlers.ClientSupporter;
import client.handlers.Dialogs;
import client.handlers.MessageList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import server.entities.User;
import server.entities.packets.MulticastMessagePacket;
import server.packets.PacketsEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class ChatController extends Application implements Initializable {

    private String username;
    private ClientSupporter client;
    private ObservableList<Pair<String, String>> multicastMessageList;

    @FXML
    private TextArea messageText;
    @FXML
    private Button sendButton;

    @FXML
    private ListView multicastList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        multicastMessageList = FXCollections.observableArrayList(MessageList.getInstance().getAllMessages());
        multicastList.setItems(multicastMessageList);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }



    @Override
    public void stop() {
        System.out.println("Stage is closing");
        try {
            client.closeSocket();
        } catch (IOException e) {
            System.out.println("Error on closing socket");
            e.printStackTrace();
        }
        // Save file
    }

    @FXML
    public void openFormNewPrivateMessage(ActionEvent event) {

    }

    public void putMessage(String sender, String receiver, String message) {
        if (receiver == null) {
            //MULTICAST
            System.out.println("Multicast Message: " + message);
            multicastMessageList.add(new Pair<>(sender, message.replace("\b", "\n")));
        } else {
            //UNICAST
            System.out.println("Unicast Message: " + message);
        }
    }

    @FXML
    public void sendMulticastMessage(ActionEvent event) throws IOException {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        messageText.setText(messageText.getText().replace("\n", "\b"));
        System.out.println(messageText.getText());
        client.sendLine(packetsEncoder.encode(new MulticastMessagePacket(this.username, messageText.getText())));
        this.putMessage(username, null, messageText.getText());
        this.messageText.clear();
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void showAppInfo(ActionEvent event) {
        Dialogs.showInfoHeadlessDialog("SampleApplication");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }

    public void checkEnterKey(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().toString().equals("ENTER")){
            this.sendMulticastMessage(null);
            this.messageText.clear();
        }

    }
}