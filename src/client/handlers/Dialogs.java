package client.handlers;

import javafx.scene.control.Alert;

public class Dialogs {
    public static void showErrorDialog(String head, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(head);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfoHeadlessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
