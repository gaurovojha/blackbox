<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}" />
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />
<div>
	<sec:authorize access="canAccessUrl('/correspondence/rejectDocument')"
		var="accessRejectDoc" />
	<sec:authorize access="canAccessUrl('/correspondence/uploadDocument')"
		var="accessUploadDoc" />
</div>

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="correspondence" varStatus="status">
	<tr class="odd">
	<td>${correspondence.jurisdictionCode}</td>
	<td>${correspondence.applicationNumber}</td>
	<td>${correspondence.mailingDate}</td>
	<td>${correspondence.documentDescription}</td>
	<td>${correspondence.updatedDate}</td>
	<td><div class="action-btns-grid" ><a class="uploadDocument" href="#" data="${correspondence.dbId}" ><i><img src="${pathImg}/upload-doc.svg" class="icon16x"></i>Upload Document</a>
	<c:choose>
						<c:when test="${accessRejectDoc}">
							<a id="rejectDocument" class="rejectDocument" data="${correspondence.dbId}" href="#"><i><img src="${pathImg}/reject.svg" class="icon16x"/></i>Reject</a>
						</c:when>
						<c:otherwise>
							<i><img src="${pathImg}/reject.svg" class="icon16x"/></i>Reject
						</c:otherwise>
	</c:choose>
		<%-- <div class="action-btns-grid" ><a class="uploadDocument" href="#" data="${correspondence.dbId}" ><i><img src="${pathImg}/upload-doc.svg" class="icon16x"></i>Upload Document</a>  <a id="rejectDocument" class="rejectDocument" data="${correspondence.dbId}" href="#"><i><img src="${pathImg}/reject.svg" class="icon16x"/></i>Reject</a></div> --%>
		</div>
	</td>
</tr>
</c:forEach>