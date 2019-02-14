package client.cellviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.io.IOException;

public class MessageListCellView extends ListCell<Pair<String, String>> {

    private String username;

    @FXML
    private Label senderUsername;
    @FXML
    private Label messageText;

    public MessageListCellView(String username) {
        this.username = username;
    }

    @Override
    protected void updateItem(Pair<String, String> item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            try {
                String location = item.getKey().equals(username) ? "../views/list/RightMessageListCellView.fxml" : "../views/list/LeftMessageListCellView.fxml";

                FXMLLoader loader = new FXMLLoader(MessageListCellView.class.getResource(location));
                loader.setController(this);
                BorderPane rootCellLayout = loader.load();

                senderUsername.setText(item.getKey());
                messageText.setText(item.getValue());

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
