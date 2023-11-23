package servletPackage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import swtkal.domain.Datum;
import swtkal.domain.Person;
import swtkal.domain.Termin;
import swtkal.exceptions.TerminException;

public class DoCreateEvent extends HttpServlet {

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
				//Beschreibung auslesen
				String beschr = request.getParameter("beschr");
				
				//Datumsobjekt für Startdatum anlegen
				String vdd = request.getParameter("von_tag");
				String vMM = request.getParameter("von_mon");
				String vyy = request.getParameter("von_jahr");
				String vHH = request.getParameter("von_std");
				String vmm = request.getParameter("von_min");
				Datum von = new Datum(vdd+"."+vMM+"."+vyy+" "+vHH+":"+vmm);
				
				//Datumsobjekt für Enddatum anlegen
				String bdd = request.getParameter("bis_tag");
				String bMM = request.getParameter("bis_mon");
				String byy = request.getParameter("bis_jahr");
				String bHH = request.getParameter("bis_std");
				String bmm = request.getParameter("bis_min");
				Datum bis = new Datum(bdd+"."+bMM+"."+byy+" "+bHH+":"+bmm);
			
				//Termin erstellen und aktuellem Benuter zuweisen
				Termin t = new Termin((Person)session.getAttribute("person"), beschr, "Testtermin", von, bis);
				
				try {
				//Termin in das Server-Objekt einpflegen
				serv.srvobj().insert(t);
				session.setAttribute("bezDatum", von.clone());
				} catch (TerminException te) {
				}
				
				//Weiterleitung zur Wochenübersicht
				response.sendRedirect("overview.jsp");
			}
			else
			{
				//Wenn nicht eingeloggt, Weiterleitung zur Anmeldemaske
				response.sendRedirect("index.jsp");
			}
						
		} catch (Exception ex) {
			ex.printStackTrace();
			
			//Bei fehlender Eingabe soll nichts passieren
			response.sendRedirect("create.jsp");
		}
		}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
