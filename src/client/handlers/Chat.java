package client.handlers;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private List<String> messages;
    private List<String> senders;

    public Chat(){
        messages = new ArrayList<>();
        senders = new ArrayList<>();
    }

    public void addMessage(String sender, String message){
        messages.add(message);
        senders.add(sender);
    }

    public Pair<String, String> getMessage(int i){
        return new Pair<>(senders.get(i), messages.get(i));
    }

    public int getAmountMessage(){
        return messages.size();
    }

    public List<String> getMessages(){
        return messages;
    }

}
