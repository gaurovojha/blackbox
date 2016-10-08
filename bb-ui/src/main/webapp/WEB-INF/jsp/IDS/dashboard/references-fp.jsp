<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:forEach items="${items}" var="app" varStatus="status">
	<tr>
	<form:input type="hidden" path="fpReference[${status.index}].refFlowId" id="refFlowId" value="${app.refFlowId}" />
		<td>${app.jurisdiction}</td>
		<td>${app.publicationNo}</td>
		<td>${app.kindCode}</td>
		<td>${app.applicant }</td>
		<td>${app.currentStatus}/${app.currentSubStatus}</td>
		<td><form:checkbox path="fpReference[${status.index}].notCited"  id="nocitedRadio3"/><label
			for="nocitedRadio3" class="control-label">Not Cited </label> 
			<form:checkbox path="fpReference[${status.index}].notCited"  id="filedIDS"/><label for="filedIDS"
			class="control-label">Mapped to a filed IDS </label> 
			<form:checkbox path="fpReference[${status.index}].notCited"  id="citedIDSRadio3" class="citedIDSRadio3"/>
			<label for="citedIDSRadio3"
			class="control-label">Cited in IDS </label>
			<div class="inline-block form-inline idsFillingDate">
				<div class="input-group date datepicker" id="datetimepicker">
					<input type="text" class="form-control"
						placeholder="IDS Filing Date"> <span
						class="input-group-addon"> <i
						class="glyphicon glyphicon-calendar"></i>
					</span>
				</div>
			</div></td>
	</tr>
</c:forEach>
