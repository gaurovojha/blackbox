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
			<spring:message code="ids.initiateIDS.idsNotification.1449update" />
		</h4>
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
					code="ids.initiateIDS.idsNotification.idspending" /></label>
			<div class="form-control-static">
				<c:forEach items="${notification.idsPending1449 }" var="idsDate"
					varStatus="loop">
					<c:choose>
						<c:when test="${fn:length(notification.idsPending1449) eq 1 }">
				Filed On
				<bbx:date dateFormat="MMM dd, yyyy" date="${idsDate}" />
						</c:when>
						<c:when test="${loop.last}">
							<bbx:date dateFormat="MMM dd, yyyy" date="${idsDate}" />
						</c:when>
						<c:otherwise>
				Filed On
				<bbx:date dateFormat="MMM dd, yyyy" date="${idsDate}" />,
				</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
		</div>
		<div class="col-sm-2">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.1449" /></label>
			<div class="form-control-static">
				<a href="javascript:void(0)"><i><img
						src="${contextPath}/assets/images/svg/attachment.svg"
						class="icon16x"></i></a>
			</div>
		</div>
	</div>
	<div class="footer">
		<button class="btn btn-submit" type="button">
			<spring:message code="button.1449update" />
		</button>
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