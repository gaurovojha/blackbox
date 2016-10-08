<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<div class="notification-box">
	<a class="remove removeNotification"
		notificationId="${notification.notificationId}"><span
		aria-hidden="true">&times;</span></a>
	<div class="clearfix">
		<h4>
			<spring:message
				code="ids.initiateIDS.idsNotification.statuschangerequest" />
		</h4>
	</div>
	<div class="form-horizontal clearfix">
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.familyId" /></label>
			<div class="form-control-static">
				<a href="" class="familyId" data="${notification.familyId}"
					data-toggle="modal" data-target="#viewFamily">${notification.familyId}</a>
			</div>
		</div>
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
					code="ids.initiateIDS.idsNotification.currentstatus" /></label>
			<div class="form-control-static action-success">${notification.currentStatus}</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.requestfor" /></label>
			<div class="form-control-static action-danger">${notification.requestedFor}</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.requestedby" /></label>
			<div class="form-control-static">${notification.requestedBy}
				<div class="pos-relative">
					<bbx:date dateFormat="MMM dd, yyyy"
						date="${notification.requestedOn}" />
					<c:if test="${not empty notification.userComment}">
						<a href="javascript:void(0)" data-toggle="tooltip"
							data-placement="top" title="${notification.userComment}"><i><img
								src="${pathImg}/comment.svg" class="icon20x pull-right"></i></a>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">
		<%-- <button class="btn btn-cancel" type="button"><spring:message code="button.reject" /></button>
		<button class="btn btn-submit" type="button"><spring:message code="button.approve" /></button> --%>

		<a class="btn btn-submit approveStatusNotification" href="#"
			data-notificationId="${notification.notificationId}"
			data-eid="${notification.appdbId}"
			data-newStatus="${notification.requestedFor}"><spring:message
				code="button.approve" /></a> <a href="#"
			class="btn btn-cancel rejectStatusNotification"
			data-notificationId="${notification.notificationId}"
			data-eid="${notification.appdbId}"
			data-newStatus="${notification.requestedFor}"><spring:message
				code="button.reject" /></a>
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