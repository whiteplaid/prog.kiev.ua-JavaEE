package ua.kiev.prog;

import java.util.HashMap;
import java.util.Map;

public class Commands {
    private static final Commands commands = new Commands();
    private final Map<String,String> list = new HashMap<>();
    private String to;
    public static Commands getInstance() {return commands;}
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public Commands() {
        list.put("help","/help - list of available commands");
        list.put("to","/to <user name> <text> - send private message");
        list.put("room","/room <name of the room> - create chat room");
        list.put("join","/join <name of the room> - join created chat room");
        list.put("exit","/exit - return to main chat");
        list.put("quit","/quit - quit chat program");
        list.put("list","/list - list of all registered users");
    }


    public String availableCommands () {
        String all = "";
        for (String value:list.values()) {
            all += "\n" + value;
        }
        return all;
    }
    private String getCommand (String command) {
        String out ="";
        if (list.containsKey(command)) {
            out = command;
        }
        return out;
    }

    public String checkCommand (String text,String to) throws Exception {
        String out = "";
        String[] in = text.split(" ");
        if(!getCommand(in[0]).equals("")) {
            if (checkLength(text)) {
                out = textOut(text,to);
            } else {
               out =  new ServiceMessage(to,"Check command statement: " + list.get(in[0]),to,ClientList.getInstance().getClient(to).getRoom()).send();
            }
        } else {
           out = new ServiceMessage(to,"No Such Command. Use /help for available commands",to,ClientList.getInstance().getClient(to).getRoom()).send();
        }
        return out;
    }
    private String equals (String[] in, String to) throws Exception {
        String out = "";
        if (in[0].equals("room")) {
            if (!ChatRoomList.getInstance().checkRoom(in[1])) {
                ChatRoom room = new ChatRoom(in[1]);
                ChatRoomList.getInstance().addRoom(room);
                out = "room#" + in[1];
            } else {
               out = new ServiceMessage(to,"Room with name " + in[1] + " already existed",to,in[1]).send();
            }
        } else if (in[0].equals("join")) {
            if (ChatRoomList.getInstance().checkRoom(in[1])) {
                out = "join#" + in[1];
            } else {
               out = new ServiceMessage(to,"No such room were created",to,in[1]).send();
            }
        }
        return out;
    }
    private String textOut (String text, String to) throws Exception{
        String out;
        String[] in = text.split(" ");
        if (in[0].equals("to")) {
            out = new PrivateMessage(in[1], in[2]).send();
        } else if (in[0].equals("quit") || in[0].equals("exit") || in[0].equals("list") || in[0].equals("help")) {
            out = in[0];
        } else {
            out = equals(in,to);
        }
        return out;
    }
    private Boolean checkLength(String text) {
        Boolean out = false;
        String[] one = {"help","list","exit","quit"};
        String[] two = {"room","join"};
        String[] put = text.split(" ");
        if (put.length==1) {
            for (String string : one) {
                if (string.equals(text)) out = true;
            }
        } else if (put.length == 2) {
            for (String string : two) {
                if (string.equals(put[0])) out = true;
            }
        } else {
            if (put[0].equals("to")) out = true;
        }
        return out;
    }
}
