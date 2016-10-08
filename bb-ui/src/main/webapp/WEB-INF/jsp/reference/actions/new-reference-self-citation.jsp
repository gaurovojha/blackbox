<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>

<input type="hidden" id="jurisdictionList"	value="${listJurisdictions}" />
<div class="main-content container">

	<div class="row">
		<div class="col-sm-7 mdm-right-pad">
			<div class="form-horizontal">
				<div class="page-header">
					<h2 class="page-heading">New Reference (Self- citation)</h2>
				</div>
				
				<div id="familyLinkage">
					<div class="form-group">
						<div class="col-sm-6">
							<label class="control-label">Search Application</label>
							<select class="form-control" id="familyLinkageSelect">
								<option value="application">Application#</option>
								<option value="attdocket">Attorney Docket#</option>
								<option value="familyid">Family Id</option>
							</select>
						</div>
					</div>
					<div class="form-group" id="applicationLinkageData">
						<div class="col-sm-3">
							<label class="control-label">Jurisdiction</label>
							<input name="jurisCode" type="text" class="form-control jurisdiction">
						</div>
						<div class="col-sm-3">
							<label class="control-label">Application #</label>
							<input name="appNo" type="text" class="form-control">
						</div>
					</div>
					<div class="form-group" id="attorneyLinkageData">
						<div class="col-sm-6">
							<label class="control-label">Attorney Docket#</label>
							<input name="attdocketNo" type="text" class="form-control">
						</div>
					</div>
					<div class="form-group" id="familyLinkageData">
						<div class="col-sm-6">
							<label class="control-label">Family Id</label>
							<input name="familyNo" type="text" class="form-control">
						</div>
					</div>
					<div class="divider"></div>
					<div class="form-group">
						<div class="col-sm-12">
							<button type="button" class="btn btn-submit" id="showFamilyGrid" onclick="fetchApplications();">Search</button>
						</div>
					</div>
				</div>
			</div>
			<div id="divApplicationDetails">
				<jsp:text></jsp:text>
			</div>
		</div>	
	</div>		
</div>

<script type="text/javascript" src="<%=js%>/reference/reference-dashboard.js"></script>