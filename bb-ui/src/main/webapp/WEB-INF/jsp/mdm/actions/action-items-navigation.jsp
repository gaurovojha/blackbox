<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="canAccessUrl('/mdm/actionItems/createRequestApp/view')" var="canViewCreateReq" />
<sec:authorize access="canAccessUrl('/mdm/actionItems/createRequestFamily/view')" var="canViewFamilyReq" />
<sec:authorize access="canAccessUrl('/mdm/actionItems/updateRequestApp/view')" var="canViewUpdateAppReq" />
<sec:authorize access="canAccessUrl('/mdm/actionItems/updateRequestAssignee/view')" var="canViewUpdateAssigneeReq" />
<%-- <sec:authorize access="canAccessUrl('/mdm/actionItems/updateRequestFamilyLinkage/view')" var="canEditApp" /> --%>
<sec:authorize access="canAccessUrl('/mdm/actionItems/changeRequest/view')" var="canViewChangeReq" />
<sec:authorize access="canAccessUrl('/mdm/editApp/{appId}')" var="canEditApp" />

<div class="main-content container">
	<div class="tab-container">
		<!-- Nav tabs -->
		<ul id="tabGroupActionItems" class="nav nav-tabs custom-tabs" role="tablist">
		<c:if test="${canViewCreateReq or canViewFamilyReq}">
			<li role="presentation" class="active"><a href="#createReqTab"
				role="tab" data-toggle="tab" id="createRequestLink">Create
					Requests <label id="lblCreateReqCount">(${actionItemsCount['createRequestCount']})</label></a></li>
		</c:if>
		<c:if test="${canViewUpdateAppReq or canViewUpdateAssigneeReq or canEditApp}">
			<li role="presentation" class="${not(canViewCreateReq or canViewFamilyReq)  and (canViewUpdateAppReq or canViewUpdateAssigneeReq or canEditApp) ? 'active' : ''}"><a href="#updateReqTab" role="tab"
				data-toggle="tab" id="updateRequestLink">Update
				 Requests<label id="lblupdateRequestCount">(${actionItemsCount['updateRequestCount']})</label></a></li>
		</c:if>
		<c:if test="${canViewChangeReq}">
			<li role="presentation" class="${not(canViewCreateReq or canViewFamilyReq) and not(canViewUpdateAppReq or canViewUpdateAssigneeReq or canEditApp) and canViewChangeReq ? 'active' : ''}"><a href="#changeRequestTab" role="tab"
				data-toggle="tab" id="changeRequestLink">Change Request<label
					id="lblChangeRequestCount">(${actionItemsCount['changeRequestCount']})</label></a></li>
		</c:if>
		</ul>
		<!-- Tab panes -->
		<div class="tab-content">
		<c:if test="${canViewCreateReq or canViewFamilyReq}">
			<div role="tabpanel" class="tab-pane active" id="createReqTab">

				<div>
					<ul class="tab-actions pull-right">
					
						<li>
								<div class="daterange-picker tab">
			          				<input type="text" id='dateRangeCreateReq' class="form-control date" name="datefilter" value="Showing till date">
									<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
			          			</div>
							</li>
					</ul>
					<ul class="nav nav-tabs custom-tabs inner" role="tablist">
					<c:if test="${canViewCreateReq}">
						<li role="presentation" class='active' ><a
							href="#createAppTab" role="tab" data-toggle="tab"
							id="createApplicationLink">Create Application</a></li>
					</c:if>
					<c:if test="${canViewFamilyReq}">
							<li role="presentation" class="${not canViewCreateReq and canViewFamilyReq ? 'active' : ''}"><a href="#createFamilyTab" role="tab"
							data-toggle="tab" id="createFamilyLink">Create Family Member</a></li>
					
					</c:if>
					</ul>
					<div class="tab-content">
					<c:if test="${canViewCreateReq}">
						<div role="tabpanel" class="tab-pane active" id="createAppTab">
							<div class="tab-info-text">
								<span><i><img src="${contextPath}/assets/images/svg/info.svg"
										class="icon16x"></i></span>
								<p>Please review the request & create an application record. You have received this request as the sender does not have rights to create an application record or the system is unable to find this application on Private PAIR. Please reject the notification if you do not wish to create the application record.</p>
							</div>
							<jsp:include page="create-app-header.jsp"></jsp:include>
						</div>
					</c:if>
					<c:if test="${canViewFamilyReq}">
						<div role="tabpanel" class="${not canViewCreateReq and canViewFamilyReq ? 'tab-pane active' : 'tab-pane' }" id="createFamilyTab">
							<div class="tab-info-text">
								<span><i><img src="${contextPath}/assets/images/svg/info.svg"
										class="icon16x"></i></span>
								<p>This is a system generated message. Please review the request & create an application record by linking it to the proposed family. You have received this request as a result of family validations run by the system on Private PAIR or INPADOC. Please reject the notification if you do not wish to create the application record.</p>
							</div>
							<jsp:include page="create-family-header.jsp"></jsp:include>
						</div>
					</c:if>	
					</div>
				</div>
			</div>
		</c:if>
		
			<!-- Update Request Tab Content -->
			
			<c:if test="${canViewUpdateAppReq or canViewUpdateAssigneeReq or canEditApp}">
				<div role="tabpanel" class="tab-pane" id="updateReqTab">
				
				<ul class="tab-actions pull-right">
							<li>
								<div class="daterange-picker tab">
			          				<input type="text" id='dateRangeUpdateReq' class="form-control date" name="datefilter" value="Showing till date">
									<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
			          			</div>
							</li>
						</ul>
				<div>
					<ul class="nav nav-tabs custom-tabs inner" role="tablist">
					<c:if test="${canViewUpdateAppReq}">
						<li role="presentation" class="active"><a
							id="applicationReqLink" href="#applicationTab" role="tab"
							data-toggle="tab">Application</a></li>
					</c:if>
					<c:if test="${canViewUpdateAssigneeReq}">
						<li role="presentation" class="${not canViewUpdateAppReq and canViewUpdateAssigneeReq ? 'active' : ''}"><a href="#assigneeTab"
						id="assigneeReqLink" role="tab" data-toggle="tab">Assignee</a></li>
					</c:if>
					<c:if test="${canEditApp}">
						<li role="presentation" class="${not canViewUpdateAppReq and not canViewUpdateAssigneeReq and canEditApp ? 'active' : ''}"><a href="#familyLinkageTab"
						id="familyReqLink" role="tab" data-toggle="tab">Family
						Linkage</a></li>
					</c:if>
					</ul>
					<div class="tab-content">
					<c:if test="${canViewUpdateAppReq}">
						<div role="tabpanel" class="tab-pane active" id="applicationTab">
							<div class="tab-info-text">
								<span><i><img src="${contextPath}/assets/images/svg/info.svg"
										class="icon16x"></i></span>
								<p> This is a system generated message. Please review the applicaton record & confirm whether the bibliographic data is in-line with USPTO, source being the reference document.</p>
							</div>
							<jsp:include page="update-app-header.jsp"></jsp:include>
						</div>
					</c:if>
					<c:if test="${canViewUpdateAssigneeReq}">
						<div role="tabpanel" class="${not canViewUpdateAppReq and canViewUpdateAssigneeReq ? 'tab-pane active' : 'tab-pane'}" id="assigneeTab">
							<div class="tab-info-text">
								<span><i><img src="${contextPath}/assets/images/svg/info.svg"
										class="icon16x"></i></span>
								<p>The system has created a new application record (Source: Private PAIR). Please add assignee to the application record.</p>
							</div>
							<jsp:include page="update-assignee-header.jsp"></jsp:include>
						</div>
					</c:if>
					<c:if test="${canEditApp}">
						<div role="tabpanel" class="${not canViewUpdateAppReq and not canViewUpdateAssigneeReq and canEditApp ? 'tab-pane active' : 'tab-pane'}" id="familyLinkageTab">
							<div class="tab-info-text">
								<span><i><img src="${contextPath}/assets/images/svg/info.svg"
										class="icon16x"></i></span>
								<p>This is a system generated message. Please review the request & confirm whether the proposed family linkage is in-line with your understanding. You have received this request as a result of family validations run by the system on Private PAIR or INPADOC. Please reject the notification if you do not wish to update the application record.</p>
							</div>
							<jsp:include page="update-family-header.jsp"></jsp:include>
						</div>
					</c:if>
					</div>
				</div>
			</div>
			</c:if>
			
			<c:if test="${canViewChangeReq}">
			<!-- Change Request Tab content -->
				<div role="tabpanel" class="tab-pane" id="changeRequestTab">
				<div class="clearfix">
					<h4 class="inner-heading pull-left">List of Notifications</h4>
					<ul class="tab-actions pull-right">
						<li>
								<div class="daterange-picker tab">
			          				<input type="text" id='dateRangeChangeReq' class="form-control date" name="datefilter" value="Showing till date">
									<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
			          			</div>
							</li>
					</ul>
				</div>
				<div class="tab-info-text">
					<span><i><img src="${contextPath}/assets/images/svg/info.svg" class="icon16x"></i></span>
					<p> You have received this change request for approval as the sender does not have rights to delete an application record. Please reject the notification if you do not wish to delete the application record.</p>
				</div>
				<jsp:include page="change-req-header.jsp"></jsp:include>
			</div>
			</c:if>
		</div>
	</div>
	
	<jsp:include page="../dashboard/view-family-modal.jsp"></jsp:include>
	<jsp:include page="../scripts/mdm-data-table.jsp" />
	<jsp:include page="pdf-popup.jsp" />
	<jsp:include page="add-assignee.jsp" />
	<jsp:include page="../scripts/actionItems.jsp" />

</div>
