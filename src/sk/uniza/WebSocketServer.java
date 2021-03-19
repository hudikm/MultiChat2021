package sk.uniza;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Set;

public class WebSocketServer extends AbstractServer {

    private final IServerCallBack iServerCallBack;

    protected WebSocketServer(UserSocketCreator userSocketCreator, Set<IUserSocket> userSocketSet, IServerCallBack iServerCallBack) {
        super(userSocketCreator, userSocketSet);
        this.iServerCallBack = iServerCallBack;
    }

    private class InnerWebSocketServer extends org.java_websocket.server.WebSocketServer {


        public InnerWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
            userSocketCreator.createUser(webSocket, iServerCallBack)
                    .ifPresent(userSocket -> {
                        userSocketCreator.registerUser(userSocket, userSocketSet);
                        iServerCallBack.onConnect(userSocket);
                    });
        }

        @Override
        public void onClose(WebSocket webSocket, int i, String s, boolean b) {
            userSocketCreator.createUser(webSocket, iServerCallBack)
                    .ifPresent(userSocket -> {
                        iServerCallBack.onDisconnect(userSocket);
                    });
        }

        @Override
        public void onMessage(WebSocket webSocket, String s) {
            userSocketSet.stream()
                    .filter(WebSocketUser.class::isInstance)
                    .map(WebSocketUser.class::cast)
                    .filter(webSocketUser -> webSocketUser.getSocket().equals(webSocket))
                    .findFirst()
                    .ifPresent(webSocketUser -> {
                        iServerCallBack.onReceive(webSocketUser, s);
                    });
        }

        @Override
        public void onError(WebSocket webSocket, Exception e) {

        }

        @Override
        public void onStart() {

        }
    }

    @Override
    Thread startServer(int port) {
        final InnerWebSocketServer innerWebSocketServer = new InnerWebSocketServer(new InetSocketAddress(port));

        final Thread thread = new Thread(innerWebSocketServer::run);
        thread.start();

        return thread;
    }
}
