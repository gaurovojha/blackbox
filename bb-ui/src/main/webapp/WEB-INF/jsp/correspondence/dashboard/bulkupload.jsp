<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <div class="modal custom fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">Bulk Upload</h4>
	      </div>
	      <div class="modal-body">
		        <form class="form-horizontal">
	    			<div class="form-group">
						<div class="col-sm-12">
							<label class="control-label">Select Files to Upload</label>
							<div class="input-group">
								<input id="uploadBulkiFile" disabled="disabled" placeholder="Choose File" class="form-control" />
								<div class="fileUpload btn btn-blue input-group-btn">
								    <span>Browse</span>
									<input type="file" name="myfile" id="uploadZipFile" class="upload" accept="application/zip" >
								</div>
							</div>
						</div>
		            </div>
		            <div class="text-center files-selected-txt"></div>
	    		</form>
	      </div>
	        <div class="BulkFileUploadError has-error"><p id="bulkFileUploadErrorMsg" class="data error"></p> </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
	        <button type="button" id="bulkUploadBtn" class="btn btn-submit" >Upload Now</button>
	      </div>
	    </div>
	  </div>
	  </div>
<%-- <%
	String context = request.getContextPath();
	String js = context+"/assets/js/correspondence";
%>
	<script type="text/javascript" src="<%=js%>/correspondence.js"></script> --%>