<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>



<c:forEach items="${items}" var="app" varStatus="status">
	<tr>
	<form:input type="hidden" path="usPublicationReference[${status.index}].refFlowId" id="refFlowId" value="${app.refFlowId}" />
		<td>${app.publicationNo}</td>
		<td>${app.kindCode}</td>
		<td>${app.applicant }</td>
		<td>${app.currentStatus}/${app.currentSubStatus}</td>
		<td>
		<form:checkbox path="usPublicationReference[${status.index}].notCited" id="nocitedRadio2"/><label
			for="nocitedRadio2" class="control-label">Not Cited </label> 
			<form:checkbox path="usPublicationReference[${status.index}].mappedInIDS" id="filedIDS2"/><label for="filedIDS"
			class="control-label">Mapped to a filed IDS </label> 
			<form:checkbox path="usPublicationReference[${status.index}].cited" id="citedIDSRadio2" class="citedIDSRadio2"/><label
			for="citedIDSRadio2" class="control-label">Cited in IDS </label>
			<div class="inline-block form-inline idsFillingDate">
				<div class="input-group date datepicker" id="datetimepicker2">
					<form:input path="usPublicationReference[${status.index}].filingDate"  class="form-control"
						placeholder="IDS Filing Date"/> <span
						class="input-group-addon"> <i
						class="glyphicon glyphicon-calendar"></i>
					</span>
				</div>
			</div></td>
	</tr>
</c:forEach>
