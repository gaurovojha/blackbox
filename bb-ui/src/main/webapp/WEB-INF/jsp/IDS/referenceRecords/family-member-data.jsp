<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<c:forEach items="${mdmDtoList}" var="application">
	<tr class="odd">
		<td>${application.jurisdiction}</td>
		<td>${application.applicationNumber}</td>
		<td>${application.attorneyDocket}</td>
		<td>
			<div class="action-btns-grid">
				<form:form method="post" action="${contextPath}/idsReference/viewReferenceRecords" id="viewReferenceRecordsForm${application.applicationNumber}">
					<!-- family ID we have to give-->
					<input type="hidden" name="familyid" value="${application.familyId}" />
					<input type="hidden" name="jurisdiction" value="${application.jurisdiction}" />
					<input type="hidden" name="applicationNumber" value="${application.applicationNumber}" />
					<input type="hidden" name="attorneyDocket" value="${application.attorneyDocket}" />
					<div class="action-btns-grid">
						<a href="javascript:void(0)"
							onclick="document.getElementById('viewReferenceRecordsForm${application.applicationNumber}').submit();">Select
						</a>
					</div>
				</form:form>
			</div>
		</td>
</c:forEach>