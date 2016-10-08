<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<!-- Active Records :: Application View -->
<table class="table custom-table mdmTable" id="tblActiveRecords">
	<thead>
		<tr>
			<th></th>
			<th>Family Id</th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Attorney Docket #</th>
			<th>Filing Date</th>
			<th>Assignee</th>
			<th>Application Type</th>
			<th>Created by</th>
			<th>Actions</th>
		</tr>
		<tbody id="tbodyActiveRecords">
		<jsp:text />
	</tbody>
</table>

<div id="pageInfo"><jsp:text /></div>

<div class="modal custom fade modal-wide" id="transferRecord" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<!-- Data through AJAX will be added here. -->
</div>

<div class="modal custom fade modal-wide" id="abandonRecord" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<!-- Data through AJAX will be added here. -->
</div>

<div class="modal custom fade modal-wide" id="deleteRecord" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<!-- Data through AJAX will be added here. -->
</div>

<div class="modal custom fade modal-wide" id="deactivateRecord" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<!-- Data through AJAX will be added here. -->
</div>

