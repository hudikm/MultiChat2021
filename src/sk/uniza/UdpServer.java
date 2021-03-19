package sk.uniza;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Set;

public class UdpServer extends AbstractServer implements Runnable {

    public static final int BUFFER_SIZE = 150;
    private final IServerCallBack iServerCallBack;
    private DatagramSocket socket;

    protected UdpServer(UserSocketCreator userSocketCreator, Set<IUserSocket> userSocketSet, IServerCallBack iServerCallBack) {
        super(userSocketCreator, userSocketSet);
        this.iServerCallBack = iServerCallBack;
    }

    @Override
    public void run() {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);

        try {
            while (true) {
                socket.receive(datagramPacket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    Thread startServer(int port) {

        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }
}
