<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tr class="odd">
	<td colspan="3">
	<select class="selectpicker form-control" id="selectedSourceAttr" data-live-search="true" onchange="loadSmlDestinationData()">
			<c:forEach items="${smlSource}" var="familySourcedata">
				<option id="${familySourcedata.dbId}"
					value="${familySourcedata.dbId}">${familySourcedata.jurisdiction}
					${familySourcedata.applicationNo}
					${familySourcedata.attorneyDocket}
				</option>
			</c:forEach>
	</select></td>
</tr>