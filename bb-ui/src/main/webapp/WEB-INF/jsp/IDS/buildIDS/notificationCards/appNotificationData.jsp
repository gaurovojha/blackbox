<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input id="currentPage" type="hidden" value="${currentPageApp}">
<input id="noOfPages" type="hidden" value="${noOfPagesApp}">
<input type="hidden" id="totalAppRecords" value="${appNotificationsList.recordsTotal}" />

<div class="slide-count">
	<a class="Url-referesh" href="javascript:void(0)"
		class="refreshNotification"><span
		class="glyphicon glyphicon-refresh refreshNotification"></span> </a>
	${currentPageApp} of ${noOfPagesApp}
</div>
<!-- Wrapper for slides -->
<div class="carousel-inner" role="listbox">
	<div class="item active">
		<c:forEach items="${appNotificationsList.items}"
			var="pendingNotification">
			<c:set var="notification" value="${pendingNotification}"
				scope="request" />
			<c:choose>
				<c:when test="${pendingNotification.notificationName eq 'NPL_DUPLICATE_CHECK'}">
					<jsp:include page="nplDuplicateNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'UPDATE_FAMILY_LINKAGE'}">
					<jsp:include page="updateFamilyNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'DASHBOARD_ACTION_STATUS'}">
					<jsp:include page="statusChangeNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'REFERENCE_MANUAL_ENTRY'}">
					<jsp:include page="refEntryNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'VALIDATE_REFERENCE_STATUS'}">
					<jsp:include page="validateRefStatusNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'UPLOAD_MANUALLY_FILED_IDS'}">
					<jsp:include page="uploadIDSNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'INPADOC_FAILED'}">
					<jsp:include page="updateRefNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'CREATE_SML'}">
					<jsp:include page="createSMLNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE'}">
					<jsp:include page="uploadDocNotification.jsp"></jsp:include>
				</c:when>
				<c:when test="${pendingNotification.notificationName eq 'N1449'}">
					<jsp:include page="1449UpdateNotification.jsp"></jsp:include>
				</c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</c:forEach>
	</div>
</div>