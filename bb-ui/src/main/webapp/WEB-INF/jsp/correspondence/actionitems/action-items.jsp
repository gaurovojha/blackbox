<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pathImg" value="${contextPath}/assets/images/svg"
	scope="request" />


<div class="main-content container">
		<div class="tab-container">
			<ul class="tab-actions pull-right">
				<li>
          			<div class="daterange-picker tab">
          				<input type="text" class="form-control date"  id="dateRangeReq" name="datefilter" value="Showing till date">
						<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
          			</div>
				</li>
				<!--search control header-->
			<li class="search-control"><%-- <jsp:include
					page="application-search.jsp"></jsp:include> --%></li>
			<li><a href="javascript:void(0)" class="actionItemExport"><i><img
						src="${pathImg}/export.svg" class="icon20x"></i> Export</a></li>
			</ul>
		  	<!-- Nav tabs -->
			<ul id="tabGroupActionItems" class="nav nav-tabs custom-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#updateDocTab" role="tab" data-toggle="tab" id="updateRequestLink">Update Request (<span id="updateRequestCount">${updateRequestCount}</span>)</a><span class="shortcut-pic"><i>X</i></span></li>
				<li role="presentation"><a href="#uploadDocTab" role="tab" data-toggle="tab" id="uploadRequestLink">Upload Request (<span id="uploadRequestCount">${uploadRequestCount}</span>)</a><span class="shortcut-pic"><i>Y</i></span></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="updateDocTab">
					<div class="tab-info-text">
						<span><i><img src="${pathImg}/info.svg" class="icon16x"></i></span> <p>This is a system generated message. You have received this notification as the system is unable to find this document on Private PAIR. Please review the document details & upload it manually. In case this document does not exist, please reject the notification.</p> 
					</div>
					<jsp:include page="update-request-header.jsp"></jsp:include>
				</div>
				<div role="tabpanel" class="tab-pane" id="uploadDocTab">
					<div class="tab-info-text">
						<span><i><img src="${pathImg}/info.svg" class="icon16x"></i></span> <p>This is a system generated message. You have received this notification as the system is unable to find this document on Private PAIR. Please review the document details & upload it manually. In case this document does not exist, please reject the notification.</p> 
					</div>
					<jsp:include page="upload-request-header.jsp"></jsp:include>
				</div>
			</div>
			<jsp:include page="../scripts/correspondence-data-table.jsp" />
			<jsp:include page="../scripts/export.jsp" />
			<jsp:include page="../scripts/actionItems.jsp" />
		</div>
	</div>
