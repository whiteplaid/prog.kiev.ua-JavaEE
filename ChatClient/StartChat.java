package com.ua.kiev.prog;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Scanner;

public class StartChat {
    private String login;
    public StartChat() {
    }
    public void start (String login) throws IOException, JAXBException {
        Thread th = new Thread(new GetThread(login));
        th.setDaemon(true);
        th.start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your message: ");
        while (!th.isInterrupted()) {
            String text = scanner.nextLine();
            Message m = new Message(login, text);

            int res = m.send(Utils.getURL() + "/add");
            if (res == 410) {
                th.interrupt();

            } else if (res != 200 && res !=410) { // 200 OK
                System.out.println("HTTP error occured: " + res);
                return;
            }
        }
        scanner.close();
    }
}
