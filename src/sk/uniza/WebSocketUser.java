package sk.uniza;

import org.java_websocket.WebSocket;

public class WebSocketUser  implements  IUserSocket{

    private final WebSocket socket;
    private final IServerCallBack iServerCallBack;

    public WebSocketUser(WebSocket socket, IServerCallBack iServerCallBack) {
        this.socket = socket;
        this.iServerCallBack = iServerCallBack;
    }

    public WebSocket getSocket() {
        return socket;
    }

    @Override
    public void sendData(String data) {
        socket.send(data);

    }

    @Override
    public void startListening() {

    }

    @Override
    public String toString() {
        return "WebSocketUser{" +
                "Address=" + socket.getLocalSocketAddress() +
                '}';
    }
}
