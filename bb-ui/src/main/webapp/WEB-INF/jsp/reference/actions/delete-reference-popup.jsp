<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
%>
<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">

<div class="overlay">
<div class="modal-dialog">
<div class="modal-content">
				<div class="modal-header">
				<div class="text-right"> <a class="close" href="#">×</a></div>
				</div>
				<div class="modal-body">
					<div class="content">
						<p class="msg">Are you sure you want to delete this document?</p>
					</div>
					<div class="modal-footer">
					<form:form method="post" action="${pageContext.request.contextPath}/reference/dashboard/deleteDocument" id="deleteReference${refEntry.notificationProcessId}">
					<input type="hidden" name="id" value="${refEntry.notificationProcessId}">					
				</form:form>
                	<button type="button" class="btn btn-cancel" data-dismiss="modal" onclick="document.getElementById('deleteReference${refEntry.notificationProcessId}').submit();">Yes</button>
                	<button type="button" class="btn btn-submit" onclick="dismiss()" >Cancel</button>
                	</div>
        		</div>
        		</div>
        		</div>
</div>
