package sk.uniza;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class TcpServer extends AbstractServer implements Runnable{

    private final IServerCallBack iServerCallBack;
    private ServerSocket serverSocket;

    public TcpServer(ConcreteUserSocketCreator concreteUserSocketCreator, Set<IUserSocket> users,IServerCallBack iServerCallBack) {
        super(concreteUserSocketCreator,users);
        this.iServerCallBack = iServerCallBack;
    }


    @Override
    public void run() {
            while (true){
                try {
                    Socket socket = serverSocket.accept();

                    userSocketCreator.createUser(socket, iServerCallBack)
                            .ifPresent(userSocket -> {
                                iServerCallBack.onConnect(userSocket);
                                userSocketCreator.registerUser(userSocket, userSocketSet);
                                userSocket.startListening();
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    Thread startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            Thread thread = new Thread(this);
            thread.start();
            return thread;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
