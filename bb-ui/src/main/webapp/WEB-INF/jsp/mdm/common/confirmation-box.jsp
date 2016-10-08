<div id="confirmationBox" class="popup-msg">
		<div class="text-right"><a class="close" href="#" onclick="hideConfirmationBox()">&times;</a></div>
		<div class="content">
			<p class="msg"></p>
		</div>
		<div class="modal-footer">
                <button type="button" class="btn btn-submit btnYes" data-dismiss="modal" onclick="hideConfirmationBox();">YES</button>
                <button type="button" class="btn btn-cancel" onclick="hideConfirmationBox();">NO</button>
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
	</script>