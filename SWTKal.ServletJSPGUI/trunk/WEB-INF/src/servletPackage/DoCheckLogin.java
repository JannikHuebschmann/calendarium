package servletPackage;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import swtkal.domain.Datum;
import swtkal.domain.Person;
import swtkal.exceptions.PersonException;

public class DoCheckLogin extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {	
			
			HttpSession session = request.getSession(false);
			
			// SWTKal Serverobjekt holen			
			DoGetServer dgs = new DoGetServer();			
				
			// Weiterleitungsziele definieren
			String redir_ok = "overview.jsp";
			String redir_bad = "index.jsp";
			
			// Sessionobjekt holen
			
			// Falls logout
			if (request.getParameter("logout") != null)
			{
				// Session wegwerfen
				if (session != null)
				{
					session.removeAttribute("person");
					session.removeAttribute("loggedin");
					session.invalidate();
				}
				
				// Zurück zum Login
				response.sendRedirect(redir_bad);
			}
			else
			{				
				// Übergabeparameter für Login
				String krz = request.getParameter("login_name");
				String pw = request.getParameter("login_pw");				
				
				try {
					// Wenn es eine Person zum Kürzel gibt
					Person p = dgs.srvobj().authenticatePerson(krz, pw);
					//Bezugsdatum (Heute)
					Datum bezug = new Datum(new Date());
					
					session = request.getSession(true);
	
					// Session fertig machen
					session.setAttribute("person", p);
					session.setAttribute("loggedin", "1");
					session.setAttribute("bezDatum", bezug);
					
					// Zur Übersicht weiterleiten
					response.sendRedirect(redir_ok);
				} catch (PersonException pe) {
					
					// Falls keine Person, Session wieder weg
					session.invalidate();					
					
					// Zurück zur Loginseite
					response.sendRedirect(redir_bad);
					}
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
