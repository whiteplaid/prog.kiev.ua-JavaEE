package com.ua.kiev.prog;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Scanner;

public class Login {
    private String login;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    public Login() {
    }

    public String getLogin() {
        return login;
    }

    public void login() throws IOException, JAXBException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your login: ");
        String login = scanner.nextLine();
        GetAuth auth = new GetAuth(login);
           if (auth.auth()) {
               this.login = login;
            System.out.println("Enter your password: ");
            String pass = scanner.nextLine();
            if (auth.checkPass(login,pass)) {
                StartChat chat = new StartChat();
                chat.start(login);
            } else {
                System.out.println(ANSI_RED + "Wrong password!" + ANSI_RESET);
                login();
            }
        } else {
            Register register = new Register();
            register.startRegister();
        }
           scanner.close();
    }
}
