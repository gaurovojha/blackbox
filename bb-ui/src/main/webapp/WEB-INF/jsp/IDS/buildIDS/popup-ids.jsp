<!-- Pop up to be displayed when user click on discard IDS button on IDS creation form -->
<div id="popupDiscardIDS" class="idsPopup popup-msg">
	<div class="text-right"><a class="close" href="#" onclick="hideCancelMessage()">×</a></div>
	<div class="content">
		<p class="msg">Your IDS will be discarded permanently. Do you want to continue?</p>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-submit" data-dismiss="modal" id="btnYesDiscardIDS">Yes</button>
		<button type="button" class="btn btn-cancel" id="btnCancelDiscardIDS">No</button>
	</div>
</div>

<!-- IDS Certification Statement -->
<div class="idsPopup modal custom fade modal-wide" id="popup_idsCertificationStatement" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" sourcePage="">
	Data through AJAX will be added here.
</div>

<div class="modal custom fade" id="refCountPopUP" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">References</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" id = "popupMsgs">
		</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-cancel" data-dismiss="modal">Back</button>
        <button type="button" class="btn btn-submit" id="refPopupContinue" data-dismiss="modal">Continue</button>
      </div>
    </div>
  </div>
</div>