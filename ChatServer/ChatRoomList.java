package ua.kiev.prog;

import java.util.LinkedList;
import java.util.List;

public class ChatRoomList {
    private static final ChatRoomList roomList = new ChatRoomList();
    private final List<ChatRoom> list = new LinkedList<>();

    public static ChatRoomList getInstance() {return roomList;}

    public Boolean checkRoom (String name) {
        Boolean response = false;
        for (ChatRoom room: list) {
            if (name.equals(room.getName())) {
                response = true;
                break;
            }
        }
        return response;
    }

    public synchronized void addRoom (ChatRoom name) {
        list.add(name);
    }

    public synchronized ChatRoom getRoom (String name) {
        ChatRoom chatRoom = null;
        for (ChatRoom room : list) {
            if (room.getName().equals(name)) {
                chatRoom = room;
                break;
            }
        }
        return chatRoom;
    }
}
