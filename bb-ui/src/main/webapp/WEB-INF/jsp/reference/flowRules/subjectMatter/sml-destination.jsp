<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="table custom-table">

	<thead>
		<tr class="small-height-row">
			<th colspan="3" class="text-center">Destination Application</th>
			<th colspan="3"></th>
		</tr>
		<tr >
			<th class="bdr-rt-none">Jurisdiction</th>
			<th class="bdr-rt-none">Application #</th>
			<th>Attorney Docket #</th>
			<th class="bdr-rt-none">Status</th>
			<th class="bdr-rt-none">Last Edit</th>
			<th>Status Change Comment</th>
		</tr>
	</thead>
	<tbody id="referenceFlowSMLinkTbl"></tbody>
</table>