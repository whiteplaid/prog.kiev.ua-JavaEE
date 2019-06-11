package com.ua.kiev.prog;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
	public static void main(String[] args) throws IOException, JAXBException {
		Boolean response = false;
		while (!response) {
			try {
				URL url = new URL((Utils.getURL()));
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				if (http.getResponseCode() == 200) {
					response = true;
				}
 			} catch (Exception e) {
				System.out.println("No connection with remote server");
			}
		}
		Login login = new Login();
		login.login();
	}
}
