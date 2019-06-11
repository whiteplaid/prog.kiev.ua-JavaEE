package ua.kiev.prog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            byte[] buf = requestBodyToArray(request);
            String bufStr = new String(buf, StandardCharsets.UTF_8);
            if (bufStr == null) {
                response.setStatus(1);
            } else {
                String[] output = bufStr.split(",");
                String login = output[0];
                String pass = output[1];
                String ip = request.getRemoteAddr();
                int port = request.getRemotePort();
                System.out.println("login: " + login + ", password: " + pass + " ip: " + ip + ":" + port);

                if (!ClientList.getInstance().checkClient(login)) {
                    Client client = new Client(login, pass);
                    client.setStatus(true);
                    client.setIp(ip);
                    client.setPort(port);
                    ClientList.getInstance().addClient(client);
                    response.setStatus(0);
                    if (!ChatRoomList.getInstance().checkRoom("all")) {
                        ChatRoom chatRoom = new ChatRoom("all");
                        ClientList.getInstance().getClient(login).setRoom("all");
                        ChatRoomList.getInstance().addRoom(chatRoom);
                    } else {
                        ClientList.getInstance().getClient(login).setRoom("all");
                    }
                    ClientList.getInstance().add(login);
                    ClientList.getInstance().addMessage(new ServiceMessage("SERVER",login + " Connected",null,"all"));
                } else {
                    response.setStatus(2);
                    return;
                }
            }
    }

    private byte[] requestBodyToArray(HttpServletRequest req) throws IOException {
        InputStream is = req.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);
        is.close();
        return bos.toByteArray();
    }
}
