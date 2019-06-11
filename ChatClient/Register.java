package com.ua.kiev.prog;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Register {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";


    public Register() {
    }
    public void startRegister() throws JAXBException {
        System.out.println(ANSI_RED + "You are not registered" + ANSI_RESET);
        System.out.println("'R' to register, 'L' to type login again");
        Scanner scanner = new Scanner(System.in);
        try {
            String resp = scanner.nextLine();
            if (resp.toLowerCase().equals("r")) {
                System.out.println("Enter your name: ");
                String name = scanner.nextLine();
                System.out.println("Enter your password: ");
                String pass = scanner.nextLine();
                if (registerUser(name, pass) == 0) {
                    StartChat chat = new StartChat();
                    chat.start(name);
                } else if (registerUser(name,pass) == 1) {
                    System.out.println(ANSI_RED + "Something wrong" + ANSI_RESET);
                    startRegister();
                } else if (registerUser(name,pass) == 2) {
                    System.out.println(ANSI_RED + "The user with name: " + name + " is already registered" + ANSI_RESET);
                    startRegister();
                } else {
                    System.out.println(ANSI_RED + "Disconnected from server" + ANSI_RESET);
                }
            } if (resp.toLowerCase().equals("l")) {
                Login login = new Login();
                login.login();
            } else {
                startRegister();
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.close();
    }
    public int registerUser (String login, String password) throws IOException {
       int response = 0;
       String reg = login + "," + password;
        URL url = new URL(Utils.getURL() + "/register?" + reg);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        OutputStream os = http.getOutputStream();
        os.write(reg.getBytes(StandardCharsets.UTF_8));
        os.close();
        response = http.getResponseCode();
       return response;
    }
}
