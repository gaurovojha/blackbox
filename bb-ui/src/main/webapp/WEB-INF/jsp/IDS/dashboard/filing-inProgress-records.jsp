<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />



<c:forEach items="${searchResult}" var="app" varStatus="status">
	<tr class="inProgressRow" idsId = "${app.dbId}">
		<td>${app.familyId}</td>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo }</td>
		<td>${app.prosecutionStatus}</td>
		<td>${app.filingInstructedBy}</td>
		<td>${app.filingFee}</td>
		<td>${app.filingChannel}</td>
		<c:choose>
		<c:when test="${app.subStatus eq 'TRACKING_STATUS_REQUEST1_FAILED'}">
			<td><select class="form-control selectedStatus">
				<option>Please Select...</option>
				<option value="filedIt" >I have filed it</option>
				<option value="doNotFile">I have decided not to file</option>
		</select>
			<div class="action-success">Your response awaited</div></td>
		</c:when>
		<c:otherwise>
			<td>${app.status}</td>
		</c:otherwise>
		</c:choose>
		<c:choose>
		<c:when test="${(app.filingChannel eq 'SYSTEM') and (app.status eq 'GENERATING_FILE_PACKAGE' or app.status eq 'FILE_PACKAGE_GENERATED' or app.status eq 'FILED')}">
		<td>
			<div class="action-btns-grid">
				<a href="javascript:void(0)" class="disabled-link"><i><img src="${pathImg}/download.svg"
						class="icon16x"></i> IDS</a> <a href="javascript:void(0)" class="disabled-link"><i><img
						src="${pathImg}/download.svg" class="icon16x"></i> Filing
					Package</a>
			</div>
		</td>
		</c:when>
		<c:when test="${(app.filingChannel eq 'MANUAL') and app.status eq 'GENERATING_FILE_PACKAGE'}">
		<td>
			<div class="action-btns-grid">
				<a href="${contextPath}/userAction/downloadIDS/${searchResult.dbId}" data-target="#notificationLink"
					data-toggle="modal"><i><img src="${pathImg}/download.svg"
						class="icon16x"></i> IDS</a> <a href="javascript:void(0)" class="disabled-link"><i><img
						src="${pathImg}/download.svg" class="icon16x"></i> Filing
					Package</a>
			</div>
		</td>
		</c:when>
		<c:otherwise>
		<td>
			<div class="action-btns-grid">
				<a href="${contextPath}/userAction/downloadIDS/${searchResult.dbId}" data-target="#notificationLink"
					data-toggle="modal"><i><img src="${pathImg}/download.svg"
						class="icon16x"></i> IDS</a> <a href="javascript:void(0)"><i><img
						src="${pathImg}/download.svg" class="icon16x"></i> Filing
					Package</a>
			</div>
		</td>
		</c:otherwise>
		</c:choose>
		
	</tr>
</c:forEach>