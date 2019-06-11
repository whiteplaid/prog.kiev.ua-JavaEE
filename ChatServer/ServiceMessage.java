package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class ServiceMessage extends Message{

    public ServiceMessage(String from, String text, String to, String room) {
        super(from, text);
        super.setTo(to);
        super.setRoom(room);
    }

    public String send() {
        return new StringBuilder().append("\n").append(super.getText())
                .toString();
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.toString());
    }


    @Override
    public String toString() {
        return new StringBuilder().append("[").append(super.getDate())
                .append(", From: ").append("SERVER").append(", To: ").append(getTo()).append(", Room: ").append(getRoom())
                .append("] ").append(getText())
                .toString();
    }
}
