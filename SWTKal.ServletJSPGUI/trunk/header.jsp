<%@ page import = "swtkal.domain.*, java.util.*" %>	
<%
	Person p = (Person)session.getAttribute("person");
	Datum d = (Datum)session.getAttribute("bezDatum");
	Datum heute = new Datum(new Date());
	String v = "Wochen&uuml;bersicht";
	boolean plusminus = false;

	v = request.getRequestURI();
	String[] results = v.split("/");	
	v = results[results.length-1];

	if (v.equals("overview.jsp")) {
        v = heute.getDateStr()+"<br>Wochen&uuml;bersicht ( KW"+d.getWeek()+ ")";
        plusminus = true;}
	else if (v.equals("change.jsp")) {
        v = "Termin &auml;ndern"; }
	else if (v.equals("create.jsp")) {
        v = "Termin erstellen"; }
	
	if (p == null) {
	p =  new Person("","",""); }

%>
	<div class="top">
		<table class="header">
			<tr>
				<td>
					<img src="./gfx/logo.gif" class="swtcal_logo">
				</td>
				<td id="currentsite">
					<%= v %>
				</td>
				<td></td>
				<td>
					<%= p.getName() %> ( Nutzer ) 
				</td>
			</tr>
			<td colspan="4">
					<hr class="line_1px"></hr>
				</td>
			</tr>
		</table>
		<table class="navigationbar">
			<tr >
				<td>
					<% if(plusminus == true){ %>
						<a href="DoPrevWeek" onMouseOver="change0.src='./gfx/link_prev_mover.gif';" onMouseOut="change0.src='./gfx/link_prev.gif';"><img name="change0" border="0" src="./gfx/link_prev.gif"></a>
					<%}
						else { %>
						<img src="./gfx/fill.gif">
					<%} %>
				</td>
				<td>					
					<a href="DoCurWeek" onMouseOver="change1.src='./gfx/link_overview_mover.gif';" onMouseOut="change1.src='./gfx/link_overview.gif';"><img name="change1" border="0" src="./gfx/link_overview.gif"></a>
				</td>
				<td>
					<a href="create.jsp" onMouseOver="change2.src='./gfx/link_create_mover.gif';" onMouseOut="change2.src='./gfx/link_create.gif';"><img name="change2" border="0" src="./gfx/link_create.gif"></a>
				</td>
				<td>
					<a href="DoCheckLogin?logout=1" onMouseOver="change3.src='./gfx/link_logout_mover.gif';" onMouseOut="change3.src='./gfx/link_logout.gif';"><img name="change3" border="0" src="./gfx/link_logout.gif"></a>
				</td>
				<td>
					<% if(plusminus == true){ %>
						<a href="DoNextWeek" onMouseOver="change4.src='./gfx/link_next_mover.gif';" onMouseOut="change4.src='./gfx/link_next.gif';"><img name="change4" border="0" src="./gfx/link_next.gif"></a>
					<%}
						else { %>
						<img src="./gfx/fill.gif">
					<%} %>
				</td>
		</table>
	</div>