<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images" scope="request" />

<table class="table custom-table mdmTable" id="tblInitiateIDSUrgentRecords">
										<thead>
											<tr>
												<th></th>
												<th>Family ID</th>
												<th>Jurisdiction</th>
												<th>Application # </th>
												<th>Last IDS Filing Date</th>
												<th>Uncited References</th>
												<th>Prosecution Status</th>
												<th>Time Since last OA Response filed</th>
												<th>Action</th>
											</tr>
										</thead>
	<tbody id="tbodyInitiateIDSUrgentRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>