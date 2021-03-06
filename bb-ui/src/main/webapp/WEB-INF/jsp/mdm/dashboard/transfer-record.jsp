<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="pathImg" value="${contextPath}/assets/images/svg" />

<div class="modal-dialog" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">Transfer Record</h4>
		</div>
		<div class="modal-body">
			<form:form class="form-horizontal" method="get"
				modelAttribute="mdmRecordStatus"
				action="${contextPath}/mdm/application/transfer/recordStatus">
				<div class="tab-info-text">
					<span><i><img src="${pathImg}/info.svg" class="icon16x"></i></span>
					<p>Status change to ‘Transfer Record’ will indicate that this
						application / family has been transferred to another law firm.
						Once this status is updated, system will:</p>
					<ol>
						<li>Disable IDS preparation for this application / family</li>
						<li>Continue to Update new correspondences as they come until
							customer number is updated for the new law firm.</li>
						<li>Send an email notification if the customer number change
							does not happen within 30 days of status change</li>
					</ol>
					<p>In case you want to achieve a different objective from this
						status change, please contact your system administrator.</p>
				</div>
				<div class="form-group">
					<div class="col-sm-3">
						<label class="control-label">Family ID <span
							class="required">*</span></label>
						<div class="form-control-static">${mdmRecord.familyId}</div>
					</div>
					<input type="hidden" name="familyId" value="${mdmRecord.familyId}" />
					<input type="hidden" name="recordId" value="${mdmRecord.dbId}" />
					<div class="col-sm-3">
						<label class="control-label">Jurisdiction <span
							class="required">*</span></label>
						<div class="form-control-static">${mdmRecord.jurisdiction}</div>
					</div>
					<div class="col-sm-3">
						<label class="control-label">Application No. <span
							class="required">*</span></label>
						<div class="form-control-static">${mdmRecord.applicationNumber}</div>
					</div>
					<div class="col-sm-3">
						<label class="control-label">Attorney Docket No.<span
							class="required">*</span></label>
						<div class="form-control-static">${mdmRecord.attorneyDocket}</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-4">
						<label class="control-label">Assignee <span
							class="required">*</span></label>
						<div class="form-control-static">${mdmRecord.assignee}</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<input type="checkbox" name="familyMembers" id="familyMembers"
							checked="true" /><label class="control-label"
							for="familyMembers">Apply same status to all Family
							members</label>
					</div>
				</div>
				<div class="divider"></div>
				<div class="form-group">
					<div class="col-sm-6">
						<label class="control-label">Comments (If any)</label>
						<textarea name="userComment" id="userComment" class="form-control"
							rows="4" cols="4"></textarea>
					</div>
					</div>
				<div id="transferConfirmationBox" class="transferConfirmationBox popup-msg">
					<div class="text-right">
						<a class="close" href="#" onclick="hideConfirmationBox()">&times;</a>
				</div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="submit" class="btn btn-submit">Yes</button>
						<button type="button" class="btn btn-cancel"
							onclick="hideConfirmationBox();">NO</button>
        </div>
	</div> 
			</form:form>
		    <div class="modal-footer">
					<button type="button" class="btn btn-cancel" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-submit"
					onclick="showConfirmationBox();">Transfer</button>
			</div>
		</div>
	</div>
</div>

<script>
	function showConfirmationBox() {
		$('.transferConfirmationBox').find('.msg ').text(
				'Are you sure want to transfer this record ?');
		$('.transferConfirmationBox').show();
		$('.transferConfirmationBox').wrap("<div class='overlay'>");
	}

	function hideConfirmationBox() {
		$('.transferConfirmationBox').hide();
		$('.transferConfirmationBox').unwrap("<div class='overlay'>");
	}
</script>

