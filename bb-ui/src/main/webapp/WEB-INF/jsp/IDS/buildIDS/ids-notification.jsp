<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input type="hidden" id="appId" value="${appForm.dbId}" />
<input type="hidden" id="referenceAge" value="${referenceAge}">
<input type="hidden" id="prosecutionStatus" value="${prosecutionStatus}">

<%
	String context = request.getContextPath();
	String css = context + "/assets/css";
%>
<div id="notificationPage">
	<link rel="stylesheet" type="text/css" href="<%=css%>/ids-style.css">

	<div class="main-content container">

		<jsp:include page="notificationCards/idsNotificationHeader.jsp"></jsp:include>
		<div class="panel-group user-management margin-btm10" id="accordion"
			role="tablist" aria-multiselectable="true">
			<jsp:include page="notificationCards/appNotifications.jsp"></jsp:include>
			<jsp:include page="notificationCards/familyNotifications.jsp"></jsp:include>
		</div>
		<!--Accordion ends-->

		<div class="form-horizontal form-footer mtop0">
			<div class="col-sm-12">
				<div class="form-group  text-left">
					<button class="btn btn-submit" type="button" id="notificationButton" data-target="#notificationLink"></button>
					<a class="btn btn-cancel pull-right"
						href="${contextPath}/ids/dashboard">Cancel</a>
				</div>
			</div>
		</div>
	</div>
	<!-- List of users modal -->
	<jsp:include page="../../mdm/dashboard/view-family-modal.jsp"></jsp:include>
	<jsp:include page="../scripts/notification-ids.jsp"></jsp:include>
	<!-- us patent popup -->
	<jsp:include page="notificationCards/ids-confirm-popup.jsp"></jsp:include>
	<jsp:include page="notificationCards/delRefEntryConfirmation.jsp"></jsp:include>
</div>

