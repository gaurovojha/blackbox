<script>
	$(document).ready(function() {

		$("#tblDrafts").DataTable({
			"searching" : false,
			
			"aoColumnDefs" : [ {
				'bSortable' : true,
				'aTargets' : [ 0 ]
			}, {
				'bSortable' : true,
				'aTargets' : [ 1 ]
			}, {
				'bSortable' : true,
				'aTargets' : [ 2 ]
			}, {
				'bSortable' : false,
				'aTargets' : [ 3 ]
			} ]
			
		});

	});
	
	$(".btnDeleteDraft").click(function(){
		var draftId = $(this).attr('data');
		$('#confirmationBox').find('.msg ').text('Do you want to delete the draft ?');
		showConfirmationBoxDrafts(draftId);		
	});
	$(".btnYes").click(function(){
		var rowId  = $(this).attr('data');
		var  requestUrl = $("#contextPath").val()+"/mdm/createApp/deleteDraft/"+rowId;
		window.location.href= requestUrl;
	});
	
	$(".openDraft").on("click",function(){
		var rowId  = $(this).parents('tr').attr('id');
		var  requestUrl = $("#contextPath").val()+"/mdm/createApp/viewDraft/"+rowId;
		window.location.href= requestUrl;
	})
	
	function showConfirmationBoxDrafts(draftId)
	{
		$('#confirmationBox').find('.btnYes').attr('data',draftId);
		$('#confirmationBox').show();
		$('#confirmationBox').wrap("<div class='overlay'>");
	}
	
	
	
</script>