<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>

<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
	String js = context+"/assets/js";
	String images = context+"/assets/images";
	String jsPlugins= context+"/assets/js/plugin";
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
%>

<html>
	<head>
		<%-- <title><spring:message code="title.blackbox" /></title> --%>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">	
		<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="<%=css%>/jquery-ui.css"/>	
		<link rel="stylesheet" type="text/css" href="<%=css%>/daterangepicker.css">
		<link rel="stylesheet" type="text/css" href="<%=css%>/datatables.min.css">
		<link rel="stylesheet" type="text/css" href="<%=css%>/main.css">
		<link rel="stylesheet" type="text/css" href="<%=css%>/dev.css">
		<link rel="stylesheet" type="text/css" href="<%=css%>/bootstrap-datepicker.css"/>
		
		
		<title>
			<tiles:insertAttribute name="title" ignore="true"/>
		</title>
	</head>
	<body oncontextmenu='return true'>
	
		<header>
			<script type="text/javascript" src="<%=jsPlugins%>/jquery.min.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/jquery.dataTables.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/multiselect.min.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/jquery-ui.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/jquery.form.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/bootstrap.min.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/moment.js"></script>	
			<script type="text/javascript" src="<%=jsPlugins%>/bootstrap-datepicker.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/daterangepicker.js"></script>
			<script type="text/javascript" src="<%=jsPlugins%>/bootstrap-select.js"></script>
		<script>
			jQuery(document).ready(function($) {
				//browserSec();
			});
		</script>


	</header>
		<div>
			<div id='progressBar' style='display:none' disableLoader="false" class="loader">
			<div>
       			<img src='<%=images%>/page-loader.gif'/>
       		</div>
			</div>
			<tiles:insertAttribute name="header" ignore="true"/>
		</div>  
        <div>
        	<tiles:insertAttribute name="navigation" ignore="true"/>
        </div>  
        <div>
        	<tiles:importAttribute name="activeTab" scope="request" />
        	<input id="activeTab" type="hidden" value="${activeTab}"/>
        	<tiles:insertAttribute name="body" />
        </div>  
        <div>
        	<tiles:insertAttribute name="footer" ignore="true"/>
        </div>  
	</body>
</html>