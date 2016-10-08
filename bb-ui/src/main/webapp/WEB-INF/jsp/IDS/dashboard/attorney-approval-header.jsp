<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


	
<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
 <c:set var="pathJs" value="${contextPath}/assets/js"
	scope="request" />

<table class="table custom-table mdmTable" id="tblAttorneyApprovalRecords" >
	<thead>
		<tr>
		    <th></th>
			<th>Family ID</th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Last IDS Filling Date</th>
			<th>Prosecution Status</th>
			<th>Reference Count</th>
			<th>IDS Prepared by</th>
			<th>Comments</th>
			<th>Action</th>
		</tr>
	</thead>
	<tbody id="tbodyAttorneyApprovalRecords">
		<jsp:text />
	</tbody>
</table>

<div id="pageInfo"><jsp:text /></div>