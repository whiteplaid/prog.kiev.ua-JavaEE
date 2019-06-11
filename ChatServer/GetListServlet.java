package ua.kiev.prog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class GetListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String fromStr = req.getParameter("from");
		String login = req.getParameter("login");
		int from = 0;
		try {
			from = Integer.parseInt(fromStr);
		} catch (Exception ex) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		XmlMessages xml = ClientList.getInstance().getMessages(login).toXML(new AtomicInteger((from)));
        try {
            if (xml != null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(XmlMessages.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                OutputStream os = resp.getOutputStream();
                marshaller.marshal(xml,os);
                os.close();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
	}
}

