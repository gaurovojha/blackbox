<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images" scope="request" />

<table class="table custom-table" id="tblPendingRecords">
										<thead>
										<tr>
											<th>Family ID</th>
											<th>Jurisdiction </th>
											<th>Application # </th>
											<th>Prosecution Status</th>
											<th>IDS Approval Pending Since</th>
											<th>Pending With</th>
											<th>New References</th>
											<th>Action</th>
										</tr>
									</thead>
	<tbody id="tbodyPendingRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>