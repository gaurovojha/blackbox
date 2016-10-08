<div class="modal custom fade modal-wide" id="updateAssignee" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Assignee Update</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal">
			<div class="form-group modalFormRecord">
				<div class="col-sm-3">
					<label class="control-label">Family Id <span class="required">*</span></label>
					<div><a href="" class="familyId" id="familyId" data-toggle="modal" data-target="#viewFamily">F281093</a></div>
				</div>
				<div class="col-sm-3">
					<label class="control-label">Jurisdiction <span class="required">*</span></label>
					<div>USA</div>
				</div>
				<div class="col-sm-3">
					<label class="control-label">Application No.<span class="required">*</span></label>
					<div>14/933,258	</div>
				</div>
				<div class="col-sm-3">
					<label class="control-label">Attorney Docket No.<span class="required">*</span></label>
					<div>65424-20345.01</div>
				</div>
            </div>
            <div class="form-group modalAssignee">
				<div class="col-sm-4">
					<label class="control-label">Assignee <span class="required">*</span></label>
					<input type="text" class="form-control" id ="txtBoxAssignee" value="">
					<div class="error hide">Assignee Not Found</div>
				</div>
				<div class="col-sm-4 hide">
					<label class="control-label">&nbsp;</label>
					<div class="form-control-static"><a href="javascript:void(0)">Create a New Assignee</a></div>
				</div>
            </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-cancel btnCancelAssignee">Cancel</button>
        <button type="button"  class="btn btn-submit btnUpdateAssignee " >Update</button>
      </div>
      <jsp:include page="../common/confirmation-box.jsp"/>
    </div>
  </div>
</div>
