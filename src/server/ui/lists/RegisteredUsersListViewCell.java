package server.ui.lists;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import server.entities.User;

import java.io.IOException;

public class RegisteredUsersListViewCell extends ListCell<Pair<User, Boolean>> {

    @FXML
    Label usernameLabel;
    @FXML
    Button blockUserButton;

    @Override
    protected void updateItem(Pair<User, Boolean> item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            try {
                FXMLLoader loader = new FXMLLoader(RegisteredUsersListViewCell.class.getResource("RegisteredsUserListViewCell.fxml"));
                loader.setController(this);
                AnchorPane rootCellLayout = loader.load();

                usernameLabel.setText(item.getKey().getUsername());
                blockUserButton.setText(item.getValue() ? "Sblocca" : "Blocca");

                setText(null);
                setGraphic(rootCellLayout);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
