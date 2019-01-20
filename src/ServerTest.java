import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import server.MessagingService;
import server.entities.User;
import server.users.OnlineUsers;
import server.users.UsersObserver;

import java.net.Socket;
import java.util.List;

public class ServerTest extends Application implements UsersObserver<User, Socket> {

    private Text statusText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chat server");

        statusText = new Text();
        statusText.setText("Launching server");
        statusText.setX(50);
        statusText.setY(50);

        StackPane root = new StackPane();
        root.getChildren().add(statusText);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

        new MessagingService.Builder()
                .onPort(8888)
                .async()
                .build()
                .start();

        OnlineUsers.getInstance().observe(this);
    }

    @Override
    public void onUsersChanged(List<Pair<User, Socket>> users) {
        statusText.setText("New user online " + users.get(users.size() - 1).getKey().getUsername());
    }
}
