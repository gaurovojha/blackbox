<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<input type="hidden" id="pendingIDSCount" value="${pendingApprovalCount}" />
<input type="hidden" id="pendingResponseCount" value="${pendingResponseCount}" />

<p id="splitter"/>

<c:forEach items="${searchResult}" var="app" varStatus="status">
<tr>
	<td>${app.familyId}</td>
	<td>${app.jurisdiction}</td>
	<td>${app.applicationNo }</td>
	<td>${app.prosecutionStatus}</td>
	<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.IDSApprovalPendingSince}"/><br/>${app.pendingDays}</td>
	<td>${app.pendingWith}</td>
	<td>${app.newReferences}</td>
	<td>
       <div class="action-btns-grid"><a href="javascript:void(0)"><i><img src="${pathImg}/initiate-ids.svg" class="icon16x"></i> Review</a></div>
   </td>
</tr>
</c:forEach>
