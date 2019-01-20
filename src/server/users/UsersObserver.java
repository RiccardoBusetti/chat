package server.users;

import javafx.util.Pair;

import java.util.List;

/**
 * Interface containing all the callbacks for the users.
 *
 * @param <U> user POJO
 * @param <I> used to store additional user information
 */
public interface UsersObserver<U, I> {
    void onUsersChanged(List<Pair<U, I>> users);
}
