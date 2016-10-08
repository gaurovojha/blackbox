<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="mdm" uri="/WEB-INF/tld/bbxTagLib"%>

<c:set var="canAddNewApp" value="${not editApplication and mdm:validCombination('FAMILY', jurisdiction, applicationType)}" />
<div id="tempBtnControls" class="form-group form-footer ${canAddNewApp ? 'custom' : ''}">
	<c:if test="${canAddNewApp}">
		<div id='linkAddNewApp'>
			<a href="javascript:void(0);" id="addNewApp" class="add-application-btn">+ Add New Application to Existing Family</a>
		</div>
	</c:if>
	<!-- <div id="applicationConfirmationBox" class="applicationConfirmationBox popup-msg">
					<div class="text-right">
						<a class="close" href="#" onclick="hideConfirmationBox()">&times;</a>
				</div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="submit" class="btn btn-submit" >Yes</button>
						<button type="button" class="btn btn-cancel"
							onclick="hideConfirmationBox();">NO</button>
        </div>
	</div>  -->
	
	
	<!-- Pop up to be displayed when user click on cancel button on create application form -->
	
	<div id="cancelPopup" class="popup-msg">
						<div class="text-right"> <a class="close" href="#" onclick="hideCancelMessage()">×</a></div>
						<div class="content">
							<p class="msg">Your changes will not be saved.Do you want to proceed.</p>
						</div>
						<div class="modal-footer">
                			<button type="button" class="btn btn-submit" data-dismiss="modal" onclick="closePage();hideCancelMessage();">YES</button>
                			<button type="button" class="btn btn-cancel" onclick="hideCancelMessage();">NO</button>
        				</div>
					</div>
	<div>
		<button type="button" id="btnSaveChanges" class="btn btn-submit">Submit</button>
		<button type="button" id="btnCancelApp" class="btn btn-cancel" data-dismiss="modal" onclick="showCancelMessage()">Cancel</button>
	</div>
</div>