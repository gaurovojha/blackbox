<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}" />

<sec:authorize access="canAccessUrl('/mdm/viewFamily/{familyId}')" var="canViewFamilyDetails" />

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app">
	<tr >
		<input type="hidden" class="rowId" value="${app.notificationId}" />
		<c:choose>
			<c:when test="${canViewFamilyDetails}">
					<td><a href="" class="familyId" data="${app.familyId}"
					data-toggle="modal" data-target="#viewFamily">${app.familyId}</a></td>
			</c:when>
			<c:otherwise>
					<td>${app.familyId}</td>
			</c:otherwise>
		</c:choose>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo}</td>
		<td>${app.attorneyDocketNumber}</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.notifiedDate}"/></td>
		<td>
			<div class="action-btns-grid">
			<sec:authorize access="canAccessUrl('/mdm/actionItems/updateRequestAssignee/addAssignee')">
				<a href="" class="btnAddAssignee" data-toggle="modal" data-target="#updateAssignee"><i><img
						src="${contextPath}/assets/images/svg/add-assignee.svg " class="icon20x"></i> Add
					Assignee</a>
			</sec:authorize>
			</div>
		</td>
	</tr>
</c:forEach>