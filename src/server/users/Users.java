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

    /**
     * Adds a user.
     *
     * @param user        entity representing the user.
     * @param information entity representing other information for the user.
     */
    void addUser(U user, I information);

    /**
     * Removes a specific user.
     *
     * @param username username we want to delete.
     */
    void removeUser(String username);

    /**
     * Removes all the users.
     */
    void removeAllUsers();

    /**
     * Gets a specific user.
     *
     * @param username username we want to search.
     * @return the found user.
     * @throws UserNotFoundException thrown when the user is not found.
     */
    Pair<U, I> getUserByUsername(String username) throws UserNotFoundException;

    /**
     * Gets all the users.
     *
     * @return a list of all users.
     */
    List<Pair<U, I>> getAllUsers();

    /**
     * Observes for any changes in the users list.
     *
     * @param usersObserver observer instance.
     */
    void observe(UsersObserver<U, I> usersObserver);
}
