package sk.uniza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpUserSocket implements IUserSocket {

    private final Socket socket;
    private final IServerCallBack iServerCallBack;

    private final BufferedReader in;
    private final PrintWriter out;

    public TcpUserSocket(Socket socket, IServerCallBack iServerCallBack) throws IOException {
        this.socket = socket;
        this.iServerCallBack = iServerCallBack;

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }


    @Override
    public void sendData(String data) {
        out.println(data);
    }

    @Override
    public void startListening() {
        new Thread(() -> {

            String inputString;
            try {
                while ((inputString = in.readLine()) != null) {
                    iServerCallBack.onReceive(this, inputString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                iServerCallBack.onDisconnect(this);
            }

        }).start();
    }

    @Override
    public String toString() {
        return "TcpUserSocket{" +
                "Address=" + socket.getLocalSocketAddress() +
                '}';
    }
}
