<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />


<c:forEach items="${searchResult}" var="app" varStatus="status">
<tr>
	<td>${app.jurisdiction}</td>
	<td>${app.applicationNo }</td>
	<td>${app.mailingDate}</td>
	<td>${app.documentDesc}</td>
	<td>${app.notified}</td>
	<td>
		<div class="action-btns-grid"><a href="javascript:void(0)"><i><img src="images/svg/upload-doc.svg" class="icon16x"></i> Upload Document</a>  
		<a href="${contextPath}/userAction/rejectUploadIDS/${app.notificationId}"><i><img src="images/svg/reject.svg" class="icon16x"></i> Reject</a></div>
	</td>
	
</tr>


</c:forEach>