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

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        byte[] buf = requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);
        if (ClientList.getInstance().checkClient(bufStr)) {
            if (!ClientList.getInstance().getClient(bufStr).getStatus()) {
                System.out.println("ok");
                ClientList.getInstance().getClient(bufStr).setStatus(true);
                ClientList.getInstance().getClient(bufStr).setRoom("all");
                ClientList.getInstance().getClient(bufStr).setIp(req.getRemoteAddr());
                ClientList.getInstance().getClient(bufStr).setPort(req.getRemotePort());
                response.setStatus(0);
                ClientList.getInstance().addMessage(new ServiceMessage("SERVER", bufStr + " Connected", null, "all"));
            } else {
                response.setStatus(1);
            }
        } else {
            System.out.println("No user");
            response.setStatus(1);
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

        return bos.toByteArray();
    }
}
