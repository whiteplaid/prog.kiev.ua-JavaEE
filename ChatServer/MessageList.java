package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
@XmlRootElement (name = "MessageList")
public class MessageList {
	@XmlElement
	private static final MessageList msgList = new MessageList();
    private static final int LIMIT = 100;

 	private final List<Message> list = new LinkedList<Message>();
	
	public static MessageList getInstance() {
		return msgList;
	}
  
  	public MessageList() {
	}

	public synchronized void add(Message m) {
	    if (list.size() + 1 == LIMIT) {
	        list.remove(0);
        }
			list.add(m);
	}
	
	public synchronized XmlMessages toXML (AtomicInteger n) {
		if (n.get() == list.size()) {
			return null;
		} else {
			return new XmlMessages(list,n);
		}
	}
 }
