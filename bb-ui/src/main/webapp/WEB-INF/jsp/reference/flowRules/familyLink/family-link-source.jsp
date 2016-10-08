<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table id="sourceAppTable" class="table custom-table">

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
	<tr class="odd">
		<td colspan="3">
				<select class="selectpicker form-control" id="selectedSourceAttr" data-live-search="true" onchange="loadDestinationData()">
				<c:forEach items="${referenceSource}" var="familySourcedata">
					<option id="${familySourcedata.dbId}" value="${familySourcedata.dbId}">${familySourcedata.jurisdiction}   ${familySourcedata.applicationNo}   ${familySourcedata.attorneyDocket}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<div id="emptyRows"></div>
</table>