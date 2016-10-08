<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />

<!-- InActive Records :: Application View -->
<table class="table custom-table mdmTable" id="tblInActiveRecords">
	<thead>
		<tr>
			<th></th>
			<th>Family ID</th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Attorney Docket #</th>
			<th>Filing Date <span class=""></span></th>
			<th>Assignee</th>
			<th>Application Type</th>
			<th>Last Edited</th>
			<th>Status</th>
		</tr>
	</thead>
	<tbody id="tbodyInActiveRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>
<div class="modal custom fade modal-wide" id="reactivateRecord" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<!-- Data through AJAX will be added here. -->
</div>