<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}"/>
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	 <input type="hidden" class="notificationId" value="${app.notificationId}" />
	 <input type="hidden" class="entityId" value="${app.entityId}" />
	 <input type="hidden" class="entityName" value="${app.entityName}" />
	<tr class="odd">
		<td><div class="jurisdictionCode">${app.jurisdiction}</div></td>
		<td><div class="appNo">${app.applicationNo}</div></td>
		<td>${app.sentBy}<a href="${contextPath}/mdm/actionItems/createRequestApp/getPdf/${app.entityId}" target= "_blank" ><i><img src="${pathImg}/attachment.svg" class="icon16x pull-right"></i></a></td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.notifiedDate}"/></td>
		<td>
			<div class="action-btns-grid">
			<sec:authorize access="canAccessUrl('/mdm/createApp/new')">
					<a href="javascript:void(0)" class="lnkCreate"><i><img
						src="${pathImg}/create.svg" class="icon16x"></i> Create</a>
			</sec:authorize>
			<sec:authorize access="canAccessUrl('/mdm/actionItems/createRequestApp/reject')">
					 <a href="javascript:void(0)" class="lnkReject"><i><img
						src="${pathImg}/reject.svg" class="icon16x"></i> Reject</a>
			</sec:authorize>
			</div>
		</td>
	</tr>
</c:forEach>