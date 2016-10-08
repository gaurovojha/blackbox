<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%
	String context = request.getContextPath();
	String js = context+"/assets/js";
	String images = context+"/assets/images";
%>

<input type="hidden" name="referenceBaseId" value="${dto.referenceBaseId}" id="referenceBaseId">
<input type="hidden" name="notificationProcessId" value="${dto.notificationProcessId}" id="notificationProcessId">

<div class="popup-msg alert" id="notDuplicate">
	<div class="text-right"><a class="close" href="#">&times;</a></div>
	<div role="alert" class="content">
		<p>Are you sure want to continue ?</p>
		<button class="btn btn-submit" onclick="actionNotDuplicatePopupYes();">Yes</button>	
		<button class="btn btn-cancel">No</button>
	</div>
</div>

<div class="popup-msg alert" id="duplicate">
	<div class="text-right"><a class="close" href="#">&times;</a></div>
	<div role="alert" class="content">
		<p>Are you sure want to continue ?</p>
		<button class="btn btn-cancel">No</button>
		<button class="btn btn-submit" onclick="actionDuplicatePopupYes();">Yes</button> 	
	</div>
</div>

<div class="main-content container">
	<ul class="breadcrumb">
		<li><a href="../dashboard?requestComeFrom=duplicateCheck">My Dashboard</a></li>
		<li>Duplicate Check</li>
	</ul>
	<table class="table custom-table">
		<thead>
			<tr class="small-height-row">
	            <th></th>
	            <th colspan="4" class="text-center">Source Application</th>
	            <th></th>
	        </tr>
			<tr>
				<th>NPL Description</th>
				<th class="bdr-rt-none">Jurisdiction</th>
				<th class="bdr-rt-none">Application No.</th>
				<th>Source Doc.</th>
				<th>Family ID</th>
				<th>Notified Date</th>
			</tr>
		</thead>
		<tbody>
			<tr class="odd">
				<td>${dto.nplDescription}</td>
				<td class="bdr-rt-none">${dto.sourceJurisdictionCode}</td>
				<td class="bdr-rt-none">${dto.applicationNumber}</td>
				<td class="bdr-rt-none">
					<a href="downloadFile" target="_blank">
						<span class="icon icon-attach" data-toggle="tooltip" title="${dto.documentDescription}"></span>
					</a>
				</td>
				<td>${dto.familyId}</td>
				<td>${dto.notifiedDate}</td>
			</tr>
		</tbody>
	</table>
	<div class="form-horizontal">
		<div class="row">
			<div class="col-sm-6">
				<div class="pdf-preview-container">
					<h2>OCR - Scanned</h2>
					<div class="hide-pdf">- Hide OCR PDF</div>
					<div class="show-pdf">+ Show Original PD</div>
					<iframe style="height:800px; width:100%" src="<%=context%>/reference/dashboard/downloadFile"></iframe>
				</div>
			</div>
			<div class="col-sm-6">
				<h4 class="reference-head">Added Reference Entries</h4>
				<div class="entry-block">
					<div class="head"><span>NPL</span>
						<span class="action-btns">
							<!-- <a href="javascript:void(0)" class="pull-right"><img src="images/svg/delete.svg" class="icon20x"></a> -->
						</span> 
					</div>
					<div class="content">
						<div class="highlighted-block">
							<div class="clearfix">
								<div class="col-sm-12">
									<p>${dto.nplDescription}</p>
								</div>
							</div>
							<div class="clearfix">
								<div class="col-sm-12">
									<label class="control-label" id="duplicateLabel"><b><span class="action-danger">Duplicate </span></b></label>
									<button type="button" id="duplicateNo" class="btn btn-cancel small" onclick="openNotDuplicatePopup();">No</button>
									<button type="button" id="duplicateYes" class="btn btn-submit small" onclick="openDuplicatePopup();">Yes</button>
									<span id="message" class="hide">Reference successfully updated.</span>
								</div>
							</div>
						</div>
						<div class="divider"></div>
						
						<c:forEach var="data" items="${dtos}">
							<div class="tab-info-text action-danger">
								<span>
									<i><img src="<%=images%>/svg/exclamation2.svg" class="icon16x"></i>
								</span>
								<p>
									${data.nplDescription}
								</p> 
							</div>
							<div class="form-group">
								<div class="col-sm-12">
									<div class="col-sm-2">
										<label class="control-label">Family ID</label>
										<div class="form-control-static">${data.familyId}</div>
									</div>
									<div class="col-sm-3">
										<label class="control-label">Jurisdiction</label>
										<div class="form-control-static">${data.sourceJurisdictionCode}</div>
									</div>
									<div class="col-sm-3">
										<label class="control-label">Application #</label>
										<div class="form-control-static">${data.applicationNumber}</div>
									</div>
									<div class="col-sm-4">
										<label class="control-label">Attorney Docket #</label>
										<div class="form-control-static">${data.attorneyDocket}</div>
									</div>
								</div>
							</div>
							
							<div class="clearfix">
								<div class="col-sm-12">
									<label class="control-label">Source Document</label>
									<div class="form-control-static">
										List of references cited by examiner 
										<a href="downloadFile" target="_blank"> <img src="<%=images%>/svg/attachment.svg" class="icon16x"></a>
									</div>
								</div>
							</div>
						
						</c:forEach>
					</div>
				</div>
				<div class="divider"></div>
				<div class="text-left">
       				<a href="../dashboard?requestComeFrom=duplicateCheck"><button type="button" class="btn btn-cancel" data-dismiss="modal">Back</button></a>
       			</div>
			</div>
			
		</div>
	</div>
	
<script type="text/javascript">
	//simple alert popup js
	
	function openNotDuplicatePopup() {
		$("#notDuplicate").removeClass("hide");
		$("#notDuplicate").show();
		$("#notDuplicate").wrap("<div class='overlay'>");
	}
	
	function actionNotDuplicatePopupYes() {
		$("#notDuplicate").addClass("hide");
		$("#notDuplicate").unwrap("<div class='overlay'>");
		var referenceBaseId = $('#referenceBaseId').val();
		var notificationProcessId = $('#notificationProcessId').val();
		duplicateRefAction('/reference/dashboard/duplicateCheckAction', 'false', referenceBaseId, notificationProcessId);
	}
	
	function openDuplicatePopup() {
		$("#duplicate").removeClass("hide");
		$("#duplicate").show();
		$("#duplicate").wrap("<div class='overlay'>");
	}
	
	function actionDuplicatePopupYes() {
		$("#duplicate").addClass("hide");
		$("#duplicate").unwrap("<div class='overlay'>");
		var referenceBaseId = $('#referenceBaseId').val();
		var notificationProcessId = $('#notificationProcessId').val();
		duplicateRefAction('/reference/dashboard/duplicateCheckAction', 'true', referenceBaseId, notificationProcessId);
	}
	
	function duplicateRefAction(url, isDuplicate, referenceBaseId, notificationProcessId) {
		$.ajax({
			url: $('#contextPath').val() + url,
			"type" : "POST",
			'data':  {'isDuplicate': isDuplicate, 'referenceBaseId': referenceBaseId, 'notificationProcessId': notificationProcessId},
			//contentType: "application/json",
			success: function(response) {
				if (response == 'true') {
					$("#duplicateNo").addClass("hide");
					$("#duplicateYes").addClass("hide");
					$("#duplicateLabel").addClass("hide");
					$("#message").removeClass("hide");
				}
			}	
		});
	}
	
	$(document).on("click", ".popup-msg a.close, .popup-msg .btn-cancel", function() {
		$(this).parents(".popup-msg").addClass("hide");
		$(this).parents(".popup-msg").unwrap("<div class='overlay'>");
	});
</script>

