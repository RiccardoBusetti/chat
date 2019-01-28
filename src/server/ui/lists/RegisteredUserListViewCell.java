package server.ui.lists;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.util.Pair;
import server.entities.User;

import java.io.IOException;

public class RegisteredUserListViewCell extends ListCell<Pair<User, Boolean>> {

    @FXML
    Label usernameLabel;
    @FXML
    Button blockUserButton;

    @Override
    protected void updateItem(Pair<User, Boolean> item, boolean empty) {
        super.updateItem(item, empty);

        try {
            FXMLLoader loader = new FXMLLoader(RegisteredUserListViewCell.class.getResource("RegisteredUserListViewCell.fxml"));
            loader.load();

            usernameLabel.setText(item.getKey().getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
