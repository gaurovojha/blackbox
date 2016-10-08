<div id="confirmationBox" class="popup-msg">
	<div class="text-right">
		<a class="close" href="#" onclick="hideConfirmationBox()">&times;</a>
	</div>
	<div class="content">
		<p class="msg"></p>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-submit btnYes auditYes " 
			data-dismiss="modal" onclick="hideConfirmationBox();">YES</button>
		<button type="button" class="btn btn-cancel btnNo auditNo "
			onclick="hideConfirmationBox();">NO</button>
	</div>
</div>
<div id="confirmationBoxNewCorrespondence" class="popup-msg">
	<div class="text-right">
		<a class="close" href="#" onclick="hideConfirmationBoxNewCorrespondence()">&times;</a>
	</div>
	<div class="content">
		<p class="msg"></p>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-submit btnYes  newCorrespondenceYes" 
			data-dismiss="modal" onclick="hideConfirmationBoxNewCorrespondence();">YES</button>
		<button type="button" class="btn btn-cancel btnNo  newCorrespondenceNo"
			onclick="hideConfirmationBoxNewCorrespondence();">NO</button>
	</div>
</div>
	<script>
	function showConfirmationBox()
	{
		$('#confirmationBox').show();
		$('#confirmationBox').wrap("<div class='overlay'>");
	}
	function hideConfirmationBox()
	{
		$('#confirmationBox').hide();
		$('#confirmationBox').unwrap("<div class='overlay'>");
	}
	function showConfirmationBoxNewCorrespondence()
	{
		$('#confirmationBoxNewCorrespondence').show();
		$('#confirmationBoxNewCorrespondence').wrap("<div class='overlay'>");
	}
	function hideConfirmationBoxNewCorrespondence()
	{
		$('#confirmationBoxNewCorrespondence').hide();
		$('#confirmationBoxNewCorrespondence').unwrap("<div class='overlay'>");
	}
	</script>