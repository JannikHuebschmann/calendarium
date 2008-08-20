<%@ include file="checksession.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="swtkal.server.javapersistence.JPAServer"%>
<html>
<head>
	<title>SWTCal - Termin ändern</title>
	<link rel="stylesheet" type="text/css" href="design.css">
</head>
<body onload="changeerrechnedauer()">
<%@ page import="swtkal.domain.*, servletPackage.*, java.util.*, swtkal.server.Server" %>
<%
	//JPAServer jps = new JPAServer();
	DoGetServer dgs = new DoGetServer();
	//zu ändernden Termin aus dem session-Objekt holen
	Termin t = dgs.srvobj().getTermin(Integer.parseInt(request.getParameter("terminID")));
%>
<div class="mainframe">
 	<%@ include file="header.jsp" %>
	<div class="bottom">
		<form name="changeform" action="" method="post" onsubmit="return changeformpruefen(0)">
			<div class="formframe">
			<table class="create">
				<tr>
					<td class="cell_label">
						Beschreibung:
					</td>
					<td>
						<textarea name="beschr" cols="30" rows="2"><%= t.getKurzText() %></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<hr class="line_1px"></hr>
					</td>
				</tr>
				<tr>
					<td class="headline" colspan="2">
						Beginn:
					</td>
				</tr>
				<tr>
					<td class="cell_label">
						Datum:
					</td>
					<td>
						<input name="von_tag" type="text" size="2" maxlength="2" value=<%= t.getBeginn().getDay() %> onchange="changeerrechnedauer()">
						<input name="von_mon" type="text" size="2" maxlength="2" value=<%= t.getBeginn().getMonth() %> onchange="changeerrechnedauer()">
						<input name="von_jahr" type="text" size="4" maxlength="4" value=<%= t.getBeginn().getYear() %> onchange="changeerrechnedauer()">
					</td>
				</tr>
				<tr>
					<td class="cell_label">
						Uhrzeit:
					</td>
					<td>
						<input name="von_std" type="text" size="2" maxlength="2" value=<%= t.getBeginn().getHour() %> onchange="changeerrechnedauer()">
						<input name="von_min" type="text" size="2" maxlength="2" value=<%= t.getBeginn().getMin() %> onchange="changeerrechnedauer()">
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<hr class="line_1px"></hr>
					</td>
				</tr>
				<tr>
					<td class="headline" colspan="2">
						Ende:
					</td>
				</tr>
				<tr>
					<td class="cell_label">
						Datum:
					</td>
					<td>
						<input name="bis_tag" type="text" size="2" maxlength="2" value=<%= t.getEnde().getDay() %> onchange="changeerrechnedauer()">
						<input name="bis_mon" type="text" size="2" maxlength="2" value=<%= t.getEnde().getMonth() %> onchange="changeerrechnedauer()">
						<input name="bis_jahr" type="text" size="4" maxlength="4" value=<%= t.getEnde().getYear() %> onchange="changeerrechnedauer()">
					</td>
				</tr>
				<tr>
					<td class="cell_label">
						Uhrzeit:
					</td>
					<td>
						<input name="bis_std" type="text" size="2" maxlength="2" value=<%= t.getEnde().getHour() %> onchange="changeerrechnedauer()">
						<input name="bis_min" type="text" size="2" maxlength="2" value=<%= t.getEnde().getMin() %> onchange="changeerrechnedauer()">
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<hr class="line_1px"></hr>
					</td>
				</tr>
				<tr>
					<td class="cell_label">
						Dauer:
					</td>
					<td>
						<input name="dauer_tage" type="text" size="3" maxlength="3"  value="" onchange="changeerrechneende()">&nbsp;Tage&nbsp;
						<input name="dauer_std" type="text" size="2" maxlength="2" value="" onchange="changeerrechneende()" >&nbsp;Std.&nbsp;
						<input name="dauer_min" type="text" size="2" maxlength="2" value="" onchange="changeerrechneende()" >&nbsp;Min.&nbsp;
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input name="terminID" type="hidden" value=<%= request.getParameter("terminID") %>>
						<input type="submit" name="create" value="&auml;ndern" onclick="senden(0)">
						<input type="submit" name="delete" value="l&ouml;schen" onclick="senden(1)">
						<input type="submit" value="abbrechen" onclick="senden(2)">
					</td>
				</tr>
			</table>
			</div>
		</form>
	</div>
</div>
<script type="application/javascript" src="functions.js"></script>
</body>
</html>