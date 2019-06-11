package com.ua.kiev.prog;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetAuth{
private String login;

    public GetAuth(String login) {
        this.login = login;
    }


    public Boolean auth () {
        Boolean response = false;
        try {
            URL url = new URL(Utils.getURL() + "/login?" + login);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            OutputStream os = http.getOutputStream();
            os.write(login.getBytes(StandardCharsets.UTF_8));
            if (http.getResponseCode() == 0) {
                response = true;
            } else {
                response = false;
            }
            os.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    public Boolean checkPass (String login,String password) {
        Boolean response = false;
        try {
            String auth = login + "," + password;
            URL url = new URL(Utils.getURL() + "/password?" +auth);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            OutputStream os = http.getOutputStream();
            os.write(auth.getBytes(StandardCharsets.UTF_8));
            if (http.getResponseCode() == 0) {
                response = true;
            } else {
                response = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
