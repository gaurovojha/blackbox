<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:forEach items="${items}" var="app" varStatus="status">
	<tr>
	<form:input type="hidden" path="nplReference[${status.index}].refFlowId" id="refFlowId" value="${app.refFlowId}"/>
		<td>${app.npl}</td>
		<td>${app.currentStatus}/${app.currentSubStatus}</td>
		<td><form:checkbox path="nplReference[${status.index}].notCited" id="nocitedRadio4"/>
		<label for="nocitedRadio4" class="control-label">Not Cited </label> 
			<form:checkbox path="nplReference[${status.index}].mappedInIDS" id="filedIDS"/>
			<label for="filedIDS" class="control-label">Mapped to a filed IDS </label> 
			<form:checkbox path="nplReference[${status.index}].cited" id="citedIDSRadio4" class="citedIDSRadio4"/>
			<label for="citedIDSRadio4" class="control-label">Cited in IDS </label>
			<div class="inline-block form-inline idsFillingDate">
				<div class="input-group date datepicker" id="datetimepicker">
					<form:input path="nplReference[${status.index}].filingDate"type="text" class="form-control"
						placeholder="IDS Filing Date"/> <span
						class="input-group-addon"> <i
						class="glyphicon glyphicon-calendar"></i>
					</span>
				</div>
			</div></td>
	</tr>
</c:forEach>
