<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/daterangepicker.css">
	<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">
</head>
<body>
	<script type="text/javascript" src="<%=js%>/jquery.min.js"></script>
	<script type="text/javascript" src="<%=js%>/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=js%>/moment.js"></script>
	<script type="text/javascript" src="<%=js%>/daterangepicker.js"></script>
	<script type="text/javascript" src="<%=js%>/multiselect.min.js"></script>
</body>
</html>