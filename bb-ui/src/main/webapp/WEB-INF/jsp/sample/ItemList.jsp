<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.activiti.engine.task.Task"%>
<%@page import="com.blackbox.ids.ui.common.Constants"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%-- <%@page import="com.blackbox.ids.core.model.Application"%> --%>

<html>
	<head>
		<title><%=SecurityContextHolder.getContext().getAuthentication().getName()%></title>
	</head>
	<body>
		<table border="1">
		<thead>
			<tr>
				<th>Task Name</th>
				<th>Item Code</th>
				<th>Item Name</th>
				<th>Action</th>
			</tr>
		</thead>
		<%-- <%
			List<Task> tasks = (List<Task>) request.getAttribute(Constants.TASK_LIST);
			Map<String, Application> modelMap = (Map<String, Application>) request.getAttribute(Constants.TASKVISE_ITEM_MAP);
			for(Task task: tasks) {
				Application model = modelMap.get(task.getId());
		%> --%>
			<tr>
				<%-- <td>
					<%=task.getName()%>
				</td>
				<td>
					<%=model.getApn() %>
				</td>
				<td>
					<%=model.getDescription() %>
				</td> --%>
				<%-- <td>
					<form action="<%=request.getContextPath() + "/tasks/" + task.getId()%>" method="get">
						<input type="submit" value="Open Task">
					</form>
				</td> --%>
			</tr>
		<%-- <% 	
			}
	
		%> --%>
		</table>
	</body>
</html>

