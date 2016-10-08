<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:forEach items="${applicationForm.apps}" var="app" begin="0" end="${countApplications}" varStatus="status">
	<c:set var="expandLast" value="${status.last and not initScreen}" />
	<div class='mdmContainer'>
		<spring:nestedPath path="apps[${status.index}]">
			<c:set var="application" value="${applicationForm.apps[status.index]}" scope="request"/>
			<form:hidden path="uiDeleted" class="uiDeleted" />
			
			<div class="form-group create-app-data mdmRecord">
				<div class="action-btns">
					<a href="javascript:void(0)"><img src="${imgPath}/svg/approve.svg" class="icon20x" title="approve"/></a>
					<c:if test="${not status.first}">
						<a href="javascript:void(0)"><img src="${imgPath}/svg/delete.svg" class="icon20x deleteApplication" title="delete"/></a>
					</c:if>
				</div>
				
				<form:hidden path="id" class="appDbId" />
				<form:hidden id="applicationNo" path="applicationNo" />
					
				<div id="mdm-summary-${status.count}" class="mdm-summary mdmApplication">
					<div class="form-group visible-data appInits">
						<div class="col-sm-4">
							<label class="control-label">Jurisdiction <span class="required">*</span></label>
							<div class="form-control-static">${app.jurisdictionName}</div>
							<form:hidden id="appJurisdiction" path="jurisdictionName"/>
						</div>
						<div class="col-sm-4">
							<label class="control-label">Application No. <span class="required">*</span></label>
							<div class="form-control-static">${app.applicationNo}</div>
						</div>
						<div class="col-sm-4">
							<label class="control-label">Application Type<span class="required">*</span></label>
							<c:choose>
								<c:when test="${not canUpdateApplicationType}">
									<div class="form-control-static"><spring:message code="application.type.${app.applicationType}" /></div>
									<form:hidden path="applicationType"/>
								</c:when>
								<c:otherwise>
									<c:set var="nonFirstOnly" value="false" scope="request"/>
									<jsp:include page="dropdown-applicationTypes.jsp" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					
					<div class="create-app-data">
						<div id="application-content_${status.count}" class="application-content hidden-data" idxApp="${status.count}" style="display: ${expandLast ? 'block;' : 'none;'}">
							<jsp:include page="application-details.jsp"/>
						</div>
					</div>
					<a href="javascript:void(0)" class="toggle-link">${expandLast ? 'Hide' : 'Show'} Details</a>
				</div>
			</div>
		</spring:nestedPath>
	</div>
</c:forEach>