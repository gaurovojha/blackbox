<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>

<div>
	<sec:authorize access="canAccessUrl('/correspondence/pairAudit/validate')" var="accessPAIRAudit"/>
	<sec:authorize access="canAccessUrl('/correspondence/activeDocuments')"    var="accessActiveDocuments"/>
	<sec:authorize access="canAccessUrl('/correspondence/inactiveDocuments')"  var="accessInactiveDocuments"/>
	
</div>


<c:set var="pathImg" value="${contextPath}/assets/images/svg" scope="request" />
<input type="hidden" id="jurisdictionData" value=${jurisdictionData } />


<div class="main-content container">
	<ul class="content-links clearfix">
		<sec:authorize access="canAccessUrl('/correspondence/add')">
			<li><a href="" id="newDocument" data-toggle="modal"
				data-target="#myModal2"> <i><img
						src="${pathImg}/new-documents.svg" class="icon20x"></i> New Documents </a>
						<span class="shortcut-pic"><i>N</i></span>
			</li>
		</sec:authorize>
		<li><a id="bulkUpload" href="" class="show-shortcut" data-toggle="modal"
			data-target="#myModal"> <i><img
					src="${pathImg}/bulk-uploads.svg" class="icon20x"></i> US Bulk
				Upload
		</a> <span class="shortcut-pic"><i>B</i></span>
		</li>
 		<c:if test="${accessPAIRAudit}">
			<li><a id="pairAudit" href="" data-toggle="modal" data-target="#myModal3"
				><i><img src="${pathImg}/pair-audit.svg"
						class="icon20x"></i> PAIR Audit</a>
				<span class="shortcut-pic"><i>P</i></span>		
			</li>
 		</c:if>
	</ul>
	<c:if test="${accessActiveDocuments or accessInactiveDocuments}">
		<c:if test="${not accessActiveDocuments}"> 
 			 <c:set value="active" var="cssClass"></c:set>
		</c:if> 	
	<div class="tab-container">
		<ul class="tab-actions pull-right">
			<li>
				<div class="daterange-picker tab">
					<input type="text" class="form-control date" name="datefilter"
						id="tblDateRangeFilter"> <span class="calendar"><i
						class="glyphicon glyphicon-calendar"></i></span>
				</div>
			</li>
			<!--search control header-->
			<li class="search-control"><jsp:include page="../common/correspondence-search.jsp"/></li>
			<li><a href="javascript:void(0)" class="export"><i
					data-alt="calendar"><img src="${pathImg}/export.svg"
						class="icon20x"></i> Export</a></li>
		</ul>
		<!-- Nav tabs -->
	
		<ul class="nav nav-tabs custom-tabs" role="tablist">
	<c:if test="${accessActiveDocuments}">
			<li role="presentation" class="active"><a href="#activeDocTab"
				role="tab" data-toggle="tab" id="activeDocuments">Active
					Documents</a></li>
		</c:if>
		<c:if test="${accessInactiveDocuments}">		
			<li role="presentation" class ="${cssClass}"> <a href="#inactiveDocTab" role="tab"
				data-toggle="tab" id="inactiveDocuments">Inactive Documents</a></li>
		</c:if>
		</ul>
		<!-- Tab panes -->
		<div class="tab-content">
			<c:if test="${accessActiveDocuments}">
				<div role="tabpanel" class="tab-pane active" id="activeDocTab">
				<div class="form-horizontal">
					<div class="list-doc-control">
						<span class="title">List of all Documents</span> <input
							type="checkbox" id="uploadedMyDocs" checked> <label
							class="control-label" for="uploadedMyDocs">Show My
							Uploaded Documents Only</label>
					</div>
				</div>
				<jsp:include page="active-documents-header.jsp"></jsp:include>
			</div>
		</c:if>
		<c:if test="${accessInactiveDocuments}">
			<div role="tabpanel" class="tab-pane ${cssClass}" id="inactiveDocTab">
				<jsp:include page="inactive-documents-header.jsp"></jsp:include>
			</div>
	</c:if>	
		</div>
	</div>
	</c:if>
	<jsp:include page="../scripts/export.jsp" />
	<jsp:include page="../common/confirmation-box.jsp" />
	<jsp:include page="../common/comments-popup.jsp" />

	<!--bulk upload control-->
	<div class="bulk-upload-control" id="FileUploadProgress"
		style="display: none">
		<div class="text-right upload-size"></div>
		<div class="upload-progress-block">
			<div class="progress">
				<div class="progress-bar progress-bar-success progress-bar-striped"
					role="progressbar" aria-valuenow="40" aria-valuemin="0"
					aria-valuemax="100" style="width: 0%" id="progressoffileupload">
					<span id="percent">0%</span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-9 col-lg-10 upload-details">
				<div class="files-uploaded">
					<span></span> <span></span>
				</div>
				<div class="upload-speed"></div>
				<div class="remaining-time">
					<span id="totalSize"></span>
				</div>
			</div>
		</div>
	</div>
</div>


<!-- new document -->
<div class="modal custom fade" id="myModal2" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">

	<!-- div to be replaced -->

	<div id="dynamicData"></div>
	<jsp:include page="../scripts/new-correspondence-script.jsp" />
</div>
<!-- Bulk Upload Modal -->

<jsp:include page="bulkupload.jsp" />
<jsp:include page="../scripts/Bulkupload-script.jsp"></jsp:include>



<!-- Pair Audit Modal -->
<jsp:include page="pair-audit.jsp"></jsp:include>
<jsp:include page="../scripts/pair-audit.jsp"></jsp:include>



<script type="text/javascript" src="<%=js%>/common/constants.js"></script>
<script type="text/javascript" src="<%=js%>/plugin/jquery.i18n.properties.js"></script>
<script type="text/javascript" src="<%=js%>/common/util/validation-util.js"></script>
<script type="text/javascript" src="<%=js%>/correspondence/validator/new-correspondence-form-validator.js"></script>
<script type="text/javascript" src="<%=js%>/correspondence/validator/search-correspondence-form-validator.js"></script>

<jsp:include page="../scripts/correspondence-data-table.jsp" />
<jsp:include page="../scripts/correspondence-dashboard.jsp" />

