<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
	
<c:forEach items="${correspondenceList.items}" var="correspondence">
<tr class="hidden-row">
	<td colspan="3" class="bdr-btm-none"></td>
	<td>${correspondence.mailingDate}</td>
	<td>
		<div class="table-data">${correspondence.documentDescription}
			<c:choose>
				<c:when test="${correspondence.viewDocumentLink}">
					<a href="${contextPath}/correspondence/viewDocument/viewPdf/${correspondence.dbId}" target="_blank" class="icon icon-attach attachmenticon pull-right"
						data="${correspondence.dbId}">
					</a>
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0)" class="icon icon-attach attachmenticon pull-right" data-toggle="tooltip" data-placement="bottom" 
					title="" data-original-title="This Document is Export Controlled. You do not have access"></a>
				</c:otherwise>
			</c:choose>
		</div></td>
	<td>
		<div>${correspondence.updatedByName}<br /></div>
		<div class="pos-relative">${correspondence.uploadedOn}
		 <c:if test="${correspondence.status eq 'DROPPED'}">
			<c:if test="${not empty correspondence.comments}">
				<i><img src="${pathImg}/comment.svg" class="icon20x pull-right" class="viewComments" data-toggle="tooltip" data-placement="bottom"  
				title  = ""	data-original-title="${correspondence.comments}"></i>
			</c:if>
		</c:if>
		</div>
	</td>
  <c:if test="${correspondence.status eq 'DROPPED'}">
	<td class="action-danger">Deleted</td>
  </c:if>
</tr>
</c:forEach>
