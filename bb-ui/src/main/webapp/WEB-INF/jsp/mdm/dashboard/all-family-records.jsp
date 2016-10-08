<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>


<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	<tr class="${app.status eq 'DROPPED' ? 'hidden-row disabled': 'hidden-row' }" style="${app.status eq 'DROPPED' ? 'pointer-events: none': 'display: table-row;' }">
		<td colspan="2"></td>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNumber}</td>
		<td>${app.attorneyDocket}</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.filingDate}" /></td>
		<td>${app.assignee}</td>
		<td>${app.applicationType}</td>
		<c:choose>
			<c:when test="${tabName eq 'ACTIVE'}">
				<td>${app.createdBy}<br />
				<bbx:date dateFormat="MMM dd, yyyy" date="${app.createdOn}" />
				<td>
					<div class="action-btns-grid">
						<a href="${contextPath}/mdm/editApp/${app.dbId}"> <i><img
								src="${pathImg}/edit.svg" class="icon16x"></i> Edit
						</a>
						<div class="dropdown grid-dropdown">
							<a id="drop4" href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-haspopup="true"
								aria-expanded="false"><img
								src="${pathImg}/change-status.svg" class="icon20x"> Change
								Status <span class="caret"></span> </a>
							<ul class="dropdown-menu" aria-labelledby="drop4">
								<li><a id='statusTransfer' class="statusTransfer" href="#"
									data="${app.dbId}" data-toggle="modal"
									data-target="#transferRecord">Transfer Record</a></li>
								<li><a id='statusAbandon' class="statusAbandon" href="#"
									data="${app.dbId}" data-toggle="modal"
									data-target="#abandonRecord">Allowed to Abandon</a></li>
								<li><a id='statusDeactivate' class="statusDeactivate"
									href="#" data="${app.dbId}" data-toggle="modal"
									data-target="#deactivateRecord">Switch Off</a></li>
								<li><a id='statusDelete' class="statusDelete" href="#"
									data-id="${app.dbId}" data-familyId="${app.familyId}"
									data-toggle="modal" data-target="#deleteRecord">Delete</a></li>
							</ul>
						</div>
					</div>
				</td>
			</c:when>
			<c:otherwise>
				<td>
					<div>${app.createdBy}</div>
					<div class="pos-relative">
						<bbx:date dateFormat="MMM dd, yyyy" date="${app.createdOn}" />
						<a href="javascript:void(0)" data-toggle="tooltip"
							data-placement="top" title=""
							data-original-title="${app.userComment}"><i><img
								src="${pathImg}/comment.svg" class="icon20x pull-right"></i></a>
					</div>
				</td>
				<c:choose>
					<c:when
						test="${(app.status eq 'TRANSFERRED') or (app.status eq 'ALLOWED_TO_ABANDON') or (app.status eq 'DEACTIVATED')}">
						<td><span class="action-success">${app.status}</span> <a
							id='statusActivate' class="statusActivate btn1" href="#"
							data="${app.dbId}" data-toggle="modal"
							data-target="#reactivateRecord">Activate</a></td>
					</c:when>
					<c:when test="${app.status eq 'DROPPED'}">
						<td><span class="action-success disabled">${app.status}</span></td>
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
			</c:otherwise>
		</c:choose>

	</tr>
</c:forEach>