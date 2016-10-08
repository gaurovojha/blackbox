<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.blackbox.ids.ui.common.Constants"%>
<html>
	<head>
		<title><%=SecurityContextHolder.getContext().getAuthentication().getName()%></title>
	</head>
	<body>
		<form action="<%=request.getContextPath() + "/items/new"%>" method="post">
			<input type="text" maxlength="250" name="<%=Constants.APP_NUMBER %>">
			<input type="text" maxlength="250" name="<%=Constants.APP_DESCR %>">
			<input type="submit" value="create">
		</form>
	</body>
</html>

