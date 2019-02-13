package client.handlers;

import client.controller.PrivateChatController;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private List<Pair<String, String>> messages;
    private String sender, receiver;
    private PrivateChatController controller;
    private long latestMessageTimestamp;

    public Chat(){
        messages = new ArrayList<>();
        latestMessageTimestamp = System.currentTimeMillis();
    }

    public void addMessage(String sender, String message){
        messages.add(new Pair<>(sender, message));
        latestMessageTimestamp = System.currentTimeMillis();
    }

    public void setController(PrivateChatController controller) {
        this.controller = controller;
    }

    public void updateUI(){
        try{
            controller.updateUI();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something happened. Maybe it simply controller pointer got lost, lol");
        }
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

    public long getLatestMessageTimestamp(){
        return latestMessageTimestamp;
    }

}
