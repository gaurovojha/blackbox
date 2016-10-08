<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<!DOCTYPE html>
<html>
<head>
<style>
.action-message {
    color: green;
    font-style: italic;
    font-weight: bold;
    display: inline-block;
}
.error {
    color: #ff0000;
    font-style: italic;
    font-weight: bold;
}
</style>
	<title><spring:message code="login.blackbox" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap-datepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/daterangepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">
</head>
<body>

	<!--login-->
	<div class="login-container">
		<h2><spring:message code="forgotPassword.page.heading" /></h2>
		<div class="login-logo">
			<img src="<%=images%>/logo_ids.gif" alt="IDS logo">
			<div><spring:message code="title.blackbox" /></div>
		</div>
		<div class="form-horizontal">
		<form:form class="form-horizontal" method="post"
				modelAttribute="forgotPasswordForm" action="forgotPassword">
		<div id="forgot-message" class="action-message"><em>${message}</em></div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label"><spring:message code="username" /></label>
					<form:input type="text" class="form-control" path="userId" ></form:input>
					<form:errors cssClass="error" path="userId"></form:errors>
				</div>
			</div>
			<div class="form-group footer-login">
				<div class="col-sm-12">
					<form:button class="btn btn-submit col-xs-12"><spring:message code="button.confirm" /></form:button>
				</div>
			</div>
			</form:form>
			<div class="form-group">
				<div class="col-sm-12">
					<button class="btn btn-cancel col-xs-12" onclick="window.location.href = '<%=context%>/login';"><spring:message code="button.cancel" /></button>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="<%=js%>/jquery.min.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=js%>/moment.js"></script>
	<script type="text/javascript" src="<%=js%>/daterangepicker.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap-datepicker.js"></script>

</body>
</html>