package ua.kiev.prog;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
public class AddServlet extends HttpServlet {
 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		byte[] buf = requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);
        if (!bufStr.equals("")) {
            Message msg = getXML(bufStr);
            if (msg != null) {
                try {
                    if (msg.checkMessage(msg.getText()) != null) {
                        if (msg.checkMessage(msg.getText()) != "") {
                            System.out.println(msg.getRoom() + " " + msg.getFrom() + " " + msg.getText());
                            ClientList.getInstance().getMessages(msg.getFrom()).add(msg);
                        }
                    } else {
                        resp.setStatus(HttpServletResponse.SC_GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
	}
    private Message getXML (String buf) {
       Message msg = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            msg = (Message) unmarshaller.unmarshal(new StringReader(buf));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return msg;
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
