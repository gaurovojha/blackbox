<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table custom-table" id="createFamilyRecords">
	<thead>
		<tr class="less-data-row">
			<th></th>
			<th></th>
			<th colspan="3" class="text-center">Link To...</th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<th>Jurisdiction</th>
			<th>Application No.</th>
			<th>Family ID</th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Source</th>
			<th>Notified Date</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody id="tbodyCreateFamilyRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>
