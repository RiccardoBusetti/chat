package client.handlers;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersList {

    private static OnlineUsersList instance;
    private List<String> users;

    public OnlineUsersList() {
        this.users = new ArrayList<>();
    }

    public static OnlineUsersList getInstance() {
        if (instance == null) instance = new OnlineUsersList();

        return instance;
    }

    public void add(String in) {
        this.users.add(in);
    }

    public void clear() {
        this.users.clear();
    }

    public List<String> getAllUsers() {
        return users;
    }

}
