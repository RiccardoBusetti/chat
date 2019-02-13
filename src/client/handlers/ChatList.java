package client.handlers;

import java.util.ArrayList;
import java.util.List;

public class ChatList {

    private static ChatList instance;

    private List<Chat> chatList;
    private List<String> receivers;

    public ChatList() {
        chatList = new ArrayList<>();
        receivers = new ArrayList<>();
    }

    public static ChatList getInstance() {
        if (instance == null) instance = new ChatList();

        return instance;
    }

    public void addChat(String user) {
        chatList.add(new Chat());
        receivers.add(user);
    }

    public void addChat(Chat chat, String user) {
        chatList.add(chat);
        receivers.add(user);
    }

    public List<String> getUsers() {
        return receivers;
    }

    public Chat getChat(int i) {
        return chatList.get(i);
    }

    public int getSize() {
        return chatList.size();
    }
}
