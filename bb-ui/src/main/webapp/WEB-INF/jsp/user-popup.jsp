<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String js = context + "/assets/js";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="<%=js%>/admin/admin.js"></script>
</head>
<body>

	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="userModalLabel"><spring:message code="admin.page.user.popup.title" /></h4>
			</div>
			<div class="modal-body">
				<div class="clearfix">
					<div id="userListContainer">
						<table class="table custom-table">
							<thead>
								<tr>
									<th><spring:message
											code="admin.page.user.popup.header.name" /></th>
									<th><spring:message
											code="admin.page.user.popup.header.email" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${userDetails}" var="user">
									<tr class="odd">
										<td>${user.userName}</td>
										<td>${user.emailId}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer">					
				<button type="button" class="btn btn-submit" id="userDetails" data-dismiss="modal">
					<spring:message code="button.close" />
				</button>
			</div>
		</div>
	</div>
	
</body>
</html>