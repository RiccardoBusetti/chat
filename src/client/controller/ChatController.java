package client.controller;

import client.handlers.ClientSupporter;
import client.handlers.Dialogs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import server.entities.packets.MulticastMessagePacket;
import server.packets.PacketsEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChatController extends Application implements Initializable {

    private String username;
    private ClientSupporter client;

    @FXML
    private TextArea messageText;
    @FXML
    private Button sendButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @Override
    public void stop(){
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
    public void openFormNewPrivateMessage(ActionEvent event){

    }

    public void putMessage(String sender, String receiver, String message){
        if (receiver == null){
            //MULTICAST
            System.out.println("Multicast Message: " + message);
        }else {
            //UNICAST
            System.out.println("Unicast Message: " + message);
        }
    }

    @FXML
    public void sendMulticastMessage(ActionEvent event) throws IOException {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        System.out.println(messageText.getText());
        client.sendLine(packetsEncoder.encode(new MulticastMessagePacket(this.username, messageText.getText())));
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void showAppInfo(ActionEvent event){
        Dialogs.showInfoHeadlessDialog("SampleApplication");
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }
}