package servletPackage;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import swtkal.domain.Datum;

public class DoCurWeek extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {	
			
			HttpSession session = request.getSession(true);				
				
			//Bezugsdatum ändern (aktuelle Woche)
			Datum bezug = new Datum(new Date());
			session.setAttribute("bezDatum", bezug);
					
			// Zur "neuen" Übersicht weiterleiten
			response.sendRedirect("overview.jsp");
			} catch (Exception ex) {
			ex.printStackTrace();}
		}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
