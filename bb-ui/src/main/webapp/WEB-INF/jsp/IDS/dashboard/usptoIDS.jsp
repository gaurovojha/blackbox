<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />

<div role="tabpanel" class="tab-pane" id="usptoIDStab">
	<ul class="nav nav-tabs custom-tabs inner" role="tablist">
		<li role="presentation" class="active"><a href="#filingTab"
			role="tab" data-toggle="tab" id="lnkFilingInProgressIDS">Filing in Progress (1)</a></li>
		<li role="presentation"><a href="#uploadIDStab" role="tab"
			data-toggle="tab" id="lnkUploadManualIDS">Upload Manually Filed IDS (1)</a></li>
		<li role="presentation"><a href="#validateStatusTab" role="tab"
			data-toggle="tab" id="lnkValidateRefStatusIDS">Validate Reference Status (1)</a></li>
	</ul>
	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active" id="filingTab">
			<div class="tab-info-text">
				<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
				<p>Please review the request &amp; create an application record.
					You have received this request as the sender does not have rights
					to create an application record or the system is unable to find
					this application on Private PAIR. Please reject the notification if
					you do not wish to create the application record.</p>
			</div>
			<div class="tab-info-text">
				<h4 class="pull-left">List of Records (6)</h4>
				<span class="pull-right legend-txt">Legend: OA (Office
					Action)</span>
			</div>
			<!-- JSP include : filing in progress -->
			<jsp:include page="filing-inProgress-header.jsp"></jsp:include>
			
		</div>
		<div role="tabpanel" class="tab-pane" id="uploadIDStab">
			<div class="tab-info-text">
				<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
				<p>This is a system generated message. You have received this
					notification as the system is unable to find this document on
					Private PAIR. Please review the document details &amp; upload it
					manually. In case this document does not exist, please reject the
					notification.</p>
			</div>
			<div class="tab-info-text">
				<h4 class="pull-left">List of Records (2)</h4>
				<span class="pull-right legend-txt">Legend: OA (Office
					Action)</span>
			</div>
			<!-- JSP include :upload manually filled IDS -->
			<jsp:include page="upload-manually-header.jsp"></jsp:include>
			
		</div>
		<div role="tabpanel" class="tab-pane" id="validateStatusTab">
			<div class="tab-info-text">
				<span><i><img src="images/svg/info.svg" class="icon16x"></i></span>
				<p>This is a system generated message. You have received this
					notification as the system is unable to find this document on
					Private PAIR. Please review the document details &amp; upload it
					manually. In case this document does not exist, please reject the
					notification.</p>
			</div>
			<div class="tab-info-text">
				<h4 class="pull-left">List of Records (1)</h4>
				<span class="pull-right legend-txt">Legend: OA (Office
					Action)</span>
			</div>
			<!-- JSP include :Validate Reference Status -->
			<jsp:include page="validate-ref-status-header.jsp"></jsp:include>
			
		</div>
	</div>
</div>