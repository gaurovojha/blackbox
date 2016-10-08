<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="table custom-table" id="allRequestRecords">
	<thead>
		<tr>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Mailing Date</th>
			<th>Document Description</th>
			<th>Requester</th>
			<th>Approver</th>
			<th>Status</th>
		</tr>
	</thead>
	<tbody id="tbody">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>