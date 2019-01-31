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

public class RegisteredUserListViewCell extends ListCell<Pair<User, Boolean>> {

    @FXML
    private Label usernameLabel;
    @FXML
    private Button blockUserButton;

    @Override
    protected void updateItem(Pair<User, Boolean> item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null | empty) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(RegisteredUserListViewCell.class.getResource("RegisteredUserListViewCell.fxml"));
                AnchorPane anchorPane = loader.load();

                setText(null);
                setGraphic(anchorPane);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }
}
