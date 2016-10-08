<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<c:forEach items="${searchResult}" var="app" varStatus="status">
	<tr>
		<td>${app.familyId}</td>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo }</td>
		<td>${app.prosecutionStatus}</td>
		<td>${app.approvedBy}</td>
		<td>${app.comments}</td>
		<td>
			<div class="action-btns-grid">
				<a href="${contextPath}/ids/buildIDS/${app.appId}"><i><img
						src="${pathImg}/edit.svg" class="icon16x"></i> Resubmit</a> 
						<a href="${contextPath}/ids/fileIds/${app.appId}/${app.dbId}" class="fileReadyIds" appid = "${app.appId}" idsid="${app.dbId}"><i><img
						src="${pathImg}/file-ids.svg" class="icon16x"></i> File IDS</a>
			</div>
		</td>
	</tr>
</c:forEach>

<jsp:include page="../scripts/build-ids.jsp" />