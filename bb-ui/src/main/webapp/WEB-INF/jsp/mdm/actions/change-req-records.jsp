<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="canAccessUrl('/mdm/viewFamily/{familyId}')" var="canViewFamilyDetails" />
<sec:authorize access="canAccessUrl('/mdm/actions/drop/reject')" var="canRejectDrop" />
<sec:authorize access="canAccessUrl('/mdm/actions/drop/approve')" var="canApproveDrop" />
<sec:authorize access="canAccessUrl('/mdm/actions/deactivate/reject')" var="canRejectDeactivation" />
<sec:authorize access="canAccessUrl('/mdm/actions/deactivate/approve')" var="canApproveDeactivation" />

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="changeRequestRecordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}" />
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />


<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	<tr class="odd">
	<c:choose>
			<c:when test="${canViewFamilyDetails}">
					<td><a href=""  class="familyId" data="${app.familyId}"
			data-toggle="modal" data-target="#viewFamily">${app.familyId}</a></td>
			</c:when>
			<c:otherwise>
					<td>${app.familyId}</td>
			</c:otherwise>
		</c:choose>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo}</td>
		<td><div class="action-success">${app.currentStatus}</div></td>
		<td><div class="action-danger">${app.requestedFor}</div></td>
		<td>
			<div>${app.requestedBy}</div>
			<div class="pos-relative"><bbx:date dateFormat="MMM dd, yyyy" date="${app.requestedOn}"/>
			<a href="javascript:void(0)" data-toggle="tooltip" data-placement="top" title="" 
			data-original-title="${app.userComment}"><i><img src="${pathImg}/comment.svg" class="icon20x pull-right"></i></a></div>
		</td>
		<td>
			<div class="action-btns-grid">
			<c:if test="${(canApproveDrop and canRejectDrop) or (canApproveDeactivation or canRejectDeactivation)}">
				<a href="javascript:void(0)" class="approve" data-notificationId="${app.notificationId}" data-eid="${app.entityId}" data-newStatus="${app.requestedFor}"><i><img src="${pathImg}/approve.svg" class="icon16x"></i> Approve</a> 
				<a href="javascript:void(0)" class="reject" data-notificationId="${app.notificationId}" data-eid="${app.entityId}" data-newStatus="${app.requestedFor}"><span class="icon icon-drop"></span>Reject</a>
			</c:if>
			</div>
		</td>
	</tr>
</c:forEach>