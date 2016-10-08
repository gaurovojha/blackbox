<script>

$(".export").on('click',function() {
	
	var hasClass = $('#tabActiveRecords').hasClass('active');
	if(hasClass) {
		if($('#switchRecordsView').is(':checked'))
			$.blackbox.util.exportTableToCSV.apply(this, ['tblActiveRecords', 'Active_Records_Application_View.xls']);
		else
			$.blackbox.util.exportTableToCSV.apply(this, ['tblActiveRecords', 'Active_Records_Family_View.xls']);
	}
	 hasClass = false;
	hasClass = $('#tabInactiveRecords').hasClass('active');
	if(hasClass) {
		if($('#switchRecordsView').is(':checked'))
			$.blackbox.util.exportTableToCSV.apply(this,['tblInActiveRecords', 'Inactive_Records_Application_View.xls']);
		else
			$.blackbox.util.exportTableToCSV.apply(this,['tblInActiveRecords', 'Inactive_Records_Family_View.xls']);
	} 
});
</script>
