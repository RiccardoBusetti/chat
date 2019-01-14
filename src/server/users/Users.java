package server.users;

import data.entities.OnlineUser;
import exceptions.UserNotFoundException;

import java.util.List;

/**
 * Interface representing the way we treat users generically inside
 * of the chat, independently from the implementation.
 * @param <U> user POJO
 * @param <C> client Object
 */
public interface Users<U, C> {
    void userConnected(U user, C clientSocket);

    void userConnected(OnlineUser<U, C> onlineUser);

    void userDisconnected(String username);

    void disconnectAllUsers();

    OnlineUser<U, C> getUserByUsername(String username) throws UserNotFoundException;

    List<OnlineUser<U, C>> getAllUsers();
}
