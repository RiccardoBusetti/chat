package client.controller;

import client.Main;
import client.handlers.Chat;
import client.handlers.ChatList;
import client.handlers.ClientSupporter;
import client.handlers.OnlineUsersList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import server.users.OnlineUsers;

import java.io.IOException;
import java.util.List;

public class NewPrivateChatController {

    @FXML
    public ListView onlineNewUsers;

    private ObservableList<String> onlineUsers;

    private ChatList tempList;

    private String self;
    private Stage stage;
    private ClientSupporter client;

    public NewPrivateChatController() {
        onlineUsers = FXCollections.observableArrayList(OnlineUsersList.getInstance().getAllUsers());
        //assert onlineNewUsers != null;
        //onlineNewUsers.setItems(onlineUsers);
    }

    public void setUpUI(){
        //onlineUsers = FXCollections.observableArrayList(OnlineUsersList.getInstance().getAllUsers());
        //assert onlineNewUsers != null;
        onlineNewUsers.setItems(onlineUsers);
    }

    public void setTempList(ChatList input){
        tempList = input;
    }

    public void setClient(ClientSupporter client){
        this.client = client;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setUpList(List<String> online, List<String> user_in_pm, String self){
        boolean found;
        for (int i = 0; i < online.size(); i++){
            found = false;
            for (int j = 0; j < user_in_pm.size(); j++){
                if (online.get(i).equals(user_in_pm.get(j)))
                    found = true;
            }
            if (!found && !self.equals(online.get(i))){
                onlineUsers.add(online.get(i));
            }
        }

        this.self = self;
    }

    @FXML
    public void openNewChat(MouseEvent arg0) throws IOException {

        String receiver = onlineNewUsers.getSelectionModel().getSelectedItem().toString();

        System.out.println("clicked on " + receiver);

        Chat chat = new Chat();

        //Set chat on list
        tempList.addChat(chat, receiver);

        //Open view
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/PrivateChatApplication.fxml"));
        Parent root = loader.load();

        //Prepare scene
        Scene scene = new Scene(root);
        PrivateChatController controller = loader.getController();

        //Controller settings
        controller.setChatData(chat);
        controller.setSender(self);
        controller.setReceiver(receiver);
        controller.setClient(client);
        controller.setUpUI();

        //Open stage
        Stage stage = new Stage();
        stage.setTitle(receiver); //Title is user to chat atm
        stage.setScene(scene);
        stage.show();

        //Hide this view
        this.hide();
    }

    private void hide() {
        this.stage.hide();
    }


}
