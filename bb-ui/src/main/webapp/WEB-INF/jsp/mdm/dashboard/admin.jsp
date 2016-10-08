<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />

<sec:authorize access="canAccessUrl('/mdm/activeRecords/view')" var="canViewActiveRecords" />
<sec:authorize access="canAccessUrl('/mdm/inactiveRecords/view')" var="canViewInactiveRecords" />


<div class="main-content container">
	<!-- Link to create application & View Switch -->
	<div class="clearfix">
		<ul class="content-links pull-left">
		<sec:authorize access="canAccessUrl('/mdm/createApp/new')">
			<li><a href="createApp/new"><span class="icon icon-doc"></span>New Application</a></li>
		</sec:authorize>
		</ul>
		<div class="pull-right">
			<div class="switch-control mdm">
				<label class="switch"> <input type="checkbox"
					id="switchRecordsView" class="switch-input" checked="checked">
					<span class="switch-label" data-on="Application View"
					data-off="Family View"></span> <span class="switch-handle"></span>
				</label>
			</div>
		</div>
	</div>
	<div class="tab-container">
		<ul class="tab-actions pull-right">
			<li>
				<div class="daterange-picker tab">
					<input type="text" class="form-control date" name="datefilter"
							value="Showing till date" id="tblDateRangeFilter"> <span class="calendar"><i
						class="glyphicon glyphicon-calendar"></i></span>
				</div>
			</li>
			<!--search control header-->
			<li class="search-control"><jsp:include
					page="application-search.jsp"></jsp:include></li>
			<li><a href="javascript:void(0)" class="export"><i><img
						src="${pathImg}/export.svg" class="icon20x "></i> Export</a></li>
		</ul>
		<!-- Navigation Tabs -->
		<ul class="nav nav-tabs custom-tabs" role="tablist">
			
			<c:if test="${canViewActiveRecords}">
				<li role="presentation" class="active"><a
				href="#tabActiveRecords" role="tab" data-toggle="tab"
				id="activeRecordsLink">Active Records</a></li>
			</c:if>
			<c:if test="${canViewInactiveRecords}">
				<li role="presentation" class="${(not canViewActiveRecords and canViewInactiveRecords)? 'active' : ''}"><a href="#tabInactiveRecords" role="tab"
				data-toggle="tab" id="inactiveRecordsLink">Inactive Records</a></li>
			</c:if>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">
			
			<c:if test="${canViewActiveRecords}">
				<div role="tabpanel" class="tab-pane active" id="tabActiveRecords">
					<div class="form-horizontal">
						<div class="list-doc-control">
							<span class="title">List of all Records</span> <input
								type="checkbox" id="myRecordsOnly" checked=""><label
								class="control-label" for="uploadedDocs">Show My Uploaded
								Records Only</label>
						</div>
					</div>
					<jsp:include page="active-records-header.jsp" />
				</div>
			</c:if>
		<c:if test="${canViewInactiveRecords}">
			<div role="tabpanel" class="${(not canViewActiveRecords and canViewInactiveRecords) ? 'tab-pane active' : 'tab-pane'}" id="tabInactiveRecords">
			<div class="form-horizontal">
				<!-- <div class="list-doc-control">
					<span class="title">List of all Records</span> <input
						type="checkbox" id="myRecordsOnly" checked=""><label
						class="control-label myRecordsOnly" for="uploadedDocs">Show My Uploaded
						Records Only</label>
				</div> -->
			</div>
				<jsp:include page="inactive-records-header.jsp"></jsp:include>
			</div>
		</c:if>
		</div>

	<div id="abandonStatusConfirmationBox" class="confirmationBox popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideConfirmationBox()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" id="abandonStatus" class="btn btn-submit">Yes</button>
                <button type="button" class="btn btn-cancel" onclick="hideConfirmationBox();">NO</button>
        </div>
	</div> 
		<jsp:include page="view-family-modal.jsp"></jsp:include>
		<jsp:include page="../scripts/mdm-data-table.jsp" />
		<jsp:include page="../scripts/dashboard.jsp" />
		<jsp:include page="../scripts/Export.jsp" />
	</div>
</div>
