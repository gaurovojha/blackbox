<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<input type="hidden" id="recordsTotal"
	value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered"
	value="${searchResult.recordsFiltered}" />
<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />


<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="app" varStatus="status">
<tr>
	<input type="hidden" class="appId" value="${app.dbId}" />
	<td><span class="icon icon-plus expandFamily"><jsp:text /></span></td>
	<c:choose>
		<c:when test = "${fn:contains(pageFamilies, app.familyId)}">
			<td><div class="duplicateFamily familyId">${app.familyId}</div></td>
		</c:when>
		<c:otherwise>
			<td><div class = "familyId">${app.familyId}</div></td>
			<c:set var="pageFamilies" value="${pageFamilies},${app.familyId}"/>
		</c:otherwise>
	</c:choose>
	<td>${app.jurisdiction}</td>
	<td>${app.applicationNo }</td>
	<c:choose>
		<c:when test = "${app.filingDate.time eq null}">
			<td><div style="text-align:center;">-</div></td>
		</c:when>
		<c:otherwise> 
			<td><a href="javascript:void(0)" ><bbx:date dateFormat="MMM dd, yyyy" date="${app.filingDate}"/></a></td>
		</c:otherwise>
	</c:choose>
	<c:if test="${app.uncitedReferencesAge gt 20}"> 
 			 <c:set value="action-danger" var="cssClass"></c:set>
	</c:if> 
	<td><span class ="${cssClass}"> ${app.uncitedReferences}</span></td> 
	<td>${app.prosecutionStatus}</td>
	<td>${app.lastOAResponse} days</td>
	<td>
		 <form method="post" action="initiate" id="initiateIDS${app.dbId}" >
       				<input type="hidden" name="appId" value="${app.dbId}">
       				<input type="hidden" name="referenceAge" value="${app.uncitedReferencesAge}">
       				<input type="hidden" name="prosecutionStatus" value="${app.prosecutionStatus}">
   				    <div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('initiateIDS${app.dbId}').submit();">
   	  				 	<i><img src="${pathImg}/initiate-ids.svg" class="icon16x"></i> Initiate IDS</a>
   	  				</div> 
       	</form>
   </td>
	
</tr>


</c:forEach>