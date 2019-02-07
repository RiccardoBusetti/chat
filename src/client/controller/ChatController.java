package client.controller;

import client.cellviews.MessageListCellView;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import server.entities.packets.MulticastMessagePacket;
import server.packets.PacketsEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController {

    private String username;
    private ClientSupporter client;
    private ObservableList<Pair<String, String>> multicastMessageList;

    @FXML
    private TextArea messageText;
    @FXML
    private Button sendButton;
    @FXML
    private ListView multicastList;

    public ChatController() {
        multicastMessageList = FXCollections.observableArrayList(MessageList.getInstance().getAllMessages());
    }

    @FXML
    public void initialize() {
        setUpUI();
    }

    @FXML
    public void openFormNewPrivateMessage(ActionEvent event) {
    }

    @FXML
    public void exitApplication(ActionEvent event) throws IOException {
        client.closeSocket();
        Platform.exit();
    }

    @FXML
    public void showAppInfo(ActionEvent event) {
        Dialogs.showInfoHeadlessDialog("SampleApplication");
    }

    private void setUpUI() {
        multicastList.setItems(multicastMessageList);
        multicastList.setCellFactory(param -> new MessageListCellView(username));
        addListeners();
    }

    private void addListeners() {
        messageText.setOnKeyPressed(this::checkEnterKey);
    }

    private void checkEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)  {
            sendMulticastMessage();
        }
    }

    private void sendMulticastMessage() {
        String message = messageText.getText().replace("\n"," \b");
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        MulticastMessagePacket multicastMessagePacket = new MulticastMessagePacket(this.username, message);

        client.sendLine(packetsEncoder.encode(multicastMessagePacket));
        showMessage(username, null, message);

        messageText.setText("");
    }

    public void showMessage(String sender, String receiver, String message) {
        if (receiver == null) {
            // Multicast message.
            multicastMessageList.add(new Pair<>(sender, message));
        } else {
            // Unicast message.
            // TODO: handle unicast message.
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }
}