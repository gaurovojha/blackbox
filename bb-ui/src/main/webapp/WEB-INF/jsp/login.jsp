<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
.message {
    color: green;
    font-style: italic;
    font-weight: bold;
}
.error {
    color: #ff0000;
    font-style: italic;
    font-weight: bold;
}
</style>
	<title>Login - Blackbox</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap-datepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/daterangepicker.css">
</head>
<body>

	<!--login-->
	<div class="login-container">
		<h2>Sign In</h2>
		<div class="login-logo">
			<img src="<%=images%>/logo_ids.gif" alt="IDS logo">
			<div>BlackBox</div>
		</div>
		<div id="login-error" class="error">${error}</div>

	<form
		action="j_spring_security_check" method="post">
		<div class="form-horizontal">
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label">Username</label>
					<input id="j_username" type="text" class="form-control" name="username" value="${username}"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label">Password</label>
					<input id="j_password" name="password" type="password" class="form-control"/>
				</div>
			</div>
			<div class="form-group footer-login">
				<div class="col-sm-12">
					<button class="btn btn-submit col-xs-12">Login</button>
					<c:if test="${not ldapFlag}">
					<a href="forgotPassword" class="forgot-pass-link">Forgot Password?</a>
					</c:if>
				</div>
			</div>
		</div>
		</form>
	</div>

	<script type="text/javascript" src="<%=js%>/jquery.min.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=js%>/moment.js"></script>
	<script type="text/javascript" src="<%=js%>/daterangepicker.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap-datepicker.js"></script>

</body>
</html>