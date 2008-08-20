<% 
		if(session.getAttribute("loggedin")==null)
		{
%>
<jsp:forward page="index.jsp"/>
<% 
		}
%>