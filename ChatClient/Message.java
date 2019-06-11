package com.ua.kiev.prog;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@XmlRootElement (name = "Message")
public class Message {
	private Date date = new Date();
	private String from;
	private String to;
	private String text;
	private String room;

	public Message(String from, String text) {
		this.from = from;
		this.text = text;
	}

    public Message() {
    }

	@Override
	public String toString() {
		return new StringBuilder().append("[").append(date)
				.append(", From: ").append(from).append(", To: ").append(to).append(", Room: ").append(room)
				.append("] ").append(text)
                .toString();
	}

	public int send(String url) throws IOException, JAXBException {
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
	
		OutputStream os = conn.getOutputStream();
        JAXBContext jax = JAXBContext.newInstance(Message.class);
        Marshaller marshaller = jax.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(this,os);
        os.close();
        return conn.getResponseCode();
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
