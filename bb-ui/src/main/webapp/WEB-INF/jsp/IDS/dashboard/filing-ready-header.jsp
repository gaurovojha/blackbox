<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images"
	scope="request" />

<table class="table custom-table" id="tblFilingReadyRecords">
	<thead>
		<tr>
			<th>Family ID</th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Prosecution Status</th>
			<th>Approved by</th>
			<th>Comments</th>
			<th>Action</th>
		</tr>
	</thead>
	<tbody id="tbodyFilingReadyRecords">
		<jsp:text />
	</tbody>
</table>

<div id="pageInfo"><jsp:text /></div>