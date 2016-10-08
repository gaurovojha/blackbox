<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="smlDestListSize" value="${smlDestListSize}"/>
<c:forEach items="${referenceDestination.content}" var="familyDestdata" varStatus="status">
	<c:set var="evenOdd" value="odd"/> 
	<c:if test = "${status.count % 2 eq 0}">
		<c:set var="evenOdd" value="even"/>
	</c:if>
	<tr class="${evenOdd} height54">

			<td class="bdr-rt-none">
				${familyDestdata.targetApplication.jurisdiction}
			</td>
			<td class="bdr-rt-none">
				${familyDestdata.targetApplication.applicationNumber}
			</td>
			<td >
				${familyDestdata.targetApplication.attorneyDocket}
			</td>
			<td>
				<c:set var="status" value="" />
				<c:if test="${familyDestdata.status eq 'INACTIVE'}">
					<c:set var="status" value="checked" />
				</c:if>
				<div class="inline-block  pos-relative">
					<div class="switch-control on-off" data-toggle="tooltip" title="${familyDestdata.createdDate}" id="${familyDestdata.familyId}">
						<label class="switch"> 
							<c:set var="status" value="" />
							<c:if test="${familyDestdata.status eq 'INACTIVE'}">
								<c:set var="status" value="checked" />
								<input type="checkbox" class="switch-input" id="toggleCheck" checked="${status}" onclick="smlSwitchData('${familyDestdata.targetApplication.dbId}')"> 
							</c:if>
							<c:if test="${familyDestdata.status eq 'ACTIVE'}">
								<input type="checkbox" class="switch-input" id="toggleCheck" onclick="smlSwitchData('${familyDestdata.targetApplication.dbId}')"> 
							</c:if>
							<span class="switch-label" data-on="On" data-off="Off"></span> 
							<span class="switch-handle"></span>
							
						</label>
					</div>
				</div>
			</td>
			<td>${familyDestdata.modifiedBy}</td>
			<td>${familyDestdata.comments}</td>
	</tr>
</c:forEach>