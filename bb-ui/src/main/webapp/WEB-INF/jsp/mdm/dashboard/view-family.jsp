<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<h4>
	<span>${familyId}</span></br>
	</h3>
	<table class="table custom-table mdmTable" id="tblActiveRecords">
		<thead>
			<tr>
				<th>Jurisdiction</th>
				<th>Application #</th>
				<th>Attorney Docket #</th>
				<th>Filing Date</th>
				<th>Assignee</th>
				<th>Application Type</th>
			</tr>
		<tbody id="tbodyViewFamily">
			<c:forEach items="${searchResult}" var="app" varStatus="status">
				<tr>
					<td>${app.jurisdiction}</td>
					<td>${app.applicationNo}</td>
					<td>${app.attorneyDocket}</td>
					<td>${app.filedOn}</td>
					<td>${app.assignee}</td>
					<td>${app.applicationType}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>