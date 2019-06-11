package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@XmlRootElement (name = "Message")
public class Message {
	private Date date = new Date();
	private String from;
	private String to;
	private String text;
	private String room;
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_GREEN = "\u001B[32m";
	public Message(String from, String text) {
		this.from = from;
		this.text = text;
	}

	public Message() {
	}

	public String toJSON() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this.toString());
	}
	
	public static Message fromJSON(String s) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(s, Message.class);
	}

	private boolean contains (String text) {
		Boolean out = false;
		String[] con = {"@","room#","join#"};
		for (String string : con) {
			if (text.contains(string)) {
				out = true;
			}
		}
		return out;
	}

	private synchronized void joinRoom (String sourceroom, String joinroom) {
		ChatRoom source = ChatRoomList.getInstance().getRoom(sourceroom);
		source.removeClient(source.getClient(this.from));
		ClientList.getInstance().getClient(from).setRoom(joinroom);
		ChatRoomList.getInstance().getRoom(joinroom).addClient(ClientList.getInstance().getClient(from));
		ClientList.getInstance().getClient(from).setRoom(ClientList.getInstance().getClient(from).getRoom());
		ClientList.getInstance().add(from);
		ClientList.getInstance().addMessage(new ServiceMessage("SERVER",from + " joined room " + joinroom,null,sourceroom));
		ClientList.getInstance().addMessage(new ServiceMessage("SERVER",from + " entered room " + joinroom,null,joinroom));
	}

	private String sendPrivate (String[] tmp) {
		if (ClientList.getInstance().checkClient(tmp[0]) && ClientList.getInstance().getClient(tmp[0]).getStatus()) {
			this.to = tmp[0];
			return tmp[1];
		} else if (ClientList.getInstance().checkClient(tmp[0]) && !ClientList.getInstance().getClient(tmp[0]).getStatus()) {
			this.to = this.from;
			return  "\n" + "The user " + tmp[0] + " is offline";
		} else {
			this.to = this.from;
			return  "\n" + "There is no user whith name " + tmp[0];
		}
	}

	private void sendService (String text, String to, String room) {
		this.to = to;
		ClientList.getInstance().getClient(from).setRoom(ClientList.getInstance().getClient(from).getRoom());
		ClientList.getInstance().addMessage(new ServiceMessage("SERVER","\n" + text,to,room));
	}
	private String textOut (String text) {
		String out = "";
		if (text.contains("@")) {
			String[] tmp = text.split("@");
			out = sendPrivate(tmp);
		} else if (text.contains("room#")) {
			String[] tmp = text.split("#");
			joinRoom(ClientList.getInstance().getClient(from).getRoom(),tmp[1]);
			sendService("room " + tmp[1] + " created",from,tmp[1]);
		} else if (text.contains("join#")) {
			String[] tmp = text.split("#");
			if (ClientList.getInstance().getClient(from).getRoom() == tmp[1]) {
				sendService("You already in this room",from,tmp[1]);
			} else {
				joinRoom(ClientList.getInstance().getClient(from).getRoom(),tmp[1]);
				sendService("You are joined room: " + tmp[1],from,tmp[1]);
			}
		}
		return out;
	}

	private String equals (String text) {
		String out = "";
		if (text.equals("quit")) {
			ClientList.getInstance().getClient(from).setStatus(false);
			sendService(from + " Disconnected", null, "all");
			out = null;
		} else if (text.equals("help")) {
			sendService("Available Commands: " + Commands.getInstance().availableCommands(),from,ClientList.getInstance().getClient(from).getRoom());
		} else if (text.equals("exit")){
			String eroom = ClientList.getInstance().getClient(this.from).getRoom();
			if (eroom != "all") {
				sendService("You are quited room " + eroom,from,"all");
				joinRoom(eroom, "all");
				setRoom("all");
			} else {
				sendService("You can't exit from main room",from,"all");
			}
		} else if (text.equals("list")) {
			sendService("Registered users:" + ClientList.getInstance().toString(),from,ClientList.getInstance().getClient(from).getRoom());
		} else {
			out = text;
		}
		return out;
	}

	public String checkMessage(String text) throws Exception {
		ClientList.getInstance().getClient(from).setRoom(ClientList.getInstance().getClient(from).getRoom());
		if (text.startsWith("/")) {
			String operator = text.substring(1);
			String out = Commands.getInstance().checkCommand(operator,from);
			if (contains(out)) {
				return this.text = textOut(out);
			} else {
				return this.text = equals(out);
			}
		} else {
			return text;
		}
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("[").append(date)
				.append(", From: ").append(from).append(", To: ").append(to).append(", Room: ").append(room)
				.append("] ").append(text)
                .toString();
	}

	public int send(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
	
		OutputStream os = conn.getOutputStream();
		try {
			String json = toJSON();
			os.write(json.getBytes(StandardCharsets.UTF_8));
			return conn.getResponseCode();
		} finally {
			os.close();
		}
	}
	@XmlElement
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
@XmlElement
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
@XmlElement
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
@XmlElement
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
@XmlElement
	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
}
