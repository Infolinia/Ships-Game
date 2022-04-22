package client;

import java.io.IOException;
import java.util.Arrays;

public class Player {

    private ThreadClient threadClient;
    private String nick;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player() {
    }

    public void login(String nick) throws IOException {
        this.nick = nick;
        this.threadClient = new ThreadClient(nick);
    }

    public void joinRoom(String name) {
        threadClient.sendMessage("joinRoom:" + name);
    }

    public int onlinePlayerRoom(String name) {
        threadClient.sendMessage("onlinePlayerRoom:" + name);
        int x = Integer.valueOf(threadClient.getMessage());
        return x;
    }

    public boolean checkNickExist(String name, String room) {
        threadClient.sendMessage("checkNickExist:" + name + ":" + room);
        return Boolean.valueOf(threadClient.getMessage());
    }

    public void leaveRoom() {
        threadClient.sendMessage("leaveRoom");
    }

    // sprawdzanie czy wtablicy wroga jest statek na danej pozycji
    public boolean isShipOnPosition(int x, int y) {
        String message = getId() + ":isShipPosition:";
        threadClient.sendMessage(message + x + ":" + y);
        return Boolean.valueOf(threadClient.getMessage());
    }

    // wywyłanie pozycji ustawionych statków na serwer
    void sendShipPosition(int[][] shipPositionArray) {
        String message = getId() + ":shipPosition:";
        threadClient.sendMessage(message + Arrays.deepToString(shipPositionArray));
    }

}
