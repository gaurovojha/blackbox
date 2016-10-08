<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>

<link rel="stylesheet" href="${contextPath}/assets/css/ids-style.css">
<!-- TODO: include this css reference in common -->

<!-- IDS: Main Navigation Bar -->
<nav class="main-nav">
	<div class="container" id="tabs">
		<ul>
			<li id="navDashboard" class="navButton"><a
				href="${contextPath}/ids/dashboard">MY DASHBOARD</a></li>
			<li id="navDashboard" class="navButton pos-relative"><a
				href="javascript:void(0)" data-toggle="dropdown">Reference
					Record</a>
				<ul class="dropdown-menu" aria-labelledby="dLabel"
					id="reference-search-control">
					<ul class="nav nav-tabs custom-tabs" role="tablist">
						<li role="presentation" class="active"><a
							href="#referenceAppTab" role="tab" data-toggle="tab">Application#</a></li>
						<li role="presentation"><a href="#referenceAttorneyTab"
							role="tab" data-toggle="tab">Attorney Docket No.</a></li>
						<li role="presentation"><a href="#referenceFamilyTab"
							role="tab" data-toggle="tab">Family Id</a></li>
					</ul>

					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="referenceAppTab">
							<form class="form-horizontal"
								action="${contextPath}/idsReference/referenceResult"
								method="post">
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6">
											<label class="control-label">Application#</label> <input
												type="text" name="applicationNumber" class="form-control">

										</div>
										<div class="col-sm-6">
											<label class="control-label">Jurisdiction</label> <input
												type="text" name="jurisdictionCode" class="form-control">
										</div>
									</div>
								</div>
							</form>
						</div>
						<div role="tabpanel" class="tab-pane" id="referenceAttorneyTab">
							<form class="form-horizontal"
								action="${contextPath}/idsReference/referenceResult"
								method="post">
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6">
											<label class="control-label">Attorney Docket Number</label> <input
												type="text" name="attdocketNo" class="form-control">
										</div>
									</div>
								</div>
							</form>
						</div>
						<div role="tabpanel" class="tab-pane" id="referenceFamilyTab">
							<form class="form-horizontal"
								action="${contextPath}/idsReference/referenceResult"
								method="post">
								<div class="form-group">
									<div class="col-sm-12">
										<div class="col-sm-6">
											<label class="control-label">Family Id</label> <input
												type="text" name="familyid" class="form-control">
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="search-footer clearfix">
						<div class="text-left">
							<button class="btn btn-cancel">Cancel</button>
							<button class="btn btn-submit ref-record-search">Search</button>
						</div>
					</div>
				</ul></li>
			<li id="navIDSNotification" class="navButton"><a
				href="${contextPath}/ids/initiateIDS/idsNotifications">IDS
					Notification</a></li>
		</ul>
	</div>
</nav>
<script type="text/javascript" src="<%=js%>/reference/ids-reference-record.js"></script> 
