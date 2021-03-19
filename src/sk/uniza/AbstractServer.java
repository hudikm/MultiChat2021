package sk.uniza;

import java.util.Set;

public abstract class AbstractServer {
    final protected UserSocketCreator userSocketCreator;
    final protected Set<IUserSocket> userSocketSet;


    protected AbstractServer(UserSocketCreator userSocketCreator, Set<IUserSocket> userSocketSet) {
        this.userSocketCreator = userSocketCreator;
        this.userSocketSet = userSocketSet;
    }

    abstract Thread startServer(int port);
}
