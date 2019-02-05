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
import server.ui.lists.OnlineUsersListViewCell;
import server.ui.lists.RegisteredUsersListViewCell;
import server.users.OnlineUsers;
import server.users.RegisteredUsers;

import java.net.Socket;

public class ControlPanelController {

    @FXML
    private TextField serverPortTextField;
    @FXML
    private Button serverActionButton;
    @FXML
    private TabPane usersTabPane;
    @FXML
    private ListView<Pair<User, Socket>> onlineUsersListView;
    @FXML
    private ListView<Pair<User, Boolean>> registeredUsersListView;

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

    private void setUpUI() {
        onlineUsersListView.setItems(onlineUsers);
        onlineUsersListView.setCellFactory(param -> new OnlineUsersListViewCell());

        registeredUsersListView.setItems(registeredUsers);
        registeredUsersListView.setCellFactory(param -> new RegisteredUsersListViewCell());

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

        OnlineUsers.getInstance().observe(users -> {
            Platform.runLater(() -> {
                onlineUsersListView.getItems().clear();
                System.out.println("Online users " + users.size());
                onlineUsersListView.getItems().addAll(users);
            });
        });

        RegisteredUsers.getInstance().observe(users -> {
            Platform.runLater(() -> {
                registeredUsersListView.getItems().clear();
                System.out.println("Registered users " + users.size());
                registeredUsersListView.getItems().addAll(users);
            });
        });
    }

    public void attachMain(Main main) {
        this.main = main;
    }
}
