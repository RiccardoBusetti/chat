package server.users;

import javafx.util.Pair;
import server.exceptions.UserNotFoundException;

import java.util.List;

/**
 * Interface representing the way we treat users generically inside
 * of the chat, independently from the implementation.
 *
 * @param <U> user POJO
 * @param <I> used to store additional user information
 */
public interface Users<U, I> {
    void addUser(U user, I information);

    void removeUser(String username);

    void removeAllUsers();

    Pair<U, I> getUserByUsername(String username) throws UserNotFoundException;

    List<Pair<U, I>> getAllUsers();

    void observe(UsersObserver<U, I> usersObserver);
}
