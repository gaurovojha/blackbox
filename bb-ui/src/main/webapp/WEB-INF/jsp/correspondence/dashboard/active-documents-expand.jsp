<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> 


	<sec:authorize access="canAccessUrl('/correspondence/activeDocuments/viewDoc')" var="canAccessViewDocument"/>
	<sec:authorize access="canAccessUrl('/correspondence/activeDocuments/deleteCorrespondence')" var="canAccessDeleteCorrespondence"/>


<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />
<c:forEach items="${correspondenceList.items}" var="correspondence">
<tr class="${correspondence.status eq 'DROPPED' ? 'hidden-row disabled': 'hidden-row' }" style="${correspondence.status eq 'DROPPED' ? 'pointer-events: none': 'display: table-row;' }">
	<td colspan="3"></td>
	<td>${correspondence.mailingDate}</td>
	<td><div class="table-data">${correspondence.documentDescription}
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
	<td><div>${correspondence.createdByName}<br /></div>
		<div class="pos-relative">${correspondence.createdOn}
		 <c:if test="${correspondence.status eq 'DROPPED'}">
			<c:if test="${not empty correspondence.comments}">
				<i><img src="${pathImg}/comment.svg" class="icon20x pull-right" class="viewComments" data-toggle="tooltip" data-placement="bottom"  
				title  = ""	data-original-title="${correspondence.comments}"></i>
			</c:if>
		</c:if>
		</div>
	</td>
	<c:choose>
		<c:when test="${correspondence.status eq 'ACTIVE'}">
			<td>
				<div class="action-btns-grid">
				<c:if test="${canAccessViewDocument}">	
					<a href="#" data="${correspondence.dbId}" data-target="#myModal4"
						data-toggle="modal" class="viewDocument"> <i><img
							src="${pathImg}/view-doc.svg" class="icon16x"></i> View Document</a>
				</c:if>	 
				<c:if test="${canAccessDeleteCorrespondence}">
					 <a href="#" data="${correspondence.dbId}" class="action-danger deleteDocument"><img src="${pathImg}/delete.svg" class="icon16x"> Delete</a><br /> 
					 <span class="action-success">Active</span>
				</c:if>	 
				</div>
			</td>
		</c:when>
		<c:when test="${correspondence.status eq 'DROPPED'}">
			<td class="action-danger">Deleted</td>
		</c:when>
		<c:otherwise>
		<c:if test="${correspondence.status eq 'ERROR'}">
		<td>
			<div class="action-btns-grid">
			<c:if test="${canAccessViewDocument}">	
				<a href="#" data="${correspondence.dbId}" data-target="#myModal4"
					data-toggle="modal" class="viewDocument"> <i><img
						src="${pathImg}/view-doc.svg" class="icon16x"></i> View Document
				</a> <br /> <span class="action-faulty">Error</span>
			</c:if>
			</div>
		</td>
			</c:if>
		</c:otherwise>
	</c:choose>
</tr>
</c:forEach>
