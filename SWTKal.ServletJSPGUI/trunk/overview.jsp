
<%@ include file="checksession.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="swtkal.server.javapersistence.JPAServer"%>
<html>
<head>
	<title>SWTCal - Wochen&uuml;bersicht</title>
	<link rel="stylesheet" type="text/css" href="design.css">
</head>
<body>
<%@ page import="swtkal.domain.*, servletPackage.*, java.util.*" %>
<%@ page session="true" %>
<%
		//Severobjekt holen
		DoGetServer dgs = new DoGetServer(); 
		//JPAServer jps = new JPAServer
		
		//Bezugsdatum (initial "heutiges" Datum  - s. DoCheckLogin)
		Datum bezug = (Datum)session.getAttribute("bezDatum");
		//Wochentag des Bezugsdatums (Mo=0,...,So=6)
		int wTag = bezug.getWeekDay(); 
		
		//Berechnung des Datums vom Montag der angezeigten KW
		Datum von = new Datum(bezug);
		von.substract(wTag); 
		//Zeit auf 00:00:00
		von.set(GregorianCalendar.HOUR_OF_DAY, 0);
		von.set(GregorianCalendar.MINUTE, 0);
		von.set(GregorianCalendar.SECOND, 0);
		
		
		//Berechnung des Datums vom Sonntag der angezeigten KW
		Datum bis = new Datum(bezug);
		bis.add(6-wTag);
		//Zeit auf 23:59:59
		bis.set(GregorianCalendar.HOUR_OF_DAY, 23);
		bis.set(GregorianCalendar.MINUTE, 59);
		bis.set(GregorianCalendar.SECOND, 59);
		
		//Termin-Vektor (aktuelle Woche) für Benutzer vom Server holen:
		Vector<Termin> termine = dgs.srvobj().getTermineVonBis(von, bis, (Person)session.getAttribute("person"));
		int anz_t = termine.size();
		//out.println("Termine fuer die "+bezug.getWeek()+". Kalenderwoche ("+von.toString()+" - "+bis.toString()+"):<br>");
		//Testausgabe des Terminvektors
		Termin termin_feld[][][]= new Termin[7][24][20];
		// im terminfeld werden Termine der Woche in einer Form gespeichert, die man gut
		// in eine HTML Tabelle schreiben kann. Erste Dimension Wochentag
		// Zweite Dimension Stunden, Dritte Dimension Terminliste Pro Stunde
		boolean tia_flag_feld[][]=new boolean [7][24];
		// Flagfeld für Termine die an den zeitpunkten laufen.
		//boolean tia_flag_begin=false;
		int anz_laufender_tia=0;
		int anz_term[]=new int [7];
		for(int i=0;i<7;i++)
		{
			anz_term[i]=0;
		}
		//Anzahl der termine am jewieligen Tag
		int test=0;
		for(int i=0; termine.size() > i; i++)  //Schleife über Termine der Woche
		{
 			//out.println(termine.elementAt(i).getKurzText()+ "Begin: " + termine.elementAt(i).getBeginn().getDateStr()+ " Stunde:"+ termine.elementAt(i).getBeginn().getHour()+" Ende:"+ termine.elementAt(i).getEnde().getDateStr() + " Stunde:"+ termine.elementAt(i).getEnde().getHour()+"<br>");
			//Hilfsausgabe für simple Terminübersicht.
			for(int wota=0;wota<7;wota++)
			{
				if(termine.elementAt(i).getBeginn().getWeekDay() == wota || termine.elementAt(i).getEnde().getWeekDay() == wota || anz_term[wota]>0)
				//Ist der Anfang oder das Ende eines Termins am Montag in er Liste?
				{	
					for(int std=0;std<24;std++)
					//Schleife über die Stunden
					{
						if(termine.elementAt(i).getBeginn().getHour() == std && termine.elementAt(i).getBeginn().getWeekDay() == wota && termine.elementAt(i).getBeginn().getWeek() == von.getWeek())
						//Ist der Anfang eines Termins am entsprechenden Wochentag zu der aktuellen Stunde in er Liste?
						{
							termin_feld[wota][std][anz_term[wota]]=	termine.elementAt(i);
							//speicher den Termin im Terminfeld an richtiger stelle
							anz_term[wota]++;
							anz_laufender_tia++;
							// Anzahl der termine am Montag werden incrementiert.
						}
						if(termine.elementAt(i).getEnde().getHour() == std && termine.elementAt(i).getEnde().getWeekDay() == wota && termine.elementAt(i).getEnde().getWeek() == von.getWeek())
						{// wenn stunde und Wochentag gleich dem des Endtermins ist...
							if(anz_laufender_tia < 1)
							{
								//Wenn ein endtermin kommt, ohne, dass vorher ein Begin war, ist der Begintermin in der vorherigen woche.
								for(int x=0;x<=wota;x++)
									//schleife über die Wochentage, bis zum aktuellen
									for(int y=0;y<24;y++)
									{
										if((y<=std && x==wota)||x<wota)
											//Am endtag dürfen nur stunden vor der aktuellen geflagt werden
											//an Wochentagen vor dem aktuellen, werden alle 24 stunden geflagt.
											tia_flag_feld[x][y]=true;
									}
								anz_laufender_tia++;
							}
							if(termine.elementAt(i).getBeginn().getHour() != std)
								termin_feld[wota][std][anz_term[wota]]=	termine.elementAt(i);
							//speicher den Termin im Terminfeld an richtiger stelle
							anz_term[wota]++;
							anz_laufender_tia--;
							// Anzahl der termine am Montag werden incrementiert.
							// anz_laufender termine in arbeit wird um eins heruntergesetzt.
						}
						else if(anz_laufender_tia>0)
						{ // bei einemoder mehreren laufenden Terminen TIA flag an entsprechende Position setzen.
							tia_flag_feld[wota][std]=true;
						}
					}
				}
				if(anz_laufender_tia>0 && wota<6 && test<1)
				{
					anz_term[wota+1]=1;
					test++;
				}
			}
		}
%>
<div class="mainframe">
 	<%@ include file="header.jsp" %>
	
	<div class="main">
		<table class="calendarview">
			<tr class="calendardays"> 
			<%-- Überschriftenzeile --%>
				<td></td>				
			<%
			
			String wota_labels[] = {"Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag","Sonntag"};
			
			Datum day_span = von;
			Datum today = new Datum();
			today.set(GregorianCalendar.HOUR_OF_DAY, 0);
			today.set(GregorianCalendar.MINUTE, 0);
			today.set(GregorianCalendar.SECOND, 0);
			
			
			for (int i = 0; i <= 6; i++)
			{
				String today_class = "";				
				
				if ((day_span.getDay() == today.getDay()) && (day_span.getMonth() == today.getMonth()) && (day_span.getYear() == today.getYear()))
				{
					today_class = " class=\"today\"";
				}
				
				out.print("<td"+today_class+">"+day_span.getDay()+"."+day_span.getMonth()+".<br>"+wota_labels[i]+"</td>");
				day_span.add(1);
			}
			
			%>	
			</tr>
			<% 
			//dynamische Erstellung der 24 Stunden Tabelle.
			String field = "";
			Boolean t_i_a = false;
			Boolean t_se = false;
			
			for(int std=0;std<24;std++)
			// Schleife über die Stunden bzw. Zeilen
			{
				out.println(" <tr class=\"calendarhours\"><td class=\"hours\"> " + std + " Uhr </td>");
				//In der ersten spalte steht die Stundenzahl
				for(int wota=0;wota<7;wota++)
				//Schleife über die Wochentage
				{
					field = "";
					t_i_a = false;
					t_se = false;
					
					for(int ter_nr=0;ter_nr<20 ;ter_nr++)
					//Schleife über alle termine am jeweiligen tag
					{
						if(termin_feld[wota][std][ter_nr]!=null)
						//Wenn ein Termin gefunden wurde
						{
							if(termin_feld[wota][std][ter_nr].getBeginn().getHour()== std )
							//Ist die Stunde Beginn des Termins? 
							{
						 		//Link der die "Termin ändern"-Seite mit der entspr. ID aufruft
								String changeLink = "change.jsp?terminID="+termin_feld[wota][std][ter_nr].getId();
						 		
						 		field += "<a href=\""+changeLink+"\"> B "+termin_feld[wota][std][ter_nr].getBeginn().getHour()+":"+termin_feld[wota][std][ter_nr].getBeginn().getMin()+": "+termin_feld[wota][std][ter_nr].getKurzText()+"<br></a>";
						 		t_se = true;
							}
						 	else if(termin_feld[wota][std][ter_nr].getEnde().getHour()== std )
						 	// Ist die Stunde Ende des Termins?
						 	{
						 		field += "E "+termin_feld[wota][std][ter_nr].getEnde().getHour()+":"+termin_feld[wota][std][ter_nr].getEnde().getMin()+": " + termin_feld[wota][std][ter_nr].getKurzText() + "<br>";
						 		t_se = true;
						 		// Ende eines Termins ausgeben
						 	}
						}
						else if(tia_flag_feld[wota][std] && ter_nr==19)
						{
						// wenn termin in bearbeitung und nichts anderes im feld steht, tia melden.
							//field += "t_i_a";
							t_i_a = true;
						}
						
					}
					
					if (t_se) {
						out.println("<td class=\"tse\">"); }
					else if (t_i_a) {
						out.println("<td class=\"tia\">"); }
					else {
						out.println("<td>"); }
					
					out.println(field);
					
					out.println("</td>");
					//letzte zelle beenden und neue beginnen.
				}
			out.println("</tr>");
			}
			%>
			
		</table>
	</div>
</div>
</body>
</html>