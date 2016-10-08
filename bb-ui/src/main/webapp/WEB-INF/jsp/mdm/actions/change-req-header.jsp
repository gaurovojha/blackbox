<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="table custom-table" id="changeReqRecords">
	<thead>
		<tr>
			<th>Family ID</th>
			<th>Jurisdiction</th>
			<th>Application No.</th>
			<th>Current Status</th>
			<th>Request for...</th>
			<th>Requested by</th>
			<th>Actions</th>
			<th>Notified On</th>
		</tr>
	</thead>
	<tbody id="tbodyChangeReqRecords">
		<jsp:text />
	</tbody>
</table>

<div id="pageInfo"><jsp:text /></div>