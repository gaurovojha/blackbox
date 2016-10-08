<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}"/>
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	<tr class="odd">
	<td>${app.jurisdictionCode}</td>
	<td>${app.applicationNumber}</td>
	<td>${app.mailingDate}</td>
	<td>${app.documentDescription}</td>
	<td>${app.createdByName}<br/>${app.notifiedDate}</td>
	<td>${app.approver}</td>
	<td>
		<c:choose>
				<c:when test="${app.notificationStatus == 'PENDING'}">
				<div class="action-faulty">${app.notificationStatus}</div>
				</c:when>
				<c:otherwise>
				<div class="action-success">${app.notificationStatus}</div>
				</c:otherwise>
		</c:choose>
		${app.notificationEndDate}
	</td>
</tr>
</c:forEach>