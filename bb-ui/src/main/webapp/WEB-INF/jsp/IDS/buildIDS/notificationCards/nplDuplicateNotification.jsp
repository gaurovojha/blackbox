<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<div class="notification-box">
	<a class="remove removeNotification"
		notificationId="${notification.notificationId}"><span
		aria-hidden="true">&times;</span></a>
	<div class="clearfix">
		<h4>NPL Duplicate Check</h4>
		<div class="col-sm-offset-6">
			<h5>
				<spring:message
					code="ids.initiateIDS.idsNotification.sourceapplication" />
			</h5>
		</div>
	</div>
	<div class="form-horizontal clearfix">
		<div class="col-sm-6 bdr-rt">
			<label class="control-label"><spring:message
					code="ids.initiateIDS.idsNotification.npldescription" /></label>
			<div class="form-control-static">${notification.nplDescription}</div>
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
			<label class="control-label">Source Doc.</label>
			<div class="form-control-static">
				<a
					href="${contextPath}/correspondence/viewDocument/viewPdf/${notification.correspondenceId}"
					target="_blank" data-toggle="tooltip"
					title="${notification.docDescription}" data-placement="bottom"><i><img
						src="${pathImg}/attachment.svg" class="icon16x"></i></a>
			</div>
		</div>
	</div>
	<div class="footer">
		<div class="action-btns-grid">
			<form:form method="post"
				action="${contextPath}/reference/dashboard/verifyDuplicateCheck"
				id="duplicateCheckForm${reference.notificationId}" target="_blank">
				<input type="hidden" name="notificationProcessId"
					value="${notification.notificationId}">
				<input type="hidden" name="notifiedDate"
					value="${notification.notifiedDate}">
				<input type="hidden" name="referenceBaseId"
					value="${notification.referenceBaseId}">
				<%-- <a href="javascript:void(0);" onclick="document.getElementById('duplicateCheckForm${reference.notificationId}').submit();">
									<i><img src="${pathImg}/verify.svg" class="icon16x"></i> Verify
								</a> --%>
				<button class="btn btn-submit" type="button"
					onclick="document.getElementById('duplicateCheckForm${reference.notificationId}').submit();">Verify</button>
			</form:form>
		</div>
		<!-- <button class="btn btn-submit" type="button">Verify</button> -->
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
