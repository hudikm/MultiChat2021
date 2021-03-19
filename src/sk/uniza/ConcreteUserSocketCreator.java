package sk.uniza;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Optional;

public class ConcreteUserSocketCreator extends UserSocketCreator{

    @Override
    Optional<IUserSocket> createUser(Socket socket, IServerCallBack iServerCallBack) {

        try {

            return Optional.of(new TcpUserSocket(socket, iServerCallBack));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    Optional<IUserSocket> createUser(DatagramSocket socket, SocketAddress socketAddress) {
        return  Optional.of(new UdpSocketUser(socketAddress,socket));
    }
}
