<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<input type="hidden" id="screen" value="${applicationForm.screen}"/>
<c:set var="editFamilyLinkage" value="${applicationForm.action eq 'UPDATE_FAMILYLINKAGE'}" scope="session" />
<c:set var="editApplication" value="${applicationForm.action eq 'UPDATE_APPLICATION' or editFamilyLinkage}" scope="request" />
<input type="hidden" id="editApplication" value="${editApplication}" />
<input type="hidden" id="draftAfterMS" value="${draftAfterMS}" />
<input type="hidden" id="jurisdictionList"	value="${listJurisdictions}" />

<div id="divApplicationForm" class="main-content container">
	<div class="row">
		<div class="col-sm-12">
			<div class="page-header">
				<span class="pull-right form-fields-tip">All <span
					class="asterisk">*</span> marked fields are compulsory
				</span>
				<h2 class="page-heading">${editApplication ? 'Update Application' : 'Create Application'}</h2>
			</div>
		</div>
	</div>
	
	<c:set var="initScreen" value="${screen eq 'INIT'}" scope="request"/>
	<input type="hidden" id="multiApps" value="true"/>
	
	<div class="row">
       	<div class="col-sm-7 mdm-right-pad">
			<form:form commandName="applicationForm" method="post" id="formApplication"
				action="${contextPath}/mdm/${editApplication ? 'editApp/save' : (initScreen  ? 'createApp/details' : 'createApp/save')}"
				class="form-horizontal">
				
				<!-- Global Variables -->
				<form:hidden id="appFamilyId" path="parentFamily" />
				<form:hidden id="appParent" path="parentApplication" />
				<input type="hidden" id="appOldFamily" />
				<form:hidden id="screen" path="screen"/>
				<form:hidden path="action" />
				<form:hidden id="notificationId" path="notificationId"/>

				<!-- Control Flags -->				
				<c:set var="canUpdateApplicationType" value="${((editApplication and ((countFamilyMembers eq 1) and applicationForm.apps[0].firstFiling)
																					 or not applicationForm.apps[0].firstFiling)) or editFamilyLinkage}" scope="request" />
				<c:set var="showFamilyLinkageDetails" value="${editFamilyLinkage or canUpdateApplicationType and not applicationForm.apps[0].firstFiling}" scope="request" />

				<!-- Existing Applications -->
				<jsp:include page="application-summary.jsp"/>
				
				<c:choose>
					<c:when test="${initScreen and (not editApplication)}">
						<!-- Initials for new application -->
						<spring:nestedPath path="apps[${countApplications}].">
							<jsp:include page="application-init.jsp"/>
						</spring:nestedPath>
					</c:when>
					<c:otherwise>
						<jsp:include page="application-controls.jsp" />
					</c:otherwise>
				</c:choose>
				
				<!-- FAMILY LINKAGE -->
				<jsp:include page="family-linkage.jsp"/>
					
			</form:form>
		</div>
	</div>
</div>

<jsp:include page="../scripts/application.jsp"></jsp:include>