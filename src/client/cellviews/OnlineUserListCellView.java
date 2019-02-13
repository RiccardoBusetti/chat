package client.cellviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class OnlineUserListCellView extends ListCell<String> {

    @FXML
    private Text receiverUsername;

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

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
}
