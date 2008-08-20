package servletPackage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DoDeleteEvent extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//Serverobjekt holen
			DoGetServer serv = new DoGetServer();
			
			//Sessionobjekt holen
			HttpSession session = request.getSession(true);
			
			if(session.getAttribute("loggedin")=="1")
			{
				//Termin ID aus session-Objekt auslesen; Termin löschen
				int tid = Integer.parseInt(request.getParameter("terminID"));
				serv.srvobj().delete(serv.srvobj().getTermin(tid));
				//Weiterleitung zur Wöchenübersicht
				response.sendRedirect("overview.jsp");
			}
			else
			{
				//Wenn nicht eingeloggt, Weiterleitung zur Anmeldemaske
				response.sendRedirect("index.jsp");
			}
				
						
		} catch (Exception ex) {
			ex.printStackTrace();}
		}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
