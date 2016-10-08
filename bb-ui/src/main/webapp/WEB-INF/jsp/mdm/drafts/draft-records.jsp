<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="now" value="<%=new java.util.Date()%>" />

<c:forEach items="${listItems}" var="draft">
	<tr id="${draft.dbId}">
		<input type="hidden" class="draftId" value="${draft.dbId}" />
		<td>${draft.jurisdiction}</td>
		<td>${draft.applicationNumber}</td>
		<td><bbx:date dateFormat="MMM dd, yyyy" date="${draft.modifiedOn}" id="dtModifiedOn"/></td>
		<td><div class="action-btns-grid">
				<a href="javascript:void(0);" class="openDraft"> <i><img
						src="${contextPath}/assets/images/svg/open.svg" class="icon16x"></i>Open
				</a> 
				<%-- <a href="${contextPath}/mdm/drafts/delete/${draft.dbId}"
					class="action-danger btnDeleteDraft"><i><img
						src="${contextPath}/assets/images/svg/delete.svg " class="icon16x"></i>
					Delete</a> --%>
					 <a href="javascript:void()" data="${draft.dbId}"
					class="action-danger btnDeleteDraft"><i><img
						src="${contextPath}/assets/images/svg/delete.svg " class="icon16x"></i>
					Delete</a>
			</div></td>
	</tr>
</c:forEach>

