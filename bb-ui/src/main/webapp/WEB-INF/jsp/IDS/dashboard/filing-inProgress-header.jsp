<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images" scope="request" />

<table class="table custom-table" id="tblFilingInProgressRecords">
										<thead>
											<tr>
												<th>Family ID</th>
												<th>Jurisdiction</th>
												<th>Application # </th>
												<th>Prosecution Status</th>
												<th>Filing Instructed by</th>
												<th>Filing fee</th>
												<th>Filing Channel</th>
												<th>Status</th>
												<th>Download</th>
											</tr>
										</thead>
	<tbody id="tbodyFilingInProgressRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>