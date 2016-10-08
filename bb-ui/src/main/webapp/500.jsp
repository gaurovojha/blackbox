<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String context = request.getContextPath();
	String images = context+"/assets/images";
%>
<html>
<head>
    <title>Blackbox</title>
</head>
<body>
  <h1>Internal Server Error</h1>

  <img src="<%=images%>/logo_site.gif" alt="IDS logo">

</body>
</html>
