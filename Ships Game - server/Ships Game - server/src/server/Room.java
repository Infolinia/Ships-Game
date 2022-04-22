package server;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private final ServerController serverController = ServerController.getInstance();
    private final ConcurrentHashMap<String, ThreadServer> allClients = new ConcurrentHashMap<>();
    private final String name;
    private int[][] firstOwnShipPosition;
    private int[][] secondOwnShipPosition;

    public int[][] getSecondOwnShipPosition() {
        return secondOwnShipPosition;
    }

    public void setSecondOwnShipPosition(int[][] secondOwnShipPosition) {
        this.secondOwnShipPosition = secondOwnShipPosition;
    }

    public void setFirstOwnShipPosition(int[][] firstOwnShipPosition) {
        this.firstOwnShipPosition = firstOwnShipPosition;
    }

    public int[][] getFirstOwnShipPosition() {
        return firstOwnShipPosition;
    }

    public Room(String name) {
        this.name = name;
    }

    public ConcurrentHashMap<String, ThreadServer> getClients() {
        return allClients;
    }

    public int getClientSize() {
        return allClients.size();
    }

    public String getName() {
        return name;
    }

    public void sendFromServerToMe(String author, String message) {
        for (String name : allClients.keySet()) {
            if (author.equals(name)) {
                allClients.get(name).send(message);
            }
        }
    }

    public synchronized void login(ThreadServer client, String name) {

        if (this.name.equals("Default Room")) {
            if (!allClients.containsKey(name)) {
                allClients.put(name, client);
            }
        } else {
            if (!allClients.containsKey(name) && getClientSize() < 2) {
                allClients.put(name, client);
                serverController.setTextArea("Client logged in: " + name);
                serverController.setTextArea("Player join room: " + this.name);
            } else {
                serverController.setTextArea("Room: " + this.name + " is busy.");
            }
        }
    }

    public synchronized void logout(ThreadServer client, String name) {

        if (this.name.equals("Default Room")) {
            if (allClients.containsKey(name) && allClients.get(name).equals(client)) {
                allClients.remove(name);
            }
        } else {
            if (allClients.containsKey(name) && allClients.get(name).equals(client)) {
                allClients.remove(name);
                serverController.setTextArea("Client logged out: " + name);
                serverController.setTextArea("Player left room: " + this.name);
            }
        }
    }

    public synchronized void logoutAll() {
        for (Entry<String, ThreadServer> entry : allClients.entrySet()) {
            ThreadServer value = entry.getValue();
            value.close();
        }
    }
}
