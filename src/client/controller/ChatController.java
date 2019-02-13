package client.controller;

import client.Main;
import client.cellviews.MessageListCellView;
import client.cellviews.UserListCellView;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import server.entities.packets.MulticastMessagePacket;
import server.packets.PacketsEncoder;

import java.io.IOException;
import java.util.List;

public class ChatController {

    private String username;
    private ClientSupporter client;

    private ObservableList<Pair<String, String>> multicastMessageList;
    private ObservableList<String> onlineUsers;
    private ObservableList<Pair<String, Chat>> privateChatUsersList;
    private ObservableList<String> pmChatSaved;

    @FXML
    private TextField messageText;
    @FXML
    private Button sendButton;
    @FXML
    private ListView multicastList;
    @FXML
    private ListView privateMessageList;

    public ChatController() {
        multicastMessageList = FXCollections.observableArrayList(MessageList.getInstance().getAllMessages());
        privateChatUsersList = FXCollections.observableArrayList(ChatListV2.getInstance().getAllChats());
        pmChatSaved = FXCollections.observableArrayList();
        onlineUsers = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setUpUI();
    }

    public void stop(){
        System.out.println("Stage is closing");
    }

    @FXML
    public void openFormNewPrivateMessage(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/NewPrivateChatApplication.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        NewPrivateChatController controller = loader.getController();

        controller.setUpList(onlineUsers, pmChatSaved, username);
        controller.setClient(client);
        controller.setUpUI();
        controller.setTempList(privateChatUsersList);
        controller.setPmList(pmChatSaved);

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
        privateMessageList.setItems(pmChatSaved);
        privateMessageList.setCellFactory(param -> new UserListCellView(client, username));
        addListeners();
    }

    private void updateOnlineUserUI(List<String> in){
        pmChatSaved.clear();
        for (String s : in) pmChatSaved.add(s);
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
    private void openChat(MouseEvent mouseEvent) throws IOException{
        try {
            String receiver = privateMessageList.getSelectionModel().getSelectedItem().toString();

            int location = 0;
            boolean found = false;
            for (int i = 0; i < pmChatSaved.size(); i++){
                if (found)
                    continue;
                if (pmChatSaved.get(i).equals(receiver)){
                    found = true;
                    location = i;
                }
            }

            Chat data = privateChatUsersList.get(location).getValue();

            //Open view
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/PrivateChatApplication.fxml"));
            Parent root = loader.load();

            //Prepare scene
            Scene scene = new Scene(root);
            PrivateChatController controller = loader.getController();

            //Controller settings
            controller.setChatData(data);
            controller.setSender(username);
            controller.setReceiver(receiver);
            controller.setClient(client);
            controller.setUpUI();

            data.setController(controller);

            //Open stage
            Stage stage = new Stage();
            stage.setTitle(receiver); //Title is user to chat atm
            stage.setScene(scene);
            stage.show();
        }catch (NullPointerException nuex){
            System.out.println("Null pointer exception found");
        }
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

                for (int i = 0; i < pmChatSaved.size(); i++)
                    if (sender.equals(pmChatSaved.get(i))){
                        //
                        //chatList.getChat(i).addMessage(sender, message);
                        privateChatUsersList.get(i).getValue().addMessage(sender, message);
                        privateChatUsersList.get(i).getValue().updateUI();
                        found = true;
                    }

                if (!found){
                    System.out.println("New Chat!");
                    Chat chat = new Chat();
                    ChatListV2.getInstance().addChat(sender, chat);
                    privateChatUsersList.add(new Pair<>(sender, chat));
                    pmChatSaved.add(sender);
                    chat.addMessage(sender, message);
                }
                else {
                    System.out.println("New message!");
                }

                //updateOnlineUserUI(Compare.compare(temp, -1));
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