package ua.kiev.prog;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonMessages {
    private final List<Message> list;


    public JsonMessages(List<Message> sourceList, AtomicInteger fromIndex) {
        this.list = new ArrayList<>();
        for (AtomicInteger i = fromIndex; i.get()< sourceList.size(); i.incrementAndGet())
            list.add(sourceList.get(i.get()));
    }
}
