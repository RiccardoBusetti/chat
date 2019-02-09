package client.controller;

import client.handlers.Chat;
import client.handlers.ClientSupporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketsEncoder;

public class PrivateChatController{

    private ClientSupporter client;
    private String sender, receiver;

    public Button sendButton;
    public ListView messageList;
    public TextField inputMessage;

    private Chat chatData;

    private ObservableList<String> observableListMessages;

    public PrivateChatController(){

    }

    public void setChatData(Chat chat){
        this.chatData = chat;
    }

    public void setUpUI(){
        observableListMessages = FXCollections.observableArrayList(chatData.getMessages());
        messageList.setItems(observableListMessages);
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

    @FXML
    public void sendUnicastMessage(ActionEvent event) {
        if (inputMessage.getText().replace("\n", "").length() == 0)
        {
            inputMessage.clear();
            return;
        }
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        client.sendLine(packetsEncoder.encode(new UnicastMessagePacket(sender, receiver, inputMessage.getText())));
        chatData.addMessage(sender, inputMessage.getText());
        inputMessage.clear();
    }

    public void checkIfEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)  {
            sendUnicastMessage(null);
        }
    }
}
