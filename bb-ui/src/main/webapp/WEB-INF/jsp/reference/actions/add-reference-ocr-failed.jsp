<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String context = request.getContextPath();
	String images = context+"/assets/images";
	String js = context+"/assets/js";
%>
<input type="hidden" id="jurisdictionList"	value="${listJurisdictions}" />
<c:set var="dashbordSubMenu" value="${dashbordSubMenu}" scope="request"/>

<div class="main-content container">
	<ul class="breadcrumb">
		<li><a href="../dashboard">My Dashboard</a></li>
		<li>Add References</li>
	</ul>
	<table class="table custom-table">
		<thead>
			<tr>
				<th>Jurisdiction</th>
				<th>Application #</th>
				<th>Mailing Date</th>
				<th>Document Description</th>
				<th>Uploaded by</th>
				<th>Status</th>
				<th>Last Updated</th>
			</tr>
		</thead>
		<tbody>
			<tr class="odd">
				<td>${referenceRecord.jurisdictionCode}</td>
				<td>${referenceRecord.applicationNumber}</td>
				<td>${referenceRecord.mailingDate}</td>
				<td>${referenceRecord.documentDescription}</td>
				<td><div>${referenceRecord.uploadedBy}</div><div>${referenceRecord.uploadedOn}</div></td>
				<td>
					<c:choose>
						<c:when test="${refEntry.ocrStatus == 'DONE'}">
							<div class="action-success">Done</div>
						</c:when>
						<c:otherwise>
							<div class="action-danger">Error</div>
						</c:otherwise>
					</c:choose>
				</td>
				<td><div>${referenceRecord.updatedBy}</div><div>${referenceRecord.updatedOn}</div></td>
			</tr>
		</tbody>
	</table>
	<form id="nullReference" method="post" action="../dashboard/addNullReference">
		<input name="ocrid" id="ocrId" type="hidden" value="${ocrid}">
		<input name="id" id="notificationProcessId" type="hidden" value="${id}">
		<input  type="hidden" name="correspondence_id" id="correspondenceId" value="${correspondence_id}">	
	</form>
	<div class="form-horizontal">
		<div class="row">
			<div class="col-sm-6">
				<div class="pdf-preview-container">
					<h2>Original</h2>
					<div class="hide-pdf">- Hide OCR PDF</div>
					<div class="show-pdf">+ Show Original PDF</div>
					<div>
						<iframe style="height:800px; width:100%" src="<%=context%>/reference/dashboard/downloadFile"></iframe>
					</div>
				</div>
			</div>
			<div id="add-ref-btn" class="col-sm-6">
				<div class="reference-btns-group">
					<a id="action" class="btn btn-submit">Add References</a>
					<a href="javascript:void(0)" data-href="$('#nullReference').submit();" onclick="popupMsgForNoReference(this)"class="btn btn-cancel">No Reference Found</a>
				</div>
			</div>
		</div>
	</div>
</div>


<div id="popupMsgForNoReference" class="popup-msg">
	<div class="text-right"><a data-dismiss="modal" class="close" href="javascript:void(0)" onclick="hidepopupMsgForNoReference();">&times;</a></div>
	<div class="content">
		<p class="msg">Are you sure no references are found in this document ?</p>
	</div>
	<div class="modal-footer">
		<a type="button" class="btn btn-submit" data-dismiss="modal">Yes</a>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hidepopupMsgForNoReference();">No</button>
    </div>
</div>

<div id="popupMsgForAddReference" class="popup-msg">
	<div class="text-right"><a data-dismiss="modal" class="close" href="javascript:void(0)" onclick="hidepopupMsgForAddReference();">&times;</a></div>
	<div class="content">
		<p class="msg">All data will be lost. Do you want to proceed.</p>
	</div>
	<div class="modal-footer">
		<a type="button" class="btn btn-submit" data-dismiss="modal" href="../dashboard">Yes</a>
        <button type="button" data-dismiss="modal" class="btn btn-cancel" onclick="hidepopupMsgForAddReference();">No</button>
    </div>
</div>
<script src="<%=js%>/reference/reference-dashboard.js"></script>