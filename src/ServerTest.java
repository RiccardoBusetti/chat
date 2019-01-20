import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import server.MessagingService;
import server.constants.Constants;
import server.entities.User;
import server.io.TxtFilesHelper;
import server.users.RegisteredUsers;
import server.users.Users;
import server.users.UsersObserver;

import java.util.List;

public class ServerTest extends Application implements UsersObserver<User, Boolean> {

    private Text statusText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chat server");

        statusText = new Text();

        //Setting the text to be added.
        statusText.setText("Launching server");

        //setting the position of the text
        statusText.setX(50);
        statusText.setY(50);

        new MessagingService.Builder()
                .onPort(8888)
                .async()
                .build()
                .start();

        StackPane root = new StackPane();
        root.getChildren().add(statusText);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

        RegisteredUsers users = RegisteredUsers.getInstance();
        users.observe(this);
        users.addUser(new User("riccardo", "5mgfk"), false);
        users.addUser(new User("paola", "whfej"), false);
        users.blockUser("riccardo");
    }

    @Override
    public void onUserAdded(List<Pair<User, Boolean>> users) {
        statusText.setText("Added new user " + users.size());
    }

    @Override
    public void onUserModified(List<Pair<User, Boolean>> users) {
        statusText.setText("User modified");
    }
}
