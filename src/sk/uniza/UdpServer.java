package sk.uniza;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

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

//                for (IUserSocket user : userSocketSet) {
//                    if(user instanceof UdpSocketUser){
//                        final UdpSocketUser user1 = (UdpSocketUser) user;
//                        if(!userSocketSet.contains(user1)){
//                            userSocketSet.add(user1);
//                            iServerCallBack.onConnect(user1);
//                        }else{
//
//                        }
//                    }
//                }
                userSocketSet.stream()
                        .filter(userSocket -> userSocket instanceof UdpSocketUser)
                        //.map(userSocket -> (UdpSocketUser)userSocket)
                        .map(UdpSocketUser.class::cast)
                        .filter(udpSocketUser -> udpSocketUser.getSocketAddress().equals(datagramPacket.getSocketAddress()))
                        .findAny()
                        .ifPresentOrElse(
                                udpSocketUser -> {
                                    final String inputString = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), StandardCharsets.UTF_8);
                                    if (inputString.equalsIgnoreCase("exit")) {
                                        iServerCallBack.onDisconnect(udpSocketUser);
                                    } else {
                                        iServerCallBack.onReceive(udpSocketUser, inputString);
                                    }
                                },
                                () -> {
                                    userSocketCreator.createUser(socket, datagramPacket.getSocketAddress())
                                            .ifPresent(userSocket -> {
                                                iServerCallBack.onConnect(userSocket);
                                                userSocketCreator.registerUser(userSocket, userSocketSet);
                                            });
                                });


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
