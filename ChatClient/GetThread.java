package com.ua.kiev.prog;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class GetThread implements Runnable {
    private int n;
    private String login;
    private String room;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    public GetThread(String login) {
        this.login = login;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/get?from=" + n + "&login=" + login);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream is = http.getInputStream();
                AtomicInteger out = new AtomicInteger(n);
                try {
                    byte[] buf = requestBodyToArray(is);
                    String strBuf = new String(buf,StandardCharsets.UTF_8);
                    if (!strBuf.equals("")) {
                        XmlMessages list = getXML(strBuf);
                        if (list.getList() != null) {
                            for (Message m : list.getList()) {
                               if (checkMessage(m)) n = out.incrementAndGet();
                            }
                        }
                    }
                } finally {
                    is.close();
                }
                Thread.sleep(500);
            }
        } catch (SocketException e) {
            System.out.println(ANSI_RED + "No Connection" + ANSI_RESET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkMessage (Message m) {
        boolean response = false;
            if (m.getTo() == null && !m.getFrom().equals("SERVER")) {
                System.out.println(m);
                response = true;
            } else if (m.getTo() == null && m.getFrom().equals("SERVER") || m.getTo().equals(login) && m.getFrom().equals("SERVER")) {
                System.out.println("\u001b[32m" + m.getText() + "\u001B[0m");
                response = true;
            } else if (m.getTo().equals(login)) {
                System.out.println("\u001B[32m" + m + "\u001B[0m");
                response = true;
        }
        return response;
    }
    private XmlMessages getXML (String buf) {
        XmlMessages msgs = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlMessages.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            msgs = (XmlMessages) unmarshaller.unmarshal(new StringReader(buf));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return msgs;
    }
    private byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
