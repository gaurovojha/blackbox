<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="canAccessUrl('/mdm/viewFamily/{familyId}')" var="canViewFamilyDetails" />

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
		<c:choose>
			<c:when test="${canViewFamilyDetails}">
					<td><a href="" class="familyId" data="${app.linkedFamilyId}"
					data-toggle="modal" data-target="#viewFamily">${app.linkedFamilyId}</a></td>
					
			</c:when>
			<c:otherwise>
					<td>${app.linkedFamilyId}</td>
			</c:otherwise>
		</c:choose>
		
		<td>${app.linkedJurisdiction}</td>
		<td>${app.linkedApplicationNo}</td>
		<td>${app.source}</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.notifiedDate}"/></td>
		<td><div class="action-btns-grid">
				<a href="javascript:void(0)" class="lnkCreate"><i><img
						src="${pathImg}/create.svg" class="icon16x"></i> Create</a>
						 <a href="javascript:void(0)" class="lnkRejectApp"><i><img
						src="${pathImg}/reject.svg" class="icon16x"></i> Reject</a>
			</div></td>
	</tr>
</c:forEach>