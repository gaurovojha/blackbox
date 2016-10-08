<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images" scope="request" />

<table class="table custom-table" id="tbl1449NotificationRecords">
										<thead>
											<tr>
												<th>Jurisdiction</th>
												<th>Application # </th>
												<th>IDS Pending 1449</th>
												<th>Notified</th>
												<th>Action</th>
											</tr>
										</thead>
	<tbody id="tbody1449NotificationRecords">
		<jsp:text/>
	</tbody>
</table>
