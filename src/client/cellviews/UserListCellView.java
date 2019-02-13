package client.cellviews;

import client.handlers.Chat;
import client.handlers.ClientSupporter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserListCellView extends ListCell<String> {

    @FXML
    private Text receiverUsername;

    private String username;
    private Chat chat;
    private AnchorPane view;
    private String sender;
    private ClientSupporter client;

    public UserListCellView(ClientSupporter client, String sender) {
        this.client = client;
        this.sender = sender;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        username = item;

        if (item != null && !empty) {
            try {
                String location = "./../views/list/UserChatLayout.fxml";

                FXMLLoader loader = new FXMLLoader(UserListCellView.class.getResource(location));
                loader.setController(this);
                AnchorPane rootCellLayout = loader.load();

                receiverUsername.setText(item);

                setText(null);
                setGraphic(rootCellLayout);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setText(null);
            setGraphic(null);
        }
    }

    @Override
    public String toString() {
        return username;
    }
}
