package client.handlers;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ChatListV2 {
    private static ChatListV2 instance;

    private List<Pair<String, Chat>> chats;

    public ChatListV2() {
        chats = new ArrayList<>();
    }

    public static ChatListV2 getInstance() {
        if (instance == null) instance = new ChatListV2();

        return instance;
    }

    public void addChat(String user, Chat chat) {
        chats.add(new Pair<>(user, chat));
    }

    public List<Pair<String, Chat>> getAllChats() {
        return chats;
    }

    public Pair<String, Chat> getFullChat(int i) {
        return chats.get(i);
    }

    public String getChatUser(int i) {
        return chats.get(i).getKey();
    }

    public Chat getChat(int i) {
        return chats.get(i).getValue();
    }

    public int size() {
        return chats.size();
    }
}