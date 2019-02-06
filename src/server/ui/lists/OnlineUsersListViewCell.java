package server.ui.lists;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import server.entities.User;

import java.io.IOException;
import java.net.Socket;

public class OnlineUsersListViewCell extends ListCell<Pair<User, Socket>> {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label socketLabel;

    @Override
    protected void updateItem(Pair<User, Socket> item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            try {
                FXMLLoader loader = new FXMLLoader(RegisteredUsersListViewCell.class.getResource("OnlineUsersListViewCell.fxml"));
                loader.setController(this);
                AnchorPane rootCellLayout = loader.load();

                usernameLabel.setText(item.getKey().getUsername());
                socketLabel.setText(item.getValue().getRemoteSocketAddress().toString());

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
}
