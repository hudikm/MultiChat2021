package sk.uniza;

import java.util.Set;

public abstract class AbstractServer {
    final protected ConcreteUserSocketCreator concreteUserSocketCreator;
    final protected Set<IUserSocket> userSocketSet;


    protected AbstractServer(ConcreteUserSocketCreator concreteUserSocketCreator, Set<IUserSocket> userSocketSet) {
        this.concreteUserSocketCreator = concreteUserSocketCreator;
        this.userSocketSet = userSocketSet;
    }

    abstract Thread startServer(int port);
}
