package sk.uniza;

import org.java_websocket.WebSocket;

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

    @Override
    Optional<IUserSocket> createUser(WebSocket socket, IServerCallBack iServerCallBack) {
        return Optional.of(new WebSocketUser(socket,iServerCallBack));
    }
}
