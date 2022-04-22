package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private static final int PORT = 7777;
    private ServerSocket serverSocket;
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }
    private static Server instance = null;

    Room firstRoom = new Room("First Room");
    Room secondRoom = new Room("Second Room");
    Room defaultRoom = new Room("Default Room");

    protected Server() {
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void serverStart() throws IOException {
        if (enabled == false) {
            enabled = true;
            serverSocket = new ServerSocket(PORT);
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void serverStop() throws IOException {
        if (enabled == true) {
            enabled = false;
            firstRoom.logoutAll();
            secondRoom.logoutAll();
            defaultRoom.logoutAll();
            serverSocket.close();
        }
    }

    @Override
    public void run() {
        try {
            while (enabled) {
                Socket socket = serverSocket.accept();
                ThreadServer client = new ThreadServer(socket);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
