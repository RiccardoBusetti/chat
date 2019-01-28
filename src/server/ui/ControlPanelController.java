package server.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import server.entities.User;
import server.ui.lists.RegisteredUserListViewCell;
import server.users.OnlineUsers;
import server.users.RegisteredUsers;

import java.net.Socket;

public class ControlPanelController {

    @FXML
    TextField serverPortTextField;
    @FXML
    Button serverActionButton;
    @FXML
    TabPane usersTabPane;
    @FXML
    ListView<Pair<User, Socket>> onlineUsersListView;
    @FXML
    ListView<Pair<User, Boolean>> registeredUsersListView;

    private Main main;
    private int port = 8888;
    private boolean isReadyToStart = true;

    private ObservableList<Pair<User, Socket>> onlineUsers;
    private ObservableList<Pair<User, Boolean>> registeredUsers;

    public ControlPanelController() {
        onlineUsers = FXCollections.observableArrayList(OnlineUsers.getInstance().getAllUsers());
        registeredUsers = FXCollections.observableArrayList(RegisteredUsers.getInstance().getAllUsers());
    }

    @FXML
    public void initialize() {
        setUpUI();
        addListeners();
    }

    // TODO: implement custom cells for each list.
    private void setUpUI() {
        onlineUsersListView.setItems(onlineUsers);

        registeredUsersListView.setItems(registeredUsers);
        //registeredUsersListView.setCellFactory(param -> new RegisteredUserListViewCell());

        updateUI();
    }

    private void updateUI() {
        updateActionButtonText();
        updatePortTextFieldStatus();
        updateUsersTabsStatus();
    }

    private void updateActionButtonText() {
        if (isReadyToStart) {
            serverActionButton.setText("Avvia server");
        } else {
            serverActionButton.setText("Arresta server");
        }
    }

    private void updatePortTextFieldStatus() {
        serverPortTextField.setDisable(!isReadyToStart);
    }

    private void updateUsersTabsStatus() {
        usersTabPane.setDisable(isReadyToStart);
    }

    private void addListeners() {
        serverPortTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int inputPort = Integer.valueOf(newValue);

            if (inputPort >= 1024 && inputPort <= 65353) {
                port = inputPort;
            }
        });

        serverActionButton.setOnAction(event -> {
            if (isReadyToStart) {
                main.startServer(port);
            } else {
                Platform.exit();
                System.exit(1);
            }

            isReadyToStart = !isReadyToStart;
            updateUI();
        });

        // TODO: try to execute the changes on the UI thread.
        OnlineUsers.getInstance().observe(users -> {
            onlineUsers.removeAll();
            onlineUsers.addAll(users);
        });

        RegisteredUsers.getInstance().observe(users -> {
            registeredUsers.removeAll();
            registeredUsers.addAll(users);
        });
    }

    public void attachMain(Main main) {
        this.main = main;
    }
}
