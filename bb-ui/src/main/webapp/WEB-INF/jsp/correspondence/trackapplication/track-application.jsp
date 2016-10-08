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
			<li><a href="javascript:void(0)" class="trackApplicationExport"><i><img
						src="${pathImg}/export.svg" class="icon20x"></i> Export</a></li>
			</ul>
		  	<!-- Nav tabs -->
			<ul id="tabGroupActionItems" class="nav nav-tabs custom-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#updateDocTab" role="tab" data-toggle="tab" id="myRequestLink">My Requests</a></li>
				<li role="presentation"><a href="#addDocTab" role="tab" data-toggle="tab" id="allRequestLink">All</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="updateDocTab">
					<jsp:include page="my-request-header.jsp"></jsp:include>
				</div>
				<div role="tabpanel" class="tab-pane" id="addDocTab">
					<jsp:include page="all-request-header.jsp"></jsp:include>
				</div>
			</div>
			<jsp:include page="../scripts/correspondence-data-table.jsp" />
			<jsp:include page="../scripts/export.jsp" />
			<jsp:include page="../scripts/trackApplication.jsp" />
		</div>
	</div>
