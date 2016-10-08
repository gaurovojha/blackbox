<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<table class="table custom-table">
	<thead>
		<tr>
			<th>Family ID</th>
			<th>Jurisdiction</th>
			<th>Application #</th>
			<th>Attorney Docket #</th>
			<th>Filing Date</th>
			<th>Action</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${mdmDtoList}" var="application">
			<tr class="odd">
				<td>${application.familyId}</td>
				<td>${application.jurisdiction}</td>
				<td>${application.applicationNumber}</td>
				<td>${application.attorneyDocket}</td>
				<td><bbx:date dateFormat="MMM dd, yyyy" date="${application.filingDate}"/></td>
				<td>
					<div class="action-btns-grid">
						<form method="post" action="" id="form${application.applicationNumber}">
							<input type="hidden" name="applicationNumber" value="${application.applicationNumber}">
							<input type="hidden" name="jurisdictionCode" value="${application.jurisdiction}">
							<a class="selectLink" href="javascript:void(0)"> Select</a>
						</form>
					</div>
				</td>
				
		</c:forEach>
	</tbody>
</table>