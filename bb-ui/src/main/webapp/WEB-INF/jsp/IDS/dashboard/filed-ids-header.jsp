<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images" scope="request" />

<table class="table custom-table" id="tblIdsFiledRecords">
										<thead>
											<tr>
										<th>IDS ID</th>
										<th>Family ID</th>
										<th>Jurisdiction </th>
										<th>Application # </th>
										<th>Filing Instructed by</th>
										<th>Filing fee</th>
										<th>Filing Channel</th>
										<th>Status</th>
										<th>Download</th>
									</tr>
										</thead>
	<tbody id="tbodyIdsFiledRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>