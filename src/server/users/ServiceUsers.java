package server.users;

import javafx.util.Pair;
import server.entities.User;
import server.exceptions.UserNotFoundException;

import java.util.List;

/**
 * Class responsible of managing common actions that are shared
 * between each users implementation.
 *
 * @param <U> user POJO
 * @param <I> used to store additional user information
 */
/* package */ abstract class ServiceUsers<U, I> implements Users<U, I> {
    /* package */ UsersObserver<U, I> usersObserver;

    /**
     * Attaches the observer.
     *
     * @param usersObserver observer instance.
     */
    /* package */ void attachObserver(UsersObserver<U, I> usersObserver) {
        this.usersObserver = usersObserver;
    }

    /**
     * Detaches the observer.
     */
    /* package */ void detachObserver() {
        this.usersObserver = null;
    }

    /**
     * Checks if the observer is observing the users changes.
     *
     * @return true if the observer is attached false otherwise.
     */
    /* package */ boolean isObserverAttached() {
        return usersObserver != null;
    }

    /**
     * Searches a user by his username on a list.
     *
     * @param username         username to search for.
     * @param users            users list to search in.
     * @param isRegisteredUser tells the exception if the user is not found in
     *                         the registered users or in the online users.
     * @return the user object pair.
     * @throws UserNotFoundException thrown when the user is not found.
     */
    /* package */ Pair<U, I> searchUserByUsername(String username, List<Pair<U, I>> users, boolean isRegisteredUser) throws UserNotFoundException {
        int counter = 0;

        while (counter < users.size()) {
            Pair<U, I> userPair = users.get(counter);

            if (userPair.getKey() instanceof User) {
                User user = (User) userPair.getKey();

                if (user.getUsername().equals(username)) {
                    return userPair;
                }
            }

            counter++;
        }

        throw new UserNotFoundException(isRegisteredUser);
    }
}
