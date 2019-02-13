package client.controller;

import client.Main;
import client.handlers.Chat;
import client.handlers.ClientSupporter;
import client.handlers.OnlineUsersList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class NewPrivateChatController {

    @FXML
    public ListView onlineNewUsers;

    private ObservableList<String> onlineUsers;
    private ObservableList<String> pmList;

    private ObservableList<Pair<String, Chat>> tempList;

    private String self;
    private Stage stage;
    private ClientSupporter client;

    public NewPrivateChatController() {
        onlineUsers = FXCollections.observableArrayList(OnlineUsersList.getInstance().getAllUsers());
        //assert onlineNewUsers != null;
        //onlineNewUsers.setItems(onlineUsers);
    }

    public void setUpUI() {
        //onlineUsers = FXCollections.observableArrayList(OnlineUsersList.getInstance().getAllUsers());
        //assert onlineNewUsers != null;
        onlineNewUsers.setStyle("-fx-control-inner-background-alt: -fx-control-inner-background");
        onlineNewUsers.setItems(onlineUsers);
    }

    public void setTempList(ObservableList<Pair<String, Chat>> input) {
        tempList = input;
    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUpList(List<String> online, List<String> user_in_pm, String self) {
        boolean found;
        for (String s : online) {
            found = false;
            for (String stringChatPair : user_in_pm) {
                if (s.equals(stringChatPair))
                    found = true;
            }
            if (!found && !self.equals(s)) {
                onlineUsers.add(s);
            }
        }

        this.self = self;
    }

    public void setPmList(ObservableList<String> pmList) {
        this.pmList = pmList;
    }

    @FXML
    public void openNewChat(MouseEvent arg0) throws IOException {

        try {
            String receiver = onlineNewUsers.getSelectionModel().getSelectedItem().toString();

            System.out.println("clicked on " + receiver);

            Chat chat = new Chat();

            //Set chat on list
            tempList.add(new Pair<>(receiver, chat));
            pmList.add(receiver);

            //Open view
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/PrivateChatApplicationV2.fxml"));
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

            chat.setController(controller);

            //Open stage
            Stage stage = new Stage();
            stage.setTitle("LilChat - Chat with " + receiver);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("../client/assets/icon.png")));
            stage.setScene(scene);
            stage.show();

            //Hide this view
            this.hide();
        } catch (NullPointerException nuex) {
            System.out.println("Null pointer occurred");
            return;
        }
    }

    private void hide() {
        this.stage.hide();
    }


}
