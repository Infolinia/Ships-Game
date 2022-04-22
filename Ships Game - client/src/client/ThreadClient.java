package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadClient {

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public ThreadClient(String clientName) throws IOException {
        try {
            socket = new Socket("localhost", 7777);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.println(clientName);
            out.flush();
            //createThreadRead();
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }

    public void close() {
        System.out.println("Closing ");
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createThreadRead() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("A: " + line);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    close();
                }
            }
        });
        t.start();
    }

    public void sendMessage(String text) {
        try {
            out.println(text);
            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            close();

        }
    }

    public String getMessage() {
        String text = null;
        try {
            text = in.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getCause());
        }
        return text;
    }

}
