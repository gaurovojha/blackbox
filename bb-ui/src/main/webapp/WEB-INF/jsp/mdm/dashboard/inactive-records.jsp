<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal"
	value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered"
	value="${searchResult.recordsFiltered}" />
<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="canAccessUrl('/mdm/viewFamily/{familyId}')" var="canViewFamilyDetails" />
<sec:authorize access="canAccessUrl('/mdm/application/reactivate/record')" var="canReactivateApp" />

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	<tr>
		<td class="text-center"><span class="icon icon-plus expandFamily"><jsp:text /></span></td>
		<c:choose>
			<c:when test="${canViewFamilyDetails and not (app.status eq 'DROPPED') }">
					<td><a href=""  class="familyId" data="${app.familyId}"
					data-toggle="modal" data-target="#viewFamily">${app.familyId}</a></td>
			</c:when>
			<c:otherwise>
					<td><a class="familyId disabled" data="${app.familyId}">${app.familyId}</a></td>
			</c:otherwise>
		</c:choose>
		<td>${app.jurisdiction}</td>
		<c:choose>
			<c:when test="${not (app.status eq 'DROPPED')}">
					<td><a href="javascript:void(0)" class="appId" data="${app.dbId}">${app.applicationNumber}</a></td>
			</c:when>
			<c:otherwise>
					<td><a class="appId disabled" data="${app.dbId}">${app.applicationNumber}</a></td>
			</c:otherwise>
		</c:choose>
		<td>${app.attorneyDocket}</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.filingDate}"/></td>
		<td>${app.assignee}</td>
		<td>${app.applicationType}</td>
		<td>
										<div>${app.modifiedBy}</div>
										<div class="pos-relative"><bbx:date dateFormat="MMM dd, yyyy" date="${app.modifiedOn}"/>
										<a href="javascript:void(0)" data-toggle="tooltip" data-placement="top" title="" 
										data-original-title="${app.userComment}"><i><img src="${pathImg}/comment.svg" class="icon20x pull-right"></i></a></div>
									</td>
		<c:choose>
			<c:when test="${(app.status eq 'TRANSFERRED') or (app.status eq 'ALLOWED_TO_ABANDON') or (app.status eq 'DEACTIVATED')}">
			<c:choose>
				<c:when test="${canReactivateApp}">
					<td><span class="action-success">${app.status}</span> <a id='statusActivate' class="statusActivate btn1"  href="#" data="${app.dbId}"  data-toggle="modal"  data-target="#reactivateRecord">Activate</a></td>
				</c:when>
				<c:otherwise>
					<td><span class="action-success">${app.status}</span> 
				</c:otherwise>
			</c:choose>
				
			</c:when>
			<c:when test="${app.status eq 'DROPPED'}">
				<td><span class="action-success disabled disableRow">${app.status}</span></td>
			</c:when>
			<c:when test="${app.newStatus eq 'DROPPED'}">
				<td><span class="action-success">Delete Pending</span></td>
			</c:when>
			<c:when test="${app.newStatus eq 'DEACTIVATED'}">
				<td><span class="action-success">Deactivate Pending</span></td>
			</c:when>			
			<c:otherwise>
				<td>${app.status}</td>
			</c:otherwise>
		</c:choose>
	</tr>
</c:forEach>