<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<!DOCTYPE html>

<input type="hidden" id="recordsTotal" value="${page.totalElementsWitoutFilter}" />
<input type="hidden" id="recordsFiltered" value="${page.totalElements}" />

<p id="splitter"/>

<c:forEach items="${page.content}" var="reference">
	<tr class="odd">
		<td>${reference.nplDescription}</td>
		<td class="text-center">
                <div class="col-md-4 text-center">${reference.sourceJurisdictionCode}</div>
                <div class="col-md-4 text-center">${reference.applicationNumber}</div>
                <div class="pos-relative text-center">
                	<a href="dashboard/downloadFile" target="_blank" data-toggle="tooltip" title="${reference.documentDescription}">
                		<i><img src="<%=images%>/svg/attachment.svg" class="icon16x"></i>
                	</a>
                </div>
            </td>
		<td>${reference.notifiedDate}</td>
		<td class="text-center">
			<sec:authorize access="canAccessUrl('/reference/dashboard/verifyDuplicateCheck')" var="refEntry">
				<div class="action-btns-grid">
					<form:form method="post" action="dashboard/verifyDuplicateCheck" id="duplicateCheckForm${reference.notificationProcessId}">
						<input type="hidden" name="notificationProcessId" value="${reference.notificationProcessId}">
						<input type="hidden" name="notifiedDate" value="${reference.notifiedDate}">
						<input type="hidden" name="referenceBaseId" value="${reference.referenceBaseId}">
						<c:choose>
							<c:when test="${not empty reference.lockedBy && reference.lockedBy != userId}">
								Locked By: ${reference.firstName} ${reference.lastName}
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0);" onclick="document.getElementById('duplicateCheckForm${reference.notificationProcessId}').submit();">
									<i><img src="<%=images%>/svg/verify.svg" class="icon16x"></i> Verify
								</a>
							</c:otherwise>
						</c:choose>
					</form:form>
				</div>
			</sec:authorize>
		</td>
	</tr>
</c:forEach>