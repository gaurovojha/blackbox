<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>


<c:forEach items="${searchResult.items}" var="app" varStatus="status">
<tr>
		<td colspan="2"></td>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo}</td>
		<c:choose>
			<c:when test = "${app.filingDate.time eq null}">
				<td><div style="text-align:center;">-</div></td>
			</c:when>
			<c:otherwise> 
				<td><a href="javascript:void(0)" ><bbx:date dateFormat="MMM dd, yyyy" date="${app.filingDate}"/></a></td>
			</c:otherwise>
		</c:choose>
	<td>${app.uncitedReferences}</td>
	<td>${app.uncitedReferencesAge } days</td>
	<td>${app.prosecutionStatus}</td>
	<td>
       <div class="action-btns-grid"><a href="${contextPath}/ids/buildIDS/123"><i><img src="${pathImg}/initiate-ids.svg" class="icon16x"></i> Initiate IDS</a></div>
   </td>
	
</tr>


</c:forEach>