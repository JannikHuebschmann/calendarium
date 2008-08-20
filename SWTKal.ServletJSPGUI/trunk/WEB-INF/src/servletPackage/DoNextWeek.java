package servletPackage;

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import swtkal.domain.Datum;

public class DoNextWeek extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {	
			
			HttpSession session = request.getSession(true);				
				
			//Bezugsdatum �ndern (eine Woche weiter)
			Datum bezug = (Datum)session.getAttribute("bezDatum");
			bezug.add(GregorianCalendar.DAY_OF_MONTH, 7);
			session.setAttribute("bezDatum", bezug);
					
			// Zur "neuen" �bersicht weiterleiten
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
