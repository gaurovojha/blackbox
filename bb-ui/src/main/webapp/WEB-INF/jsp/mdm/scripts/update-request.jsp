<script>
	$(document).ready(function() {
		
		applicationRecordsTable = null;
		assigneeRecordsTable = null;
		familyRecordsTable = null;
		fetchApplicationRecords();
	
	
	
	});
	$('#applicationReqLink').on('click',function()
			{
				if(applicationRecordsTable==null)
					fetchApplicationRecords();
			});
	$('#assigneeReqLink').on('click',function(){
		
		{
			if(assigneeRecordsTable==null)
			fetchAssigneeRecords();
		}
	});
	
	$('#familyReqLink').on('click',function(){
		
		{
			if(familyRecordsTable==null)
			fetchFamilyRecords();
		}
	});
	
	/*- ----------------------------------------------------------
	 Update Request - Application Records
	 -------------------------------------------------------------- */
	function fetchApplicationRecords() {
		applicationRecordsTable = new $.blackbox.mdm.DataTable('#tblUpdateApplcation','/mdm/updateApp/records',null,true);
	}
	
	function fetchAssigneeRecords() {
		assigneeRecordsTable = new $.blackbox.mdm.DataTable('#tblUpdateAssignee','/mdm/updateAssignee/records',null,true);
	}
	function fetchFamilyRecords() {
		familyRecordsTable = new $.blackbox.mdm.DataTable('#tblUpdateFamily','/mdm/updateFamily/records',null,true);
	}
	
</script>