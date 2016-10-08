<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="table custom-table" id="updateReqRecords">
	<thead>
		<tr>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Mailing Date</th>
			<th>Document Description</th>
			<th>Notified</th>
			<th>Status</th>
			<th>Action</th>
		</tr>
	</thead>
	<tbody id="tbodyUpdateReqRecords">
		<jsp:text/>
	</tbody>
</table>

<div id="pageInfo"><jsp:text/></div>
<div class="modal custom fade modal-wide" id="myModal4" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"></div>