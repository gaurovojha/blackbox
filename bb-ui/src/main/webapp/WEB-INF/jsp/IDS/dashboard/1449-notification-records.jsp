<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:forEach items="${listItems}" var="item">
	<tr>
		<input type="hidden" class="appId" value="${item.dbId}" />
		<td>${item.jurisdiction}</td>
		<td>${item.applicationNo}</td>
		<td><div class = "lstIdsPending1449"><c:forEach items="${item.idsPending1449 }" var="entry" varStatus = "loop">
			<c:if test="${loop.first}">
			<c:set var = "firstIdsId" value = "${entry.key}"/>
			</c:if>
			<c:choose>
				<c:when test = "${fn:length(item.idsPending1449) eq 1 }" >
				<input type = "hidden" class = "idsId" value = "${entry.key}"/>
				<span class="idsFilingDate"><bbx:date dateFormat="MMM dd, yyyy" date="${entry.value}" /></span>
				</c:when>
				<c:when test = "${loop.last}">
				<input type = "hidden" class = "idsId" value = "${entry.key}"/>
				<span class="idsFilingDate"><bbx:date dateFormat="MMM dd, yyyy" date="${entry.value	}" /></span>
				</c:when>
				<c:otherwise>
				<input type = "hidden" class = "idsId" value = "${entry.key}"/>
				<span class="idsFilingDate"><bbx:date dateFormat="MMM dd, yyyy" date="${entry.value}" /></span>,
				</c:otherwise>
			</c:choose>
			</c:forEach></div></td>
		<td>
			<bbx:date dateFormat="MMM dd, yyyy" date="${item.notifiedOn }"></bbx:date>
			<a href="javascript:void(0)"><i><img src="${contextPath}/assets/images/svg/attachment.svg" class="icon16x"></i></a>
		</td>
		<td>
			<div class="action-btns-grid">
				<a href="${contextPath}/ids/referenceReview/${item.dbId}/${firstIdsId}" class="lnk1449Update"><i><img
						src="${contextPath}/assets/images/svg/change-status.svg"
						class="icon16x"></i> 1449 Update</a>
			</div>
		</td>
	</tr>
</c:forEach>
