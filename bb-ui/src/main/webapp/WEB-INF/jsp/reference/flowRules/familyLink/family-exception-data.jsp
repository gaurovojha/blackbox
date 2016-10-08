<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach items="${referenceDestination}" var="familyExclusionData"
	varStatus="status">
	<c:set var="evenOdd" value="odd"/> 
	<c:if test = "${status.count % 2 eq 0}">
		<c:set var="evenOdd" value="even"/>
	</c:if>
	<tr class="${evenOdd}">
		<td class="bdr-rt-none">${familyExclusionData.sourceApplication.jurisdiction}</td>
		<td class="bdr-rt-none">${familyExclusionData.sourceApplication.applicationNumber}</td>
		<td>${familyExclusionData.sourceApplication.attorneyDocket}</td>
		<td class="bdr-rt-none">${familyExclusionData.targetApplication.jurisdiction}</td>
		<td class="bdr-rt-none">${familyExclusionData.targetApplication.applicationNumber}</td>
		<td>${familyExclusionData.targetApplication.attorneyDocket}</td>
		<td><div>${familyExclusionData.modifiedBy}</div>
			<div>${familyExclusionData.modifiedDate}</div></td>
		<td>${familyExclusionData.comments}</td>
	</tr>
</c:forEach>