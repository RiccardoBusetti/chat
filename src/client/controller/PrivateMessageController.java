package client.controller;

import client.handlers.ClientSupporter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketsEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivateMessageController extends Application implements Initializable {

    private ClientSupporter client;
    private String sender, receiver;

    private TextArea messageArea;
    private ListView messages;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }

    public void updateMessage(String sender, String message){
        //messages.ins
    }

    @FXML
    public void sendUnicastMessage(ActionEvent event) throws IOException {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        client.sendLine(packetsEncoder.encode(new UnicastMessagePacket(sender, receiver, messageArea.getText())));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
    }
}
