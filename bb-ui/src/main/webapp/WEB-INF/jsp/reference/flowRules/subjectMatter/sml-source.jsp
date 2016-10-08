<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table id="smlAppTable" class="table custom-table">

	<thead>
		<tr class="small-height-row">
			<th colspan="3" class="text-center">Source Application</th>
		</tr>
		<tr>
			<th class="bdr-rt-none">Jurisdiction</th>
			<th class="bdr-rt-none">Application #</th>
			<th>Attorney Docket #</th>
		</tr>
	</thead>
	<tbody id="referenceFlowSMLinkSrcTbl"></tbody>
	<div id="emptyRows"></div>
</table>