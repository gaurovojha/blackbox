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
.error {
    color: #ff0000;
    font-style: italic;
    font-weight: bold;
}
</style>
	<title>Blackbox</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap-datepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/daterangepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">
</head>
<body>

	<!--login-->
	<div class="login-container">
		<h2>Change Password</h2>
		<div class="login-logo">
			<img src="<%=images%>/logo_ids.gif" alt="IDS logo">
			<div>BlackBox</div>
		</div>
		<form:form class="form-horizontal" method="post"
				modelAttribute="changePasswordForm" action="changePassword">
		<div class="form-horizontal">
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label"><spring:message code="changepassword.oldpassword" /></label>
					<form:input type="password" class="form-control" path="password" ></form:input>
					<form:errors cssClass="error" path="password"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label"><spring:message code="changepassword.newpassword" /></label>
					<form:input id="newPassword" type="password" class="form-control" path="newPassword" ></form:input>
					<form:errors cssClass="error" path="newPassword" ></form:errors>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="control-label"><spring:message code="changepassword.confirmpassword" /></label>
					<form:input type="password" class="form-control"
							path="confirmPassword" ></form:input>
					<form:errors cssClass="error" path="confirmPassword"></form:errors>
				</div>
			</div>
			<div class="form-group footer-login">
				<div class="col-sm-12">
					<form:button class="btn btn-submit col-xs-12" name="changePassword">Submit</form:button>
				</div>
			</div>
		</div>
		</form:form>
	</div>

	<script type="text/javascript" src="<%=js%>/jquery.min.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=js%>/moment.js"></script>
	<script type="text/javascript" src="<%=js%>/daterangepicker.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap-datepicker.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			    $('input[type=="password"]')
			         .val('')
			         .blur();
			}); 
	</script>

</body>
</html>