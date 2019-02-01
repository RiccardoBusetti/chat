package client.controller;

import client.ClientSupporter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ChatController extends Application implements Initializable {

    private String username;
    private ClientSupporter client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        // Save file
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setClient(ClientSupporter client) {
        this.client = client;
    }
}