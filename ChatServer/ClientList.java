package ua.kiev.prog;

import java.util.*;

public class ClientList {
    private static final ClientList clientList = new ClientList();
    private final List<Client> list = new LinkedList<Client>();
    private Map<ChatRoom,MessageList> roomMessages = Collections.synchronizedMap(new HashMap<ChatRoom,MessageList>());
    public static ClientList getInstance() {return clientList;}

    public synchronized MessageList getMessages (String name) {
        return roomMessages.get(ChatRoomList.getInstance().getRoom(getClient(name).getRoom()));
    }

    public Map<ChatRoom, MessageList> getRoomMessages() {
        return roomMessages;
    }
    public List<Client> clients () {
        return list;
    }
    public synchronized Boolean checkMessages (String name) {
        Boolean response = false;
        if (getClient(name).getRoom() != null && ChatRoomList.getInstance().getRoom(getClient(name).getRoom()) != null) {
            if (getMessages(name) != null) {
                response = true;
            }
        }
        return response;
    }

    public synchronized void addMessage (Message message) {
        ChatRoom room = ChatRoomList.getInstance().getRoom(message.getRoom());
        MessageList messageList = roomMessages.get(room);
        messageList.add(message);
    }
    public synchronized void add (String name) {
        if (ClientList.getInstance().getClient(name) != null) {
            if (!ChatRoomList.getInstance().checkRoom(ClientList.getInstance().getClient(name).getRoom())) {
                ChatRoom room = new ChatRoom(ClientList.getInstance().getClient(name).getRoom());
                ChatRoomList.getInstance().addRoom(room);
                roomMessages.put(room, new MessageList());
           } else {
                if (!checkMessages(name))
               roomMessages.put(ChatRoomList.getInstance().getRoom(ClientList.getInstance().getClient(name).getRoom()), new MessageList());
           }
        }
    }

    public Boolean checkClient (String client) {
        Boolean response = false;
        for (Client user: list) {
            if (client.equals(user.getName())) {
                response = true;
                break;
            }
        }
        return response;
    }
    public Boolean checkPass (String pass) {
        Boolean response = false;
        String[] auth = pass.split(",");
        for (Client user: list) {
            if (user.getName().equals(auth[0])) {
                if (auth[1].equals(user.getPassword())) {
                    response = true;
                    break;
                }
            }
        }
        return response;
    }

    public synchronized void addClient (Client client) {
        list.add(client);
    }
    public synchronized Client getClient (String name) {
        Client client = null;
        for (Client user:list) {
            if (user.getName().equals(name)) {
                client = user;
            }
        }
        return client;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String status;
        for (Client user:list) {
            if (user.getStatus()) status = "online"; else status = "offline";
            sb.append("\n").append(user.getName()).append("\t").append("Status: ").append(status).append("\t").append("Room: ").append(user.getRoom());
        }
        return sb.toString();
    }
}
