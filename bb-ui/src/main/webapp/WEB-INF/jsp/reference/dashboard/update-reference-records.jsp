<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="bbx" uri="/WEB-INF/tld/bbxTagLib"%>

<!DOCTYPE html>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<input type="hidden" id="recordsTotal" value="${page.totalElementsWitoutFilter}" />
<input type="hidden" id="recordsFiltered" value="${page.totalElements}" />

<p id="splitter"/>

<c:forEach items="${page.content}" var="reference" varStatus="count">
	<tr class="odd updateReferenceRow">
		<td>
			<div class="action-danger">
				${reference.refJurisdictionCode}
			</div>
		</td>
		<td>
			<div class="action-danger">
				${reference.publicationNo}
			</div>
		</td>
		<td class="bdr-rt-none">${reference.sourceJurisdictionCode}</td>
		<td class="bdr-rt-none">${reference.applicationNumber}</td>
		<td>
			<c:if test="${not empty reference.correspondenceId}">
	        	<div class="pos-relative">
	        		<a href="dashboard/downloadFile" target="_blank" data-toggle="tooltip" data-placement="top" title="${reference.documentDescription}" data-original-title="">
	        			<i><img src="<%=images%>/svg/attachment.svg" class="icon16x"></i>
	        		</a>
	        	</div>
			</c:if>
		</td>
		<td><%-- <bbx:date dateFormat="MMM dd, yyyy" date="${reference.notifiedDate}"/> --%>${reference.notifiedDate}</td>
		<td class="text-center">
			<div class="action-btns-grid">
				<c:choose>
					<c:when test="${not empty reference.lockedBy && reference.lockedBy != userId}">
						Locked By: ${reference.firstName} ${reference.lastName}
					</c:when>
					<c:otherwise>
						<sec:authorize access="canAccessUrl('/reference/dashboard/addDetailsUpdateReference')">
							<form:form method="post" action="dashboard/addDetailsUpdateReference" id="updateRefAddDetailsForm${count.index}">
								<input type="hidden" name="notificationProcessId" value="${reference.notificationProcessId}">
								<input type="hidden" name=referenceStagingId value="${reference.referenceStagingId}">
								<a href="javascript:void(0)" onclick="document.getElementById('updateRefAddDetailsForm${count.index}').submit();" class="add-detailss">
									<img src="<%=images%>/svg/add-details.svg" class="icon16x"></i> Add Details
								</a>
							</form:form>
						</sec:authorize>
						<sec:authorize access="canAccessUrl('/reference/dashboard/deleteUpdateReference')">
							<form:form method="post" action="dashboard/deleteUpdateReference" id="updateRefDeleteForm${count.index}">
								<input type="hidden" name="notificationProcessId" value="${reference.notificationProcessId}">
								<input type="hidden" name=referenceStagingId value="${reference.referenceStagingId}">
									<a href="javascript:void(0)" onclick="openUpdateRefPopup('${count.index}');" class="action-danger">
										<i><img src="<%=images%>/svg/delete.svg" class="icon16x"></i> Delete Reference
									</a>
							</form:form>
						</sec:authorize>
					</c:otherwise>
				</c:choose>
			</div>
		</td>
	</tr>
</c:forEach>