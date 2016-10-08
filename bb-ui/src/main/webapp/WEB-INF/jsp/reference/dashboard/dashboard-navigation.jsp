<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>


<div class="main-content container">
	<sec:authorize access="canAccessUrl('/reference/dashboard/newSelfCitedReference')" var="refEntry">
		<div class="new-reference">
			<ul class="content-links added-txt clearfix">
				<li>
					<a href="dashboard/newSelfCitedReference">
						<i><img src="<%=images%>/svg/new-documents.svg" class="icon20x"></i> New Reference <span>(Self-citation)</span>
					</a>		
				</li>
			</ul>
		</div>
	</sec:authorize>
	
	<sec:authorize access="canAccessUrl('/reference/dashboard/referenceEntry')" var="refEntry"/>
	<sec:authorize access="canAccessUrl('/reference/dashboard/updateReference')" var="updateRef"/>
	<sec:authorize access="canAccessUrl('/reference/dashboard/duplicateCheck')" var="duplicateCheck"/>
	
	<c:if test="${refEntry || updateRef || duplicateCheck }">
		<div class="tab-container">
		    <ul class="tab-actions pull-right">
		        <li>
		            <div class="daterange-picker tab">
          				<input id="dateRangeFilter" type="text" class="form-control date"  name="datefilter" value="Showing till date">
						<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
          			</div>
		        </li>
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
												<input type="text" class="form-control">

											</div>
											<div class="col-sm-6">
												<label class="control-label">Jurisdiction</label>
												<input type="text" class="form-control">
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
												<input type="text" class="form-control">
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
												<input type="text" class="form-control">
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
												<label class="control-label">Jurisdiction</label>
												<input type="text" class="form-control">
											</div>
											<div class="col-sm-6">
												<label class="control-label">Application#</label>
												<input type="text" class="form-control">
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-12">
											<div class="col-sm-6">
												<label class="control-label">Upload Date</label>
												<div class="daterange-picker">
							          				<input type="text" class="form-control date">
													<span class="calendar"><i class="glyphicon glyphicon-calendar"></i></span>
							          			</div>
												
											</div>
											<div class="col-sm-6">
												<label class="control-label">Document Description</label>
												<input type="text" class="form-control">
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-12">
											<div class="col-sm-6">
												<label class="control-label">Uploaded By</label>
												<input type="text" class="form-control">
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
		        <li><a id="exportData" href="javascript:void(0);"><i data-alt="calendar"><img src="<%=images%>/svg/export.svg" class="icon20x"></i> Export</a></li>
		    </ul>
		    <!-- Nav tabs -->
		    <c:choose >
				<c:when test="${dashbordSubMenu eq 'referenceEntry'}">
					<c:set var="activeRefEntry" value="active"/>
					<c:set var="activeRefUpdate" value=""/>
					<c:set var="activeRefDuplicateCheck" value=""/>
				</c:when>
				<c:when test="${dashbordSubMenu eq 'updateReference'}">
					<c:set var="activeRefEntry" value=""/>
					<c:set var="activeRefUpdate" value="active"/>
					<c:set var="activeRefDuplicateCheck" value=""/>
				</c:when>
				<c:otherwise>
					<c:set var="activeRefEntry" value=""/>
					<c:set var="activeRefUpdate" value=""/>
					<c:set var="activeRefDuplicateCheck" value="active"/>
				</c:otherwise>
			</c:choose>
		    <ul class="nav nav-tabs custom-tabs" role="tablist">
	    		<c:if test="${refEntry}">
			        <li role="presentation" class="${activeRefEntry}">
			        	<a class="navigate" href="javascript:#"  url='${pageContext.request.contextPath}/reference/dashboard/referenceEntry' targetId='dashboardSubMenu' role="tab" data-toggle="tab">Reference Entry <span>(${referenceEntryCount})</span></a>
			        </li>
	    		</c:if>
	    		<c:if test="${updateRef}">
			        <li role="presentation" class="${activeRefUpdate}">
			        	<a class="navigate" href="javascript:void()"url='${pageContext.request.contextPath}/reference/dashboard/updateReference' targetId='dashboardSubMenu' role="tab" data-toggle="tab">Update Reference<span> (${updateReferenceCount})</span></a>
			        </li>
	    		</c:if>
	    		<c:if test="${duplicateCheck}">
			        <li role="presentation" class="${activeRefDuplicateCheck}">
			        	<a class="navigate" href="javascript:void()" url='${pageContext.request.contextPath}/reference/dashboard/duplicateCheck' targetId='dashboardSubMenu' role="tab" data-toggle="tab">Duplicate Check<span> (${duplicateCheckCount})</span></a>
			        </li>
	    		</c:if>
		    </ul>
		    <!-- Tab panes -->
		    <div class="tab-content">
			   	<div role="tabpanel" class="tab-pane active" id="dashboardSubMenu">
					<c:choose>
						<c:when test="${dashbordSubMenu eq 'referenceEntry'}">
							<jsp:include page="reference-entry-header.jsp"></jsp:include>
						</c:when>
						<c:when test="${dashbordSubMenu eq 'updateReference'}">
							<jsp:include page="update-reference-header.jsp"></jsp:include>
						</c:when>
						<c:otherwise>
							<jsp:include page="duplicate-check-reference-header.jsp"></jsp:include>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>	
	</c:if>
</div>

<script type="text/javascript" src="<%=js%>/reference/reference-dashboard.js"></script>
<jsp:include page="../scripts/reference-data-table.jsp"></jsp:include>

