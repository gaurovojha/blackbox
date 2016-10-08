<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<input type="hidden" id="recordsTotal" value="${searchResult.recordsTotal}" />
<input type="hidden" id="recordsFiltered" value="${searchResult.recordsFiltered}" />

<p id="splitter"/>
<c:forEach items="${searchResult.items}" var="app" varStatus="status">
	<tr>
		<td>${app.jurisdiction}</td>
		<td>${app.applicationNo}</td>
		<td><div class="action-danger">
				<i><img src="${contextPath}/assets/images/svg/exclamation.svg " class="icon16x"></i>${app.discrepencies}
			</div>
		</td>
		<td>${app.refernceDocument}<a class="pdfPop" data="${app.id}" href="javascript:void(0)">
			<i><img src="${contextPath}/assets/images/svg/attachment.svg" class="icon16x pull-right"></i></a>
		</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${app.notifiedDate}" /></td>
		<td>
			<div class="action-btns-grid">
			<sec:authorize access="canAccessUrl('/mdm/editApp/{appId}')">
				<a href="javascript:void(0)"><i><img src="${contextPath}/assets/images/svg/edit.svg " class="icon16x"></i>Edit</a>
			</sec:authorize>
			</div>
		</td>
	</tr>
</c:forEach>