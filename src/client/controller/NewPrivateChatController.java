package client.controller;

import client.handlers.OnlineUsersList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import server.users.OnlineUsers;

import java.util.List;

public class NewPrivateChatController {

    @FXML
    public ListView onlineNewUsers;

    private ObservableList<String> onlineUsers;

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

    public void setUpList(ObservableList<String> online, List user_in_pm){

    }

}
