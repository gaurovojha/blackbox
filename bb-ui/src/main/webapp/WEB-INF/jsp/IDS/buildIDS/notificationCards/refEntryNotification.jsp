<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<div class="notification-box">
	<a class="remove removeNotification"
		notificationId="${notification.notificationId}"><span
		aria-hidden="true">&times;</span></a>
	<div class="clearfix">
		<h4>Reference Entry Pending</h4>
	</div>
	<div class="form-horizontal clearfix">
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.jurisdiction" /></label>
			<div class="form-control-static">${notification.jurisdiction}</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.application" /></label>
			<div class="form-control-static">${notification.applicationNo}</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.mailingdate" /></label>
			<div class="form-control-static">
				<bbx:date dateFormat="MMM dd, yyyy"
					date="${notification.mailingDate}" />
			</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.docdescription" /></label>
			<div class="form-control-static">
				${notification.docDescription} <a
					href="${contextPath}/correspondence/viewDocument/viewPdf/${notification.correspondenceId}"
					target="_blank"> <i><img src="${pathImg}/attachment.svg"
						" class="icon16x"></i></a>
			</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.ocrstatus" /></label>
			<c:choose>
				<c:when test="${notification.ocrStatus eq 'DONE'}">
					<div class="form-control-static action-success">Done</div>
				</c:when>
				<c:otherwise>
					<div class="form-control-static action-danger">Error</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="footer">
		<div class="inline-block">
			<input type="hidden" class="notificationId"
				value="${notification.notificationId}" />
			<button class="btn btn-cancel deleteRefEntry" type="button"
				data-target="#confirmationBoxDelRefEntry" data-toggle="modal">Delete
				Document</button>
		</div>
		<div class="inline-block">
			<c:choose>
				<c:when test="${notification.ocrStatus eq 'FAILED'}">
					<form:form method="post"
						action="${contextPath}/reference/dashboard/addReferenceForFailed"
						target="_blank" id="addReference${notification.ocrDataId}">
						<input type="hidden" name="id"
							value="${notification.notificationId}">
						<input type="hidden" name="ocrid"
							value="${notification.ocrDataId}">
						<input type="hidden" name="screen" value="IDSNotification">
						<button class="btn btn-submit" target="_blank"
							onclick="document.getElementById('addReference${notification.ocrDataId}').submit();">
							<spring:message code="button.addreferences" />
						</button>
					</form:form>
				</c:when>
				<c:otherwise>
					<form:form method="post"
						action="${contextPath}/reference/dashboard/addReferenceForDone"
						target="_blank" id="addReference${notification.ocrDataId}">
						<input type="hidden" name="id"
							value="${notification.notificationId}">
						<input type="hidden" name="ocrid"
							value="${notification.ocrDataId}">
						<a class="btn btn-submit" target="_blank"
							onclick="document.getElementById('addReference${notification.ocrDataId}').submit();">
							<spring:message code="button.addreferences" />
						</a>
					</form:form>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="pull-right form-control-static">
			<a href="" class="show-more"><spring:message
					code="ids.initiateIDS.idsNotification.more" /></a>
		</div>
	</div>
	<div class="more-notification">
		<p>${notification.status}
			<c:if test="${not empty notification.roles}">
				@
				<c:forEach items="${notification.roles}" var="role" varStatus="loop">
					<c:choose>
						<c:when test="${fn:length(notification.roles) eq 1 }">
							${role}
						</c:when>
						<c:when test="${loop.last}">
							${role}
						</c:when>
						<c:otherwise>
							${role},
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<spring:message code = "ids.initiateIDS.idsNotification.roles"/>
			</c:if>
			<spring:message code = "ids.initiateIDS.idsNotification.since"/>
			<bbx:days date="${notification.notifiedDate}"></bbx:days>
		</p>
		<p>${notification.message}</p>
		<a href="" class="show-less"><spring:message
				code="ids.initiateIDS.idsNotification.less" /></a>
	</div>
</div>