<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>SWTCal - Login</title>
	<link rel="stylesheet" type="text/css" href="design.css">
</head>
<body onload="document.loginform.login_name.focus();">
<div class="loginframe">
	<form action="DoCheckLogin" method="post" name="loginform">
	<table class="login">
	<tr>
		<td class="cell_center" colspan="3">
			<img src="./gfx/logo.gif" class="swtcal_logo">
		</td>
	</tr>
	<tr>
		<td class="cell_center" colspan="3">
			<hr class="line_1px"></hr>
		</td>
	</tr>
	<tr>
		<td class="cell_label">
			Name:
		</td>
		<td colspan="2">
			<input type="text" name="login_name">
		</td>
	</tr>
	<tr>
		<td class="cell_label">
			Passwort:
		</td>
		<td colspan="2">
			<input type="password" name="login_pw">
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td>
			<input class="button" type="submit" name="login" value="login">
		</td>
		<td>
			<input class="button" type="reset" value="abbrechen">
		</td>
	</tr>
	</table>
	</form>
</div>
</body>
</html>