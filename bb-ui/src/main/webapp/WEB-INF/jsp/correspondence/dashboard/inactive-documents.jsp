<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />
<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}" />

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="correspondence" varStatus="status">
 <tr>
		<td class="text-center">
			<span class="icon icon-plus expandCorrespondence correspondenceId" data="${correspondence.dbId}"><jsp:text /></span>
		</td>
		<td>${correspondence.jurisdictionCode}</td>
		<td>${correspondence.applicationNumber}</td>
		<td>${correspondence.mailingDate}</td>
		<td>
			<div class="table-data">${correspondence.documentDescription}
				<c:choose>
					<c:when test="${correspondence.viewDocumentLink}">
						<a href = "${contextPath}/correspondence/viewDocument/viewPdf/${correspondence.dbId}" target= "_blank" data = "${correspondence.documentLocation}">
							<span class="icon icon-attach attachmenticon"></span></a>
					</c:when>
					<c:otherwise>
						<a href="#" class = "icon icon-attach attachmenticon pull-right" data-toggle="tooltip" data-placement="bottom"
						title="" data-original-title= "This Document is Export Controlled. You do not have access"></a>
					</c:otherwise>
			 	</c:choose>
			 </div>
		</td>
		<td>
			<div>${correspondence.updatedByName}<br /></div>
			<div class="pos-relative">${correspondence.uploadedOn}
				<c:if test="${not empty correspondence.comments}">
					<img src="${pathImg}/comment.svg" class="icon20x pull-right" data-toggle="tooltip" data-placement="bottom" title="${correspondence.comments}"/></c:if> </div>
		</td> 
	<c:if test="${correspondence.status eq 'DROPPED'}">
		<td><span class="action-danger">Deleted</span></td>
	</c:if>
 </tr>
</c:forEach>