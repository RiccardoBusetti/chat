package client.controller;

import client.handlers.ClientSupporter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketsEncoder;

import java.io.IOException;

public class PrivateChatController{

    private ClientSupporter client;
    private String sender, receiver;

    private TextArea messageArea;
    private ListView messages;


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

    public void updateMessage(String sender, String message) {
        //messages.ins
    }

    @FXML
    public void sendUnicastMessage(ActionEvent event) throws IOException {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        client.sendLine(packetsEncoder.encode(new UnicastMessagePacket(sender, receiver, messageArea.getText())));
    }
}
