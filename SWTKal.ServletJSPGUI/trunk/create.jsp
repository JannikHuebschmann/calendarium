<%@ include file="checksession.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>SWTCal - Termin erstellen</title>
	<link rel="stylesheet" type="text/css" href="design.css">
</head>
<body>
<div class="mainframe">
 	<%@ include file="header.jsp" %>
	<div class="bottom">
		<form name="createform" action="DoCreateEvent" method="post" onsubmit="return createformpruefen(0)" >
			<div class="formframe">
			<table class="create">
				<tr>
					<td class="cell_label">
						Beschreibung:
					</td>
					<td>
						<textarea name="beschr" cols="30" rows="2"></textarea>
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
						<input name="von_tag" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
						<input name="von_mon" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
						<input name="von_jahr" type="text" size="4" maxlength="4" onchange="createerrechnedauer()">
					</td>
				</tr>
				<tr>
					<td class="cell_label">
						Uhrzeit:
					</td>
					<td>
						<input name="von_std" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
						<input name="von_min" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
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
						<input name="bis_tag" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
						<input name="bis_mon" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
						<input name="bis_jahr" type="text" size="4" maxlength="4" onchange="createerrechnedauer()">
					</td>
				</tr>
				<tr>
					<td class="cell_label">
						Uhrzeit:
					</td>
					<td>
						<input name="bis_std" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
						<input name="bis_min" type="text" size="2" maxlength="2" onchange="createerrechnedauer()">
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
					<td >
						<input name="dauer_tage" type="text" size="3" maxlength="3" onchange="createerrechneende()">&nbsp;Tage&nbsp;
						<input name="dauer_std" type="text" size="2" maxlength="2" onchange="createerrechneende()">&nbsp;Std.&nbsp;
						<input name="dauer_min" type="text" size="2" maxlength="2" onchange="createerrechneende()">&nbsp;Min.&nbsp;
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input type="submit" name="create" value="eintragen">
						<input type="reset" value="abbrechen">
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