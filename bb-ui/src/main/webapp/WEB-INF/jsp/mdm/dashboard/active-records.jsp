<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<input type="hidden" id="recordsTotal"
	value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered"
	value="${searchResult.recordsFiltered}" />
<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="canAccessUrl('/mdm/viewFamily/{familyId}')" var="canViewFamilyDetails" />
<sec:authorize access="canAccessUrl('/mdm/editApp/{appId}')" var="canEditApp" />
<sec:authorize access="canAccessUrl('/mdm/application/transfer/record')" var="canTransferApp" />
<sec:authorize access="canAccessUrl('/mdm/application/abandon/record')" var="canAbandonApp" />
<sec:authorize access="canAccessUrl('/mdm/application/deactivate/record')" var="canSwitchOffApp" />
<sec:authorize access="canAccessUrl('/mdm/application/delete/record')" var="canDropApp" />



<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	<tr>
		<td><span class="icon icon-plus expandFamily"><jsp:text /></span></td>
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
		<td><a href="javascript:void(0)" data-toggle="modal" data-target="viewFamily" class="appId" data="${app.dbId}">${app.applicationNumber}</a></td>
		<td>${app.attorneyDocket}</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.filingDate}"/></td>
		<td>${app.assignee}</td>
		<td><spring:message code="application.type.${app.applicationType}" /></td>
		<td>${app.createdBy}<br /><bbx:date dateFormat="MMM dd, yyyy" date="${app.createdOn}"/>
		</td>
		<td>
			<c:choose>
				<c:when test="${not empty app.lockedBy}">
					<i><img src="${pathImg}/locked.svg" class="icon16x"></i> ${app.lockedBy}
				</c:when>
				<c:otherwise>
					<div class="action-btns-grid">
						<c:if test="${canEditApp and !app.nonEditable}">
							<a href="${contextPath}/mdm/editApp/${app.dbId}">
								<i><img src="${pathImg}/edit.svg" class="icon16x"></i> Edit
							</a>
						</c:if>
						<c:choose>
							<c:when test="${canTransferApp or canAbandonApp or canSwitchOffApp or canDropApp}">
								<div class="dropdown grid-dropdown">
									<a id="drop4" href="#" class="dropdown-toggle"
										data-toggle="dropdown" role="button" aria-haspopup="true"
										aria-expanded="false"><img src="${pathImg}/change-status.svg"
										class="icon20x"> Change Status <span class="caret"></span> </a>
									<ul class="dropdown-menu" aria-labelledby="drop4">
									<c:if test="${canTransferApp}">
										<li><a id='statusTransfer' class="statusTransfer" href="#"
										data="${app.dbId}" data-toggle="modal"
										data-target="#transferRecord">Transfer Record</a></li>
									</c:if>
									<c:if test="${canAbandonApp}">
										<li><a id='statusAbandon' class="statusAbandon" href="#"
										data="${app.dbId}" data-toggle="modal"
										data-target="#abandonRecord">Allowed to Abandon</a></li>
									<c:if test="${canSwitchOffApp}"></c:if>
										<li><a id='statusDeactivate' class="statusDeactivate"
										href="#" data="${app.dbId}" data-toggle="modal"
										data-target="#deactivateRecord">Switch Off</a></li>
									</c:if>
									<c:if test="${canDropApp}">
										<li><a id='statusDelete' class="statusDelete" href="#"
										data-id="${app.dbId}" data-familyId="${app.familyId}" data-toggle="modal"
										data-target="#deleteRecord">Delete</a></li>
									</c:if>
									</ul>
								</div>
							</c:when>
						</c:choose>
					</div>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</c:forEach>