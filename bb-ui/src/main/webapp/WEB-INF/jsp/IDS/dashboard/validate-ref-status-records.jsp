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
		<td>${app.referenceCount}</td>
		<td>${app.notified}</td>
		<td>
			<div class="action-btns-grid">
				<a href="userAction/updateRefStatus/${app.dbId}/${app.notificationProcessId}"><i><img
						src="${pathImg}images/svg/change-status.svg" class="icon16x" ></i> Update
					Reference Status</a>
			</div>
		</td>

	</tr>


</c:forEach>