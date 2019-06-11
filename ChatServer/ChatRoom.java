package ua.kiev.prog;

import java.util.LinkedList;
import java.util.List;


public class ChatRoom {
    private String name;
    private static ChatRoom clientList = new ChatRoom();
    private final List<Client> roomClients = new LinkedList<Client>();
    public static ChatRoom getInstance() {return clientList;}

    public ChatRoom(String name) {
        this.name = name;
    }

    public ChatRoom() {
    }


    public synchronized void addClient (Client client) {
        roomClients.add(client);
    }

    public synchronized void removeClient (Client client) {roomClients.remove(client);}

    public Client getClient (String name) {
        Client client = null;
        for (Client user:roomClients) {
            if (user.getName().equals(name)) {
                client = user;
                break;
            }
        }
        return client;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Client> getRoomClients() {
        return roomClients;
    }

}
