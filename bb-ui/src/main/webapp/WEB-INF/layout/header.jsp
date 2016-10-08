<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>

<%
	String context = request.getContextPath();
	String css = context + "/assets/css";
	String js = context + "/assets/js";
	String images = context + "/assets/images";
%>

<header class="header">
	<title><spring:message code="title.blackbox" /></title> <input
		id="contextPath" type="hidden"
		value="${pageContext.request.contextPath}" />
	<c:set var="contextPath" value="${pageContext.request.contextPath}"
		scope="session" />
	<c:set var="imgPath" value="${contextPath}/assets/images"
		scope="session" />

	<div class="container">
		<a href="javascript:void(0)" class="logo"> <img
			src="<%=images%>/logo_site.gif" alt="Torry | Morris">
		</a>
		<div class="header-right">
			<div class="dropdown user-login">
				<button class="btn btn-default dropdown-toggle" type="button"
					id="userdropdown" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="true">
					${sessionScope.userFullName}
					<span class="caret"></span>
				</button>
				<ul class="dropdown-menu" aria-labelledby="userdropdown">
					<li><a id="logoutLink" href="javascript:void(0);"
						onclick="showLogoutPopup()">Log Out</a></li>
				</ul>
			</div>
			<img src="<%=images%>/logo_ids.gif" alt="IDS">
			<div id="logoutPopup" class="popup-msg">
				<div class="text-right">
					<a class="close" href="#" onclick="hideLogoutPopup()">&times;</a>
				</div>
				<div class="content">
					<span id="uploadMsgWarning" class="msg"></span>
					<p class="msg">Do you want to logout?</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal"
						onclick="sessLogOut();hideLogoutPopup();">OK</button>
					<button type="button" class="btn btn-submit"
						onclick="hideLogoutPopup();">Cancel</button>
				</div>
			</div>
			<div id="timeoutPopup" class="popup-msg">
				<div class="text-right">
					<a class="close" href="#" onclick="hideTimeoutPopup()">&times;</a>
				</div>
				<div class="content">
					<p class="msg">Your Session will be expired in 30 seconds. Do
						you want to continue your session?</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal"
						onclick="continueSession();hideTimeoutPopup();">OK</button>
					<button type="button" class="btn btn-submit"
						onclick="sessLogOut();hideTimeoutPopup();">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<%-- <div id="action-error" class="action-error">${error}</div> --%>
</header>
<script type="text/javascript">
	var logoutUrl = '${pageContext.request.contextPath}/logout';
</script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/assets/js/common/namespace.js"></script>
<script type="text/javascript" src="<%=js%>/common.js"></script>
<script type="text/javascript" src="<%=js%>/browserSecurity.js"></script>
<script type="text/javascript"
	src="<%=js%>/plugin/jquery.i18n.properties.js"></script>
<script type="text/javascript" src="<%=js%>/common/util/common-util.js"></script>