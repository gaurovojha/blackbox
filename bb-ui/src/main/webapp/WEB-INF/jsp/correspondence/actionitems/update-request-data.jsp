<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}"/>
<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />

<div>
	<sec:authorize access="canAccessUrl('/correspondence/reviewDocument')"
		var="accessReviewDoc" />
</div>

<p id="splitter"/>

<c:forEach items="${searchResult.items}" var="correspondence" varStatus="status">
	<tr class="odd">
	<td>${correspondence.jurisdictionCode}</td>
	<td>${correspondence.applicationNumber}</td>
	<td>${correspondence.mailingDate}</td>
	<td>${correspondence.documentDescription}</td>
	<td>${correspondence.updatedDate}</td>
	<td>${correspondence.ocrStatus}</td>
	<td>
	<c:choose>
						<c:when test="${accessReviewDoc}">
							<div class="action-btns-grid changeReviewStatus" data="${correspondence.dbId}"><a href="#" data-target="#myModal4" data-toggle="modal"><i><img src="${pathImg}/review-doc.svg" class="icon16x"></i>Review Document</a></div>
						</c:when>
						<c:otherwise>
							<div class="action-btns-grid changeReviewStatus" data="${correspondence.dbId}"><i><img src="${pathImg}/review-doc.svg" class="icon16x"></i>Review Document</div>
						</c:otherwise>
	</c:choose>
	</td>
</tr>
</c:forEach>