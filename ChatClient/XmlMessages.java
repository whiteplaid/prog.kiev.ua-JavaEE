package com.ua.kiev.prog;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@XmlRootElement (name = "XmlMessages")
public class XmlMessages {
    private List<Message> list;

    public XmlMessages(List<Message> sourceList, AtomicInteger fromIndex) {
        this.list = new ArrayList<>();
        for (AtomicInteger i = fromIndex; i.get() < sourceList.size(); i.incrementAndGet())
            list.add(sourceList.get(i.get()));
    }

    public XmlMessages() {
    }


    public List<Message> getList() {
        return list;
    }
    @XmlElement
    public void setList(List<Message> list) {
        this.list = list;
    }
}
