<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.activiti.engine.task.Task"%>
<%@page import="com.blackbox.ids.ui.common.Constants"%><%-- 
<%@page import="com.blackbox.ids.core.model.Application"%> --%>
<%-- <%
	Application itemModel = (Application) request.getAttribute(Constants.APPLICATION);
	String descr = itemModel.getDescription() != null ?itemModel.getDescription(): "" ;
	Task task = (Task) request.getAttribute(Constants.TASK);
%> --%>

<html>
	<head>
		<title><%=SecurityContextHolder.getContext().getAuthentication().getName()%></title>
	</head>
	<body>
		<%-- <span> Task Name: <%=task.getName() %></span>
		<form action="<%=request.getContextPath() + "/tasks/" + task.getId()%>" method="post">
			<input type="text" maxlength="250" readonly="readonly" name="<%=Constants.APP_NUMBER %>" value="<%=itemModel.getApn() %>">
			<input type="text" maxlength="250" name="<%=Constants.APP_DESCR %>" value="<%=descr %>">
			<input type="submit" value="Complete Task">
		</form> --%>
	</body>
</html>

