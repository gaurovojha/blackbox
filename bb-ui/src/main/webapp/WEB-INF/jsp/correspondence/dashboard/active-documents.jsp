<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> 


<sec:authorize access="canAccessUrl('/correspondence/activeDocuments/viewDoc')" var="canAccessViewDocument"/>
<sec:authorize access="canAccessUrl('/correspondence/activeDocuments/deleteCorrespondence')" var="canAccessDeleteCorrespondence"/>
<sec:authorize access="canAccessUrl('/mdm/editApp/{appdbId}')" var="canEditApp" />

	
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />
<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}" />

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="correspondence">
	<tr>
			<td>
				<span class="icon icon-plus expandCorrespondence correspondenceId" data="${correspondence.dbId}"></span>
			</td>
			<td>${correspondence.jurisdictionCode}</td>
			<td>
				<c:choose>
					<c:when test="${canEditApp}">
						<a href="${contextPath}/mdm/editApp/${correspondence.appdbId}">${correspondence.applicationNumber}</a>
					</c:when>
					<c:otherwise>
						${correspondence.applicationNumber}
					</c:otherwise>			
				</c:choose>
			</td>
			<td>${correspondence.mailingDate}</td>
			<td>
				<div class="table-data">${correspondence.documentDescription}
					<c:choose>
						<c:when test="${correspondence.viewDocumentLink}">
							<a href = "${contextPath}/correspondence/viewDocument/viewPdf/${correspondence.dbId}" class = "icon icon-attach attachmenticon pull-right" target= "_blank" data = "${correspondence.dbId}"></a>
						</c:when>
						<c:otherwise><a href="#" class = "icon icon-attach attachmenticon pull-right"
										 data-toggle="tooltip" data-placement="bottom"
									title=""	data-original-title="This Document is Export Controlled. You do not have access"></a>
						</c:otherwise>
				 	</c:choose>
				 </div>
			 </td>
			<td>${correspondence.createdByName}<br />${correspondence.createdOn}
			</td>
			<td>
				<div class="action-btns-grid">	
					<c:if test="${canAccessViewDocument}">		
						<a href="#" data="${correspondence.dbId}" data-target="#myModal4" class="viewDocument"> <i><img src="${pathImg}/view-doc.svg" class="icon16x"></i> View Document</a>
					</c:if>
					<c:if test="${canAccessDeleteCorrespondence}">	
						<a href="#"  data="${correspondence.dbId}" class="action-danger deleteDocument"><img src="${pathImg}/delete.svg"   class="icon16x"> Delete</a>
					</c:if>
				</div>
			</td>
		</tr>
</c:forEach>