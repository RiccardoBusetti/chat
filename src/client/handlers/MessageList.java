package client.handlers;

import javafx.util.Pair;

import java.util.*;

public class MessageList {

    private static MessageList instance;

    private List<Pair<String, String>> messages;

    public MessageList(){
        messages = new ArrayList<>();
    }

    public static MessageList getInstance() {
        if (instance == null) instance = new MessageList();

        return instance;
    }

    public void addMessage(String user, String message){
        messages.add(new Pair<>(user, message));
    }

    public List<Pair<String, String>> getAllMessages(){
        return messages;
    }

}
