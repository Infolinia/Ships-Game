package client;

import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;

public class Connect {

    public boolean isConneted() {
        try (Socket s = new Socket("localhost", 7777)) {
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    public void checking() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(100);
                        Socket socket = new Socket("localhost", 7777);
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        new AlertBox("Server Connection", "Server is Offline. Error Server Connection!");
                    });

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    System.out.println(ex.getCause());
                }
            }

        });
        t.start();
    }

}
