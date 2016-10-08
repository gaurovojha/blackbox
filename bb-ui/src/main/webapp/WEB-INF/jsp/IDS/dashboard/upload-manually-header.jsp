<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images" scope="request" />

<table class="table custom-table" id="tblUpdloadManuallyRecords">
										<thead>
											<tr>
												<th>Jurisdiction</th>
												<th>Application # </th>
												<th>Mailing Date</th>
												<th>Document Description</th>
												<th>Notified</th>
												<th>Action</th>
											</tr>
										</thead>
	<tbody id="tbodyUpdloadManuallyRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>