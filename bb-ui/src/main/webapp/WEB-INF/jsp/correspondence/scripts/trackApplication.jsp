<script>
var tblMyRequestView = null;
var tblAllRequestView = null;


$(document).ready(function() {
	bindControls();
	fetchMyApplications();
});

function bindControls() {
	var initialStartDate = moment().subtract(180, 'days').startOf('days');
	var initialEndDate = moment();
	//var initialOrder = null;
	$(".daterange-picker .date").daterangepicker({
		opens:'left',
    	startDate : initialStartDate,
    	endDate :initialEndDate,
    	 autoUpdateInput: true,
		locale : {
			format : 'MMM DD, YYYY',
			cancelLabel : 'Clear'
		}
	});
	$('.daterange-picker i').click(function() {
		$(this).parents(".daterange-picker").find('input').click();
	});

	 $('.daterange-picker .date').on('cancel.daterangepicker',
			function(ev, picker) {
				$(this).parents(".daterange-picker").find('input').val('');
			}); 

	$('input[name="datefilter"]').on(
			'apply.daterangepicker',
			function(ev, picker) {
				$(this).val(
						picker.startDate.format('MMM DD, YYYY') + ' - '
								+ picker.endDate.format('MMM DD, YYYY'));
				if ($('#myRequestLink').parents().hasClass('active')) {
					//intialOrder = [[previousSortColumn, previousSortOrder]];
					fetchMyApplications();
				} else {
					fetchAllApplications();
				}
			});

	$('input[name="datefilter"]').on('cancel.daterangepicker',
			function(ev, picker) {
				$(this).val('');
				if ($('#myRequestLink').parents().hasClass('active')) {
					//intialOrder = [[previousSortColumn, previousSortOrder]];
					fetchMyApplications()
				} else {
					fetchAllApplications();
				}
			});
}

function fetchMyApplications() {
	clearTables();
	if (tblMyRequestView == null) {
		var filterData = {
			'iDateRange' : $('#dateRangeReq').val(),
			'iMyRecords' : true,
		}
		var colDef = {
				'col' : [ {
					"name" : "jurisdiction_code",
					"targets" : 0
				},{
					"name" : "application_no",
					"targets" : 1
				}, {
					"name" : "mailing_date",
					"targets" : 2
				}, {
					"name" : "document_desc",
					"targets" : 3
				}, {
					"name" : "requester",
					"targets" : 4
				}, {
					"name" : "approver",
					"orderable": false,
					"targets" : 5
				}, {
					"name" : "status",
					"targets" : 6
				} ],
				'defaultVal' : 'mailing_date',
				'order' : [[2, 'desc']],
			};
		tblMyRequestView = new $.blackbox.correspondence.DataTable(
				'#myRequestRecords',
				'/correspondence/trackApplication/myRequest', filterData,
				true, fnCallback, colDef);
	}
}

$(document).on('click', "#allRequestLink", function() {
	if (tblAllRequestView == null) {
		fetchAllApplications();
	}
})

$(document).on('click', "#myRequestLink", function() {
	if (tblMyRequestView == null) {
		fetchMyApplications();
	}
})

function fetchAllApplications() {
	clearTables();
	if (tblAllRequestView == null) {
		var filterData = {
			'iDateRange' : $('#dateRangeReq').val(),
			'iMyRecords' : true,
		}
		var colDef = {
				'col' : [ {
					"name" : "jurisdiction_code",
					"targets" : 0
				},{
					"name" : "application_no",
					"targets" : 1
				}, {
					"name" : "mailing_date",
					"targets" : 2
				}, {
					"name" : "document_desc",
					"targets" : 3
				}, {
					"name" : "requester",
					"targets" : 4
				}, {
					"name" : "approver",
					"orderable": false,
					"targets" : 5
				}, {
					"name" : "status",
					"targets" : 6
				} ],
				'defaultVal' : 'mailing_date',
				'order' : [[2, 'desc']],
			};
		tblAllRequestView = new $.blackbox.correspondence.DataTable(
				'#allRequestRecords',
				'/correspondence/trackApplication/allRequest', filterData,
				true, fnCallback, colDef);
	}
}

function fnCallback() {
	
}

function clearTables() {
	if (tblMyRequestView != null) {
		tblMyRequestView.destroy();
		tblMyRequestView = null;
	}
	if (tblAllRequestView != null) {
		tblAllRequestView.destroy();
		tblAllRequestView = null;
	}
	$('tbody').html('');
}

$(document).on(
		'click',
		".trackApplicationExport",
		function() {

			var hasActiveClass = $('#updateDocTab').hasClass('active');
			if (hasActiveClass) {
				if ($('#myRequestRecords').css('display') != 'none')
					exportTableToCSV.apply(this, [ 'myRequestRecords',
							'MyTrackApplications.xls' ]);
			} else {
				if ($('#allRequestRecords').css('display') != 'none')
					exportTableToCSV.apply(this, [ 'allRequestRecords',
							'AllTrackApplications.xls' ]);
			}
		});
</script>