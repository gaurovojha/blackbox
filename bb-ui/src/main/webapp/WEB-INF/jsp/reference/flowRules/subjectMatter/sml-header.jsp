<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>

<div class="form-horizontal pad-top-10">
	<div class="form-group form-inline">
		<div class="col-sm-6" id="smldropdown">
			<label class="control-label">Showing</label> 
					<select	class="form-control" id="showFamilyLink" onchange="loadSmlData()">
						<option value="select">Select Destination family IDs</option>
						<c:forEach items="${targetFamilyIdList}" var="targetFamilyId">
							<option id="" value="${targetFamilyId}">${targetFamilyId}</option>
						</c:forEach>
					</select>
		</div>
		<div class="col-sm-6 text-right">
			<button type="button" class="btn btn-submit disabled" id="deleteFamilyLink">Delete link</button>
		</div>
	</div>
</div>
<table  class="table custom-table referenceTable">
	<tr>
		<td>
			<jsp:include page="sml-source.jsp" />
		</td>
		<td>
			<jsp:include page="sml-destination.jsp" />
		</td>
	</tr>
</table>
