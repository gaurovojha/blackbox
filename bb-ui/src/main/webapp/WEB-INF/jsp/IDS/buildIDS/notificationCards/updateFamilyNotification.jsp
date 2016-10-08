<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<div class="notification-box">
	<a class="remove removeNotification" notificationId = "${notification.notificationId}"><span aria-hidden="true">&times;</span></a>
	<div class="clearfix">
		<h4>
			<spring:message
				code="ids.initiateIDS.idsNotification.updatefamilylinkage" />
		</h4>
		<div class="col-sm-offset-4">
			<h5>
				<spring:message code="ids.initiateIDS.idsNotification.linkto" />
			</h5>
		</div>
	</div>
	<div class="form-horizontal clearfix">

		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.jurisdiction" /></label>
			<div class="form-control-static">${notification.jurisdiction}</div>
		</div>
		<div class="col-sm-2 bdr-rt">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.application" /></label>
			<div class="form-control-static">${notification.applicationNo}</div>
		</div>
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
			<div class="form-control-static">${notification.linkedJurisdiction}</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.application" /></label>
			<div class="form-control-static">${notification.linkedApplicationNumber}</div>
		</div>

		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.source" /></label>
			<div class="form-control-static">${notification.source}</div>
		</div>
		<div class="col-sm-offset-4">
			<div class="form-control-static col-sm-12">
			<a href="" class="familyId" data="${notification.linkedFamilyId}"
					data-toggle="modal" data-target="#viewFamily">
				<i><spring:message
						code="ids.initiateIDS.idsNotification.currentlylinked" /> ${notification.linkedFamilyId}						
						</i> </a>
			</div>
		</div>
	</div>
	<div class="footer">
	 <input type="hidden" class="notificationId" value="${notification.notificationId}" />
	 <input type="hidden" class="entityId" value="${notification.entityId}" />
	 <input type="hidden" class="entityName" value="${notification.entityName}" />
	 
		<a class="btn btn-cancel rejectFamilyLinkage"  href="#">
			<spring:message code="button.reject" />
		</a>
		<a class="btn btn-submit"  href="${contextPath}/mdm/editApp/${notification.appdbId}?notificationId=${notification.notificationId}">
			<spring:message code="button.edit" /> </a>
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