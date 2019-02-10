package client.handlers;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private List<Pair<String, String>> messages;

    public Chat(){
        messages = new ArrayList<>();
    }

    public void addMessage(String sender, String message){
        messages.add(new Pair<>(sender, message));
    }

    public Pair<String, String> getMessage(int i){
        return messages.get(i);
    }

    public int getAmountMessage(){
        return messages.size();
    }

    public List<Pair<String, String>> getMessages(){
        return messages;
    }

}
