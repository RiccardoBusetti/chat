package server.users;

/**
 * Class responsible of managing common actions that are shared
 * between each users implementation.
 *
 * @param <U> user POJO
 * @param <I> used to store additional user information
 */
/* package */ abstract class ServiceUsers<U, I> implements Users<U, I> {
    /* package */ UsersObserver<U, I> usersObserver;

    /* package */ void attachObserver(UsersObserver<U, I> usersObserver) {
        this.usersObserver = usersObserver;
    }

    /* package */ void detachObserver() {
        this.usersObserver = null;
    }

    /* package */ boolean isObserverAttached() {
        return usersObserver != null;
    }
}
