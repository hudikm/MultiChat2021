package sk.uniza;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Server implements IServerCallBack {

    private final Set<IUserSocket> userSocketSet = new HashSet<>();
    private final ConcreteUserSocketCreator concreteUserSocketCreator = new ConcreteUserSocketCreator();

    private final Logger logger = Logger.getLogger("Server");

    void startServer() {
        Thread udpThread = new UdpServer(concreteUserSocketCreator, userSocketSet, this).startServer(9000);
        Thread tcpThread = new TcpServer(concreteUserSocketCreator, userSocketSet, this).startServer(9001);

        try {
            tcpThread.join();
            udpThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReceive(IUserSocket userSocket, String data) {
        logger.info("New data received: \"%s\" from %s".formatted(data, userSocket.toString()));

        userSocketSet.stream()
                .filter(userSocket1 -> !userSocket1.equals(userSocket))
                .forEach(userSocket1 -> userSocket1.sendData(data));
    }

    @Override
    public void onDisconnect(IUserSocket userSocket) {
        logger.info(String.format("User: %s", userSocket.toString()));
        userSocketSet.remove(userSocket);
    }

    @Override
    public void onConnect(IUserSocket userSocket) {
        logger.info(String.format("New User connected: %s", userSocket.toString()));

    }
}
