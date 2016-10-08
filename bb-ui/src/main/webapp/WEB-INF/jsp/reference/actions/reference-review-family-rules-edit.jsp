<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reference Rule Review Edit</title>
</head>
<body>
		<input type="hidden" id="familyid" value="${familyId}">
		<div class="main-content container">
		<div class="page-header">
			<h2 class="page-heading">Review Reference Flow Rules </h2>
		</div>
		<div class="form-group family-id-txt">
			<label class="control-label">Family ID :</label> <a href="#">${familyId}</a>
		</div>
		<div class="tab-container">
		  	<!-- Nav tabs -->
			<ul class="nav nav-tabs custom-tabs inner" role="tablist">
				
				<li role="presentation" class="active">
					<form id="famL" action="reviewFamily" method="post" >
						<input type="hidden" name="familyid" value="${familyId}">
						<a href="#familyLinkTab" role="tab" data-toggle="tab" onclick="document.getElementById('famL').submit();">Family Link</a>
					</form>
				</li>
				
				<li role="presentation"><a href="#subjectMatterLinkTab" role="tab" data-toggle="tab" id="smlTab" >Subject Matter link</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				 <div role="tabpanel" class="tab-pane active" id="familyLinkTab">
					<jsp:include page="../flowRules/familyLink/family-link-header.jsp"></jsp:include>
				</div>
				<div role="tabpanel" class="tab-pane" id="subjectMatterLinkTab">
					<jsp:include page="../flowRules/subjectMatter/sml-header.jsp"></jsp:include>
				</div>
			</div>

		</div>

		<!-- transfer record modal -->
		<div class="modal custom fade" id="confirmStatusChange" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Confirm Status Change</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" id="comments">
							<p>Do you want to switch off......</p>
							<div class="form-group">
								<div class="col-sm-6">
									<label class="control-label" >Comments <span class="required" id="comment" style="display:none;">*</span></label>
									<textarea class="form-control" id="txtcomments" rows="4" cols="4"></textarea>
								</div>
							</div>

						</form>
					</div>
					<div class="modal-footer">
						<input type="hidden" id="sourceApp" value="">
						<input type="hidden" id="targetApp" value="">
						<input type="hidden" id="sourceFamilyId" value="">
						<input type="hidden" id="targetFamilyId" value="">
						<button type="button" class="btn btn-cancel" data-dismiss="modal">No</button>
						<button type="button" class="btn btn-submit" onclick="checkComment();">Yes</button>
					</div>
				</div>
			</div>
		</div>

		<!-- </div> -->
	
	
</body>

<jsp:include page="../scripts/reference-data-table.jsp"></jsp:include>

<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
%>
<script type="text/javascript" src="<%=js%>/reference/reference-flow-rule.js"></script>


</html>