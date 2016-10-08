<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<input type="hidden" id="jurisdictionList"	value="${listJurisdictions}" />
	
	
<div id="divApplicationForm" class="main-content container">
	<div class="row">
		<div class="col-sm-12">
			<div class="page-header">
				<span class="pull-right form-fields-tip">All <span
					class="asterisk">*</span> marked fields are compulsory
				</span>
				<h2 class="page-heading">Create Application</h2>
			</div>
		</div>
	</div>

	<form:form commandName="applicationForm" method="post" id="formApplication" action="${contextPath}/mdm/createApp/anotherApp">
		<form:hidden id="appFamilyId" path="parentFamily" />
		<form:hidden id="appParent" path="parentApplication" />
		<form:hidden id="screen" path="screen"/>
		<form:hidden path="action" value="CREATE_APPLICATION" />
		<form:hidden path="notificationId" />
		
		<div class="row applicationRow mdmApplication" id="mdmApplication_1">
			<div class="col-sm-6 mdm-right-pad">
				<div class="form-horizontal">
					<div id="application-init_1" page="1" class="application-init">
						<spring:nestedPath path="apps[0]">
							<jsp:include page="application-init.jsp" />
						</spring:nestedPath>
					</div>

					<!-- FAMILY LINKAGE -->
					<div id="divFamilySearchForm" class="familySearch" style="display: none;">
						<div class="divider"><jsp:text/></div>
						<jsp:include page="family-search-form.jsp"/>
					</div>
					<div id="resultFamilySearch" class="familySearch"><jsp:text/></div>
					
					<!-- Application Details -->
					<div id="application-content_1">
						<jsp:text/>
					</div>
				</div>
			</div>
			<c:if test="${not empty applicationForm.correspondenceId}">
				<div class="col-sm-6">
					<div class="form-contorl-static">
						<div style="height:643px" class="pdf-preview">
							<iframe style="height: 99%; width: 100%"
								src="${contextPath}/mdm/actionItems/createRequestApp/getPdf/${applicationForm.correspondenceId}">
							</iframe>
						</div>
					</div>
				</div>			
			</c:if>
		</div>
		
		<div class="row" id="divPageControls">
			<div class="col-sm-7 mdm-right-pad">
				<div id="btnControls"><jsp:text/></div>
			</div>
		</div>
		
	</form:form>
	<input type="hidden" id="draftAfterMS" value="${draftAfterMS}" />
	
	<jsp:include page="../dashboard/view-family-modal.jsp"></jsp:include>
	<jsp:include page="../scripts/application.jsp"></jsp:include>
</div>