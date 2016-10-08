
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="pathImg" value="${contextPath}/assets/images/svg" />

<input type="hidden" id="familyMembers"
	value="${mdmRecord.familyMembers}" />
	
<div class="modal-dialog" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">Deactivate Record</h4>
		</div>
		<div class="modal-body">
			<form:form class="form-horizontal" method="get"
				modelAttribute="mdmRecordStatus"
				action="${contextPath}/mdm/application/delete/recordStatus">
				<c:choose>
					<c:when test="${mdmRecord.familyMembers}">
						<div class="tab-info-text">
							<p>Active Cases are associated to this family, do you want to
								delete the all members</p>
						</div>
					</c:when>
				</c:choose>
				<div class="tab-info-text">
					<p>Total No of affected transactions are:
						${mdmRecord.countAffectedTransaction}</p>
				</div>
				<div class="tab-info-text">
					<span><i><img src="${pathImg}/info.svg" class="icon16x"></i></span>
					<p>TBD</p>
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
				<div class="divider"></div>
				<div class="form-group">
					<div class="col-sm-6">
						<label class="control-label">Comments (If any)</label>
						<textarea name="userComment" id="userComment" class="form-control" rows="4" cols="4"></textarea>
					</div>
				</div>
				<div id="deleteConfirmationBox"
					class="deleteConfirmationBox popup-msg">
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
					<c:choose>
					<c:when test="${mdmRecord.familyMembers}">
						<button type="button" class="btn btn-submit"
					onclick="showConfirmationBox();">Delete All</button>
					</c:when>
					<c:otherwise>
					<button type="button" class="btn btn-submit"
					onclick="showConfirmationBox();">Delete</button>
					</c:otherwise>
				</c:choose>
				
			</div>
		</div>
	</div>
</div>

<script>
	function showConfirmationBox() {
		$('.deleteConfirmationBox').find('.msg ').text("");
		if($('#familyMembers').val() === "true")
		{
           $('.deleteConfirmationBox').find('.msg ').text(
			'Are you sure want to delete all records ?');	
		}else {
			$('.deleteConfirmationBox').find('.msg ').text(
			'Are you sure want to delete this record ?');
		}
		
		$('.deleteConfirmationBox').show();
		$('.deleteConfirmationBox').wrap("<div class='overlay'>");
	}

	function hideConfirmationBox() {
		$('.deleteConfirmationBox').hide();
		$('.deleteConfirmationBox').unwrap("<div class='overlay'>");
	}
</script>
