<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%
	String context = request.getContextPath();
	String images = context+"/assets/images";
%>

<ul class="breadcrumb">
	<li><a href="#">My Notifications</a></li>
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
				<td><div class="action-success">${referenceRecord.ocrStatus}</div></td>
				<td><div>${referenceRecord.updatedBy}</div><div>${referenceRecord.updatedOn}</div></td>
			</tr>
	</tbody>
</table>
<div class="form-horizontal">
	<div class="row">
		<div class="col-sm-6">
			<div class="pdf-preview-container single">
				<h2>OCR'ed PDF</h2>
				<img src="images/pdf-reader.png" class="img-responsive">
			</div>
		</div>
		<div class="col-sm-6">
			<div class="switch-control reference text-right">
				<label class="switch">
			      <input type="checkbox" class="switch-input" checked="checked">
			      <span class="switch-label" data-on="Automated: 24 hrs Turnaround" data-off="I Will do it myself"></span>
			      <span class="switch-handle"></span>
    			</label>
			</div>
		</div>
        <div class="col-sm-6">
            <div class="form-group">
            <h4>Add Reference Entries</h4>
                <div class="col-sm-6">
                    <select class="form-control" id="referenceEntries">
                        <option>Select a Reference Type</option>
                        <option value="us">US</option>
                        <option value="foreign">Foreign</option>
                        <option value="npl">NPL</option>
                    </select>
                </div>
                <div class="col-sm-6" id="nplSelect">
                    <input type="checkbox" id="unpublishedApp">
                    <label class="control-label" for="unpublishedApp">US Unpublished Application</label>
                </div>
            </div>
            <div id="usData">
                <jsp:include page="create-reference-pus.jsp"></jsp:include>
            </div>
            <div id="foreignData">
                <jsp:include page="create-reference-fp.jsp"></jsp:include>
            </div>
            <div id="nplData">
                <jsp:include page="create-reference-npl.jsp"></jsp:include>
            </div>
            
            <div class="form-footer">
		        <div class="col-sm-12">
		            <div class="form-group  text-left">
		                <button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
		                <button type="button" class="btn btn-submit">Add</button>
		            </div>
		        </div>
    		</div>
        </div>
	</div>

</div>