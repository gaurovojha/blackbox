<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}" />

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app">
	 <input type="hidden" class="notificationId" value="${app.dbId}" />
	 <input type="hidden" class="notificationId" value="${app.notificationId}" />
	 <input type="hidden" class="entityId" value="${app.entityId}" />
	 <input type="hidden" class="entityName" value="${app.entityName}" />
	<tr>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo}</td>
		<td><div>${app.createdBy}</div>
			<div>
				<bbx:date dateFormat="MMM dd, yyyy" date="${app.createdOn}" />
			</div>
		</td>
		<td>
			<div>
				<a href="" class="familyId" data="${app.linkedFamilyId}"
					data-toggle="modal" data-target="#viewFamily">${app.linkedFamilyId}</a>
			</div>
			<div>
				<a href="" class="familyId" data="${app.familyId}"
					data-toggle="modal" data-target="#viewFamily">Currently Linked With:${app.familyId}</a>
			</div>
		</td>
		<td>${app.linkedJurisdiction}</td>
		<td>${app.linkedApplicationNumber}</td>
		<td>${app.source}</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.notifiedDate}" /></td>
		<td><div class="action-btns-grid">
				<a href="${contextPath}/mdm/editApp/${app.dbId}?notificationId=${app.notificationId}"><i><img src="${contextPath}/assets/images/svg/edit.svg " class="icon16x"></i>Edit</a> 
				<a href="javascript:void(0)" class="lnkReject"><i><img src="${contextPath}/assets/images/svg/reject.svg" class="icon16x"></i>Reject</a>
			</div>
		</td>
	</tr>
</c:forEach>