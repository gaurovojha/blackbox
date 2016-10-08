<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%
	String context = request.getContextPath();
	String css = context+"/assets/css";
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>
<head>
    <link rel="stylesheet" type="text/css" href="<%=css%>/reference/reference_main.css">
</head>

<c:choose >
	<c:when test="${fromDashboard}">
		<c:set var="activeRefDashboard" value="active"/>
		<c:set var="activeRefMgmt" value=""/>
		<c:set var="activeRefFlowrule" value=""/>
	</c:when>
	<c:when test="${fromRefMgmt}">
		<c:set var="activeRefDashboard" value=""/>
		<c:set var="activeRefMgmt" value="active"/>
		<c:set var="activeRefFlowrule" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="activeRefDashboard" value=""/>
		<c:set var="activeRefMgmt" value=""/>
		<c:set var="activeRefFlowrule" value="active"/>
	</c:otherwise>
</c:choose>

<nav class="main-nav">
	<div class="container">
		<ul>
			<sec:authorize access="canAccessUrl('/reference/dashboard')">
				<li class="${activeRefDashboard}"><a href="${pageContext.request.contextPath}/reference/dashboard">My Dashboard </a>
					<p class="shortcut-pic">Shift+D</p>
				</li>
			</sec:authorize>
			<sec:authorize access="canAccessUrl('/reference/management')" var="accessNotification">
				<li  class="${activeRefMgmt}"><a href="${pageContext.request.contextPath}/reference/management">Reference Management </a>
					<p class="shortcut-pic">Shift+A</p>
				</li>
			</sec:authorize>
			<%-- <sec:authorize access="canAccessUrl('/reference/flowRule')"> --%>
				<li class="${activeRefFlowrule}"><a href="${pageContext.request.contextPath}/reference/flowRule">Reference Flow Rules </a><sup>#</sup>
					<p class="shortcut-pic">Shift+R</p>
				</li>
			<%-- </sec:authorize> --%>
			<!-- <li class="last">Reference Management</li> -->
		</ul>
	</div>
</nav>