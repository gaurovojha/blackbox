<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="countFamilies" value="${fn:length(searchResult)}" />
<c:choose>
	<c:when test="${countFamilies eq 0}">
		No record found.
	</c:when>
	<c:otherwise>
		<table id="tblFamilyResults" class="table custom-table">
			<thead>
				<tr>
					<th>Family ID</th>
					<th>Jurisdiction</th>
					<th>Application #</th>
					<th>Attorney Docket #</th>
					<th>Filing Date</th>
					<th>Assignee</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${searchResult}" var="family" varStatus="status">
					<tr class="trFamilyResult" style="display: ${status.count gt 5 ? 'none': 'table-row'}">
						<input type="hidden" class="tdAppDbId" value="${family.dbId}" />
						<td><a href=""  class="familyId tdFamilyId" data="${family.familyId}"
			data-toggle="modal" data-target="#viewFamily">${family.familyId}</a></td>
						<td class="tdJurisdiction">${family.jurisdiction}</td>
						<td class="tdApplicationNo">${family.applicationNo}</td>
						<td class="tdDocketNo">${family.attorneyDocket}</td>
						<td><fmt:formatDate pattern="dd/MM/yyyy" value="${family.filedOn}" />
						<td class="tdAssignee">${family.assignee}</td>
						<td>
							<div class="action-btns-grid">
								<a href="javascript:void(0)" class="showFamilyLinked ${editApplication ? 'updateFamily' : 'linkFamily'}"><span
									class="icon icon-link"></span> Link</a>
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>

<c:if test="${countFamilies gt 5}">
	<a class="showAllFamilies" href="javascript:void(0)">Show All Results >></a>
</c:if>

<!-- Edit box with linked family details. -->

<c:if test="${not editApplication}">
<jsp:include page="linked-family-details.jsp" />
</c:if>
