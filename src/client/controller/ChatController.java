package client.controller;

import client.Main;
import client.cellviews.MessageListCellView;
import client.handlers.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.List;

public class ChatController {

    private String username;
    private ClientSupporter client;
    private ChatList privateChatList;

    private ObservableList<Pair<String, String>> multicastMessageList;
    private ObservableList<String> onlineUsers;
    private ObservableList<String> privateChatUsersList;

    @FXML
    private TextArea messageText;
    @FXML
    private Button sendButton;
    @FXML
    private ListView multicastList;
    @FXML
    private ListView privateMessageList;

    public ChatController() {
        multicastMessageList = FXCollections.observableArrayList(MessageList.getInstance().getAllMessages());
        privateChatUsersList = FXCollections.observableArrayList(ChatList.getInstance().getUsers());
        onlineUsers = FXCollections.observableArrayList();
        privateChatList = ChatList.getInstance();
    }

    @FXML
    public void initialize() {
        setUpUI();
    }

    public void stop(){
        System.out.println("Stage is closing");
    }

    @FXML
    public void openFormNewPrivateMessage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/NewPrivateChatApplication.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        NewPrivateChatController controller = loader.getController();

        controller.setUpList(onlineUsers, privateChatList.getUsers(), username);
        controller.setClient(client);
        controller.setUpUI();
        controller.setTempList(privateChatList);

        Stage stage = new Stage();
        stage.setTitle("New Chat...");
        stage.setScene(scene);

        controller.setStage(stage);

        stage.show();
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
        privateMessageList.setItems(privateChatUsersList);
        addListeners();
    }

    private void addListeners() {
        messageText.setOnKeyPressed(this::checkEnterKey);
    }

    @FXML
    private void checkEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            sendMulticastMessage();
    }

    @FXML
    private void sendMulticastMessage() {

        if (messageText.getText().replace("\n", "").length() == 0)
        {
            messageText.clear();
            return;
        }

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
            if (receiver.equals(username)) {
                boolean found = false;

                for (int i = 0; i < privateChatList.getSize(); i++)
                    if (sender.equals(privateChatList.getUsers().get(i))){
                        privateChatList.getChat(i).addMessage(sender, message);
                        found = true;
                    }

                if (!found){
                    System.out.println("New Chat!");
                    Chat chat = new Chat();
                    privateChatList.addChat(chat, sender);
                    chat.addMessage(sender, message);
                }
                else {
                    System.out.println("New message!");
                }
            }else {
                System.out.println("Unicast ignored due to not matching username");
            }

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

    public void changeOnlineUserList(List<String> users) {
        onlineUsers.clear();
        onlineUsers.addAll(users);
    }
}