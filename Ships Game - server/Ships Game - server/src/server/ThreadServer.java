package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public class ThreadServer {

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private String name;
    private Room room;

    public ThreadServer(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        createThread();
    }

    private int[][] intToDeep(String str) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '[') {
                row++;
            }
        }
        row--;
        for (int i = 0;; i++) {
            if (str.charAt(i) == ',') {
                col++;
            }
            if (str.charAt(i) == ']') {
                break;
            }
        }
        col++;

        int[][] out = new int[row][col];
        str = str.replaceAll("\\[", "").replaceAll("\\]", "");
        String[] s1 = str.split(", ");

        int j = -1;
        for (int i = 0; i < s1.length; i++) {
            if (i % col == 0) {
                j++;
            }
            out[j][i % col] = Integer.valueOf(s1[i]);
        }
        return out;
    }

    private void chooseRoom(String message) {
        if (message.contains(":")) {
            String[] parts = message.split(":");
            if (parts[0].equals("joinRoom")) {
                room = selectRoom(parts[1]);
            }
        }
    }

    private Room selectRoom(String message) {
        switch (message) {
            case "First Room":
                return Server.getInstance().firstRoom;
            case "Second Room":
                return Server.getInstance().secondRoom;
            case "Default Room":
                return Server.getInstance().defaultRoom;
        }
        return null;
    }

    private void leaveRoom(String message) {
        if (message.equals("leaveRoom")) {
            close();
        }
    }

    private void received(String message) {
        if (name == null) {
            name = message;
        } else if (room == null) {
            chooseRoom(message);
            room.login(this, name);
        }
        leaveRoom(message);
        onlinePlayerRoom(message);
        checkNickExist(message);
        sendShipPosition(message);
        isShipOnPosition(message);
    }

    public void onlinePlayerRoom(String message) {
        if (message.contains(":")) {
            if (message.length() > 16 && message.substring(0, 16).equals("onlinePlayerRoom")) {
                String[] parts = message.split(":");
                room.sendFromServerToMe(name, String.valueOf(selectRoom(parts[1]).getClientSize()));
            }
        }
    }

    public void sendShipPosition(String message) {
        if (message.contains(":")) {
            if (message.substring(2, 14).equals("shipPosition")) {
                String[] parts = message.split(":");
                if (message.substring(0, 1).equals("0")) {
                    room.setFirstOwnShipPosition(intToDeep(parts[2]));
                } else {
                    room.setSecondOwnShipPosition(intToDeep(parts[2]));
                }
            }
        }
    }

    public void isShipOnPosition(String message) {
        if (message.contains(":")) {
            if (message.substring(2, 16).equals("isShipPosition")) {
                String[] parts = message.split(":");
                int x = Integer.valueOf(parts[2]);
                int y = Integer.valueOf(parts[3]);
                if (message.substring(0, 1).equals("0")) {
                    if (room.getSecondOwnShipPosition()[x][y] == 1) {
                        room.sendFromServerToMe(name, "true");
                    } else {
                        room.sendFromServerToMe(name, "false");
                    }
                } else {
                    if (room.getFirstOwnShipPosition()[x][y] == 1) {
                        room.sendFromServerToMe(name, "true");
                    } else {
                        room.sendFromServerToMe(name, "false");
                    }
                }
            }
        }
    }

    public void checkNickExist(String message) {
        if (message.contains(":")) {
            if (message.length() > 14 && message.substring(0, 14).equals("checkNickExist")) {
                String[] parts = message.split(":");
                boolean x = false;
                for (Map.Entry<String, ThreadServer> entry : selectRoom(parts[2]).getClients().entrySet()) {
                    if (entry.getKey().equals(parts[1])) {
                        x = true;
                    }
                }
                room.sendFromServerToMe(name, String.valueOf(x));
            }
        }
    }

    public void send(String message) {
        try {
            writer.println(message);
            writer.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (room != null && name != null) {
                room.logout(this, name);
                room = null;
                name = null;
                writer.close();
                reader.close();
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        received(line);
                    }
                } catch (IOException e) {
                    close();
                    System.out.println(e.getMessage());
                }
            }
        });
        t.start();
    }

}
