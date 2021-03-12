package sk.uniza;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Optional;

public abstract class UserSocketCreator {

    abstract Optional<IUserSocket> createUser(Socket socket, IServerCallBack iServerCallBack);

    abstract Optional<IUserSocket> createUser(DatagramSocket socket, SocketAddress socketAddress);

//    abstract Optional<IUserSocket> createUser(WebSocket socket, IServerCallBack iServerCallBack);

    final boolean registerUser(IUserSocket userSocket, Collection<IUserSocket> userSockets) {
        if (userSocket != null && userSockets != null) {
            return userSockets.add(userSocket);
        }
        return false;
    }

}
