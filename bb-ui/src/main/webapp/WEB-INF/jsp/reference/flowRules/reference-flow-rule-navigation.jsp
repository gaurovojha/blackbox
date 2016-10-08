<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>


<div class="main-content container">
		<div class="new-reference">
			<ul class="content-links added-txt clearfix">
				<li>
					<a href="flowRule/newRuleSearch">
						<i><img src="<%=images%>/svg/new-documents.svg" class="icon20x"></i> New Rule  <span>(subject matter link)</span>
					</a>		
				</li>
			</ul>
		</div>
	
	<sec:authorize access="canAccessUrl('/reference/flowRule/allRecords')" var="allRecords"/>
	<sec:authorize access="canAccessUrl('/reference/flowRule/notifications')" var="notification"/>
	
	<c:set var="allRecords" value="true"/>
	<c:set var="notification" value="true"/>
	
	<c:if test="${allRecords || notification }">
		<div class="tab-container">
		    <ul class="tab-actions pull-right">
		    	<li class="search-control">
					<button type="submit" value="submit" class="search-btn">
						<i class="icon-search" data-alt="search"><img src="<%=images%>/svg/search.svg" class="icon20x"></i>
					</button>
					<div class="search-dropdown">
						<h5>Search By</h5>
						<ul class="nav nav-tabs custom-tabs" role="tablist">
							<li role="presentation" class="active"><a href="#applicationNoTab" role="tab" data-toggle="tab">Application#</a></li>
							<li role="presentation"><a href="#attorneyDocTab" role="tab" data-toggle="tab">Attorney Docket No.</a></li>
							<li role="presentation"><a href="#mailingDateTab" role="tab" data-toggle="tab">Family Id</a></li>
							<li role="presentation"><a href="#moreTab" role="tab" data-toggle="tab">More</a></li>
						</ul>

						<div class="tab-content">
							<div role="tabpanel" class="tab-pane active" id="applicationNoTab">
								<form class="form-horizontal">
									<div class="form-group">
										<div class="col-sm-12">
											<div class="col-sm-6">
												<label class="control-label">Application Number</label>
												<input id="txtApplicationNo" type="text" class="form-control">

											</div>
											<div class="col-sm-6">
												<label class="control-label">Jurisdiction</label>
												<input id="txtJurisdiction" type="text" class="form-control">
											</div>
										</div>
									</div>
								</form>
							</div>
							<div role="tabpanel" class="tab-pane" id="attorneyDocTab">
								<form class="form-horizontal">
									<div class="form-group">
										<div class="col-sm-12">
											<div class="col-sm-6">
												<label class="control-label">Attorney Docket Number</label>
												<input id = "txtAttorneyDocketNo" type="text" class="form-control">
											</div>
										</div>
									</div>
								</form>
							</div>
							<div role="tabpanel" class="tab-pane" id="mailingDateTab">
								<form class="form-horizontal">
									<div class="form-group">
										<div class="col-sm-12">
											<div class="col-sm-6">
												<label class="control-label">Family ID</label>
												<input id="txtFamilyId" type="text" class="form-control">
											</div>
										</div>
									</div>
								</form>
							</div>
							<div role="tabpanel" class="tab-pane" id="moreTab">
								<div class="form-horizontal">
									<div class="form-group">
										<div class="col-sm-12">
											<div class="col-sm-6">
												<label class="control-label">Upload Date</label>
												<div class="daterange-picker">
							          				<input id="txtUploadedOn" type="text" class="form-control date">
													<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
							          			</div>
												
											</div>
											<div class="col-sm-6">
												<label class="control-label">Document Description</label>
												<input id="txtDescription" type="text" class="form-control">
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-12">
											<div class="col-sm-6">
												<label class="control-label">Uploaded By</label>
												<input id="txtUploadedBy" type="text" class="form-control">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="search-footer clearfix">
							<div class="text-left">
								<button class="btn btn-cancel" id="hideSearch">Cancel</button>
								<button class="btn btn-submit" id="gotoSearch">Search</button>
							</div>
						</div>
					</div>
				</li>
				<li><a class="export" href="javascript:void(0);"><i data-alt="calendar"><img src="<%=images%>/svg/export.svg" class="icon20x"></i> Export</a></li>
			</ul>
			
			<!-- Nav tabs -->
		    <c:choose >
				<c:when test="${refFlowSubMenu eq 'allRecords'}">
					<c:set var="activeAllRecords" value="active"/>
					<c:set var="activeNotifications" value=""/>
				</c:when>
				<c:when test="${refFlowSubMenu eq 'notifications'}">
					<c:set var="activeAllRecords" value=""/>
					<c:set var="activeNotifications" value="active"/>
				</c:when>
			</c:choose>
			
			<ul class="nav nav-tabs custom-tabs" role="tablist">
	    		<c:if test="${allRecords}">
			        <li id="allRecords" role="presentation" class="${activeAllRecords}">
			        	<a class="navigate" href="javascript:#"  url='bb-ui/reference/flowRule/allRecords' targetId='refFlowSubMenu' role="tab" data-toggle="tab">All Records <span>(${referenceAllrecordsCount})</span></a>
			        </li>
	    		</c:if>
	    		<c:if test="${notification}">
			        <li role="presentation" class="${activeNotifications}">
			        	<a class="navigate" href="javascript:void()" url='bb-ui/reference/flowRule/notifications' targetId='refFlowSubMenu' role="tab" data-toggle="tab">Notifications<span> (${referenceNoticationsCount})</span></a>
			        </li>
	    		</c:if>
		    </ul>
		    
		    <!-- Tab panes -->
		    <div class="tab-content">
			   	<div role="tabpanel" class="tab-pane active" id="refFlowSubMenu">
			   		<jsp:include page="reference-flowrule-allrecords-header.jsp"></jsp:include>
<%-- 					<c:choose>
						<c:when test="${refFlowSubMenu eq 'allRecords'}">
							<jsp:include page="reference-flowrule-allrecords-header.jsp"></jsp:include>
						</c:when>
						<c:when test="${refFlowSubMenu eq 'notifications'}">
							<jsp:include page="reference-flowrule-notifications-header.jsp"></jsp:include>
						</c:when>
					</c:choose> --%>
				</div>
			</div>
		    
		</div>	
	</c:if>
</div>

<jsp:include page="../scripts/reference-data-table.jsp"></jsp:include>

<script>
$(document).ready(function() {
	var referenceTableRecords = null;
	fetchAllRecords();
	
	function fetchAllRecords() {
		clearTables();
		
		var refFlowSubMenu = $('#refFlowSubMenu').val();
		
		referenceTableRecords = new $.blackbox.reference.DataTable('#referenceEntryTable', '/reference/flowRule/allRecords', recordFilters(), true);
	}
	
	function recordFilters() {
		return {
			'iApplicationNumber' :$("#txtApplicationNo").val(),
			'iJurisdiction':$("#txtJurisdiction").val(),
			'iAttorneyDocketNumber':$("#txtAttorneyDocketNo").val(),
			'sFamilyId':$("#txtFamilyId").val(),
			'sDocumentDescription':$("#txtDescription").val(),
			'sUploadedBy':$("#txtUploadedBy").val(),
			'iUploadedDateRange':$("#txtUploadedOn").val()
		}
	}
	
	$("#gotoSearch").on("click", function(){
		if ($('#allRecords').hasClass('active')) {
			fetchAllRecords();
		} 	
		$(".search-dropdown").slideUp("hide");
	});	

	
	function clearTables() {
		if (referenceTableRecords != null) {
			referenceTableRecords.destroy();
			referenceTableRecords = null;
		}
		$('#referenceEntryTableBody1').html('');
	}
	
	// This must be a hyperlink
	$(".export").on('click', function (event) {
		
	 	var hasClass = $('#allRecords').hasClass('active');
		if(hasClass) {
			$.blackbox.util.exportTableToCSV.apply(this, ['referenceEntryTable', 'AllRecords.xls']);
		}
		// CSV
		//$.blackbox.util.exportTableToCSV.apply(this, ['referenceEntryTable', 'exportData.xls']);
  
	});

	var searchHeight = $('.search-dropdown').height();

	$(".search-control .search-btn").on("click", function(){

		$('.search-dropdown').slideToggle("show");
	});

	$("#hideSearch").on("click", function(){
		$(this).parents(".search-input").removeClass("active");
		$(".search-dropdown").slideUp("hide");
	}); 
	
	
});	

</script>