package client.controller;

import client.cellviews.MessageListCellView;
import client.handlers.Chat;
import client.handlers.ClientSupporter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketsEncoder;

public class PrivateChatController {

    public ImageView sendButton;
    public ListView messageList;
    public TextField inputMessage;
    private ClientSupporter client;
    private String sender, receiver;
    private Chat chatData;

    private ObservableList<Pair<String, String>> observableListMessages;

    public PrivateChatController() {

    }

    public void setChatData(Chat chat) {
        this.chatData = chat;
    }

    public void setUpUI() {
        observableListMessages = FXCollections.observableArrayList(chatData.getMessages());
        messageList.setStyle("-fx-control-inner-background-alt: -fx-control-inner-background");
        messageList.setItems(observableListMessages);
        messageList.setCellFactory(param -> new MessageListCellView(sender));
        messageList.getItems().addListener((ListChangeListener) c -> {
            messageList.scrollTo(messageList.getItems().size() - 1);
        });
    }

    public void updateUI() {
        try {
            observableListMessages.add(chatData.getMessage(chatData.getAmountMessage() - 1));
        } catch (Exception e) {
            //Nothing happens. Workaround to don't trigger so much exception threads
        }
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
        if (inputMessage.getText().replace("\n", "").length() == 0) {
            inputMessage.clear();
            return;
        }
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        client.sendLine(packetsEncoder.encode(new UnicastMessagePacket(sender, receiver, inputMessage.getText())));
        chatData.addMessage(sender, inputMessage.getText());
        observableListMessages.add(new Pair<>(sender, inputMessage.getText()));
        inputMessage.clear();
    }

    public void checkIfEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendUnicastMessage(null);
        }
    }
}
