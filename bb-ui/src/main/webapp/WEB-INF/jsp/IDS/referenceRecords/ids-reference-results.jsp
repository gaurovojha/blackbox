<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<div class="main-content container">

	<div class="search-result-text">
		Your Search Criteria, US 14/800,234 <a href="${contextPath}/ids/dashboard"><sup><i><img
					src="<%=images%>/svg/delete.svg" class="icon16x"></i></sup></a>
	</div>
	<div class="tab-container">
		<table class="table custom-table">
			<thead>
				<tr>
					<th>Family ID:</th>
					<th>Jurisdiction</th>
					<th>Application #</th>
					<th>Attornet Docket #</th>
					<th>Filing Date</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${mdmDtoList}" var="application">
					<tr class="odd">
						<td>${application.familyId}</td>
						<td>${application.jurisdiction}</td>
						<td>${application.applicationNumber}</td>
						<td>${application.attorneyDocket}</td>
						<td><bbx:date dateFormat="MMM dd, yyyy" date="${application.filingDate}"/></td>
						<td>
							<div class="action-btns-grid">
					 			<form:form method="post" action="${contextPath}/idsReference/viewReferenceRecords" id="viewReferenceRecordsForm${application.applicationNumber}"><!-- family ID we have to give--> 
									<input type="hidden" name="familyid" value="${application.familyId}"/>
									<input type="hidden" name="jurisdiction" value="${application.jurisdiction}"/>
									<input type="hidden" name="applicationNumber" value="${application.applicationNumber}"/>
									<input type="hidden" name="attorneyDocket" value="${application.attorneyDocket}"/>
		      						<div class="action-btns-grid"><a href="javascript:void(0)" onclick="document.getElementById('viewReferenceRecordsForm${application.applicationNumber}').submit();">
		      							<img src="<%=images%>/svg/review-doc-reference.svg" class="icon16x">View Reference Record</a>
		      						</div> 
								</form:form>
							</div>
						</td>						
				</c:forEach>
 			</tbody>			

		</table>

	</div>
</div>