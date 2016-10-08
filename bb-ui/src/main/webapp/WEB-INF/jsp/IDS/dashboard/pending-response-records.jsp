<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>


<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
	
<input type="hidden" id="pendingIDSCount" value="${pendingApprovalCount}" />
<input type="hidden" id="pendingResponseCount" value="${pendingResponseCount}" />

<c:forEach items="${searchResult}" var="app" varStatus="status">
	<tr idsId="${app.dbId}"  notificationId="${app.notificationProcessId}">
		<td>${app.familyId}</td>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo }</td>
		<td>${app.prosecutionStatus}</td>
		<td>${app.IDSApprovalPendingSince}</td>
		<td>${app.attorney}</td>
		<td>${app.attorneyComments}</td>
		<td>
			<div class="action-btns-grid">
				<a href="${contextPath}/ids/buildIDS/${app.appId}"><i><img
						src="${pathImg}/edit-ids.svg" class="icon16x"></i> Edit IDS</a>
			</div>
			<div class="action-btns-grid">
				<a href="javascript:void(0)" data-target="#emailResponse"
					data-toggle="modal" class="lnkEmailResponse"><i><img
						src="${pathImg}/email-response.svg" class="icon16x"></i> Email
					Response</a>
			</div>
		</td>

	</tr>
</c:forEach>