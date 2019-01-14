package data.entities;

/**
 * POJO that represents the online user, which is an entity that has
 * a POJO describing its data and an Object representing the client used to connect.
 * @param <U> user POJO
 * @param <C> client Object
 */
public class OnlineUser<U, C> {

    private U user;
    private C client;

    public OnlineUser() {
    }

    public OnlineUser(U user, C client) {
        this.user = user;
        this.client = client;
    }

    public U getUser() {
        return user;
    }

    public void setUser(U user) {
        this.user = user;
    }

    public C getClient() {
        return client;
    }

    public void setClient(C client) {
        this.client = client;
    }
}
