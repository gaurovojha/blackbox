<script>
	var tblUpdateRequestView = null;
	var tblUploadRequestView = null;
	//var previousSortColumn = null;
	//var previousSortOrder = null;
	

	$(document).ready(function() {
		bindControls();
		//var intialOrder = [[2, 'asc']];
		fetchUpdateRequestRecords();
		//previousSortColumn = 2;
		//previousSortOrder = 'asc';

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
					if ($('#updateRequestLink').parents().hasClass('active')) {
						//intialOrder = [[previousSortColumn, previousSortOrder]];
						fetchUpdateRequestRecords()
					} else {
						fetchUploadRequestRecords();
					}
				});

		$('input[name="datefilter"]').on('cancel.daterangepicker',
				function(ev, picker) {
					$(this).val('');
					if ($('#updateRequestLink').parents().hasClass('active')) {
						//intialOrder = [[previousSortColumn, previousSortOrder]];
						fetchUpdateRequestRecords()
					} else {
						fetchUploadRequestRecords();
					}
				});
	}

	function fetchUpdateRequestRecords() {
		clearTables();
		if (tblUpdateRequestView == null) {
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
						"name" : "notified",
						"targets" : 4
					}, {
						"name" : "status",
						"targets" : 5
					}, {
						"name" : "action",
						"orderable": false,
						"targets" : 6
					} ],
					'defaultVal' : 'mailing_date',
					'order' : [[2, 'asc']],
					/* 'disabledCols' : [ {
						'bSortable' : false,
						'aTargets' : [ 6 ]
					} ] */
				};
			tblUpdateRequestView = new $.blackbox.correspondence.DataTable(
					'#updateReqRecords',
					'/correspondence/actionItems/updateRequest', filterData,
					true, fnCallbackUpdateRequest, colDef);
		}
	}

	function fetchUploadRequestRecords() {
		clearTables();
		if (tblUploadRequestView == null) {
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
						"name" : "notified",
						"targets" : 4
					}, {
						"name" : "action",
						"orderable": false,
						"targets" : 5
					} ],
					'defaultVal' : 'mailing_date',
					'order' : [[2, 'asc']],
					/* 'disabledCols' : [ {
						'bSortable' : false,
						'aTargets' : [ 5 ]
					} ] */
				};
			tblUploadRequestView = new $.blackbox.correspondence.DataTable(
					'#uploadReqRecords',
					'/correspondence/actionItems/uploadRequest', filterData,
					true, fnCallbackUploadRequest, colDef);
		}
	}

	function clearTables() {
		if (tblUploadRequestView != null) {
			tblUploadRequestView.destroy();
			tblUploadRequestView = null;
		}
		if (tblUpdateRequestView != null) {
			tblUpdateRequestView.destroy();
			tblUpdateRequestView = null;
		}
		$('tbodyUpdateReqRecords').html('');
		$('tbodyUploadReqRecords').html('');
	}

	$(document).on('click', "#updateRequestLink", function() {
		var initialOrder = null;
		if (tblUpdateRequestView == null) {
			initialOrder = [[2, 'asc']];
			fetchUpdateRequestRecords();
		}
	})

	$(document).on('click', "#uploadRequestLink", function() {
		if (tblUploadRequestView == null) {
			fetchUploadRequestRecords();
		}
	})

	function fnCallbackUpdateRequest() {
		changeStatusAction();
		changeUpdateRequestCount();
	}
	
	function changeUpdateRequestCount() {
		var uploadCount = $('#uploadRequestCount').text();
		var updateCount = $('#recordsTotal').val();
		$('#updateRequestCount').text(updateCount);
		$('#actionItemCount').text(parseInt(updateCount, 10) + parseInt(uploadCount, 10));
	}

	function changeStatusAction() {
		$('#myModal4').html("");
		$(".changeReviewStatus")
				.on(
						'click',
						function() {
							var correspondenceId = $(this).attr('data');
							console.log(correspondenceId);

							$('#myModal4')
									.on(
											'show.bs.modal',
											function() {
												var target = "../correspondence/reviewDocument?correspondenceId="
														+ correspondenceId;

												$.get(target, function(data) {
													$('#myModal4').html(data);
												});
											});
						});
	}

	function fnCallbackUploadRequest() {
		showRejectDocumentPopUp();
		//showUploadDocumentPopUp();
		changeUploadRequestCount();
	}
	
	function changeUploadRequestCount() {
		var updateCount = $('#updateRequestCount').text();
		var uploadCount = $('#recordsTotal').val();
		$('#uploadRequestCount').text(uploadCount);
		$('#actionItemCount').text(parseInt(updateCount, 10) + parseInt(uploadCount, 10));
	}

	function showUploadDocumentPopUp() {
		$('#UploadDocumentModalId').html("");
		$(".uploadDocument")
				.on(
						'click',
						function() {
							var correspondenceId = $(this).attr('data');
							console.log(correspondenceId);

							$('#UploadDocumentModalId')
									.on(
											'show.bs.modal',
											function() {
												var target = "../correspondence/uploadDocument?notificationId="
														+ correspondenceId;

												$.get(target, function(data) {
													$('#UploadDocumentModalId')
															.html(data);
												});
											});
						});
	}

	function showRejectDocumentPopUp() {
		$('#rejectDocument')
				.on(
						'click',
						function() {
							var correspondenceId = $(this).attr('data');
							showConfirmationBox();
							$(".btnYes")
									.click(
											function() {
												window.location.href = "../correspondence/rejectDocument?recordId="
														+ correspondenceId;
											});
						});
	}

	function showConfirmationBox() {
		$('#confirmationBox').show();
		$('#confirmationBox').wrap("<div class='overlay'>");
		$('#confirmationBox').find('.msg').text(
				'Do you want to reject the correspondence ?');
	}

	function hideConfirmationBox() {
		$('#confirmationBox').hide();
		$('#confirmationBox').unwrap("<div class='overlay'>");
	}

	$(document).on(
			'click',
			".actionItemExport",
			function() {

				var hasActiveClass = $('#updateDocTab').hasClass('active');
				if (hasActiveClass) {
					if ($('#updateReqRecords').css('display') != 'none')
						exportTableToCSV.apply(this, [ 'updateReqRecords',
								'UpdateRequest.xls' ]);
				} else {
					if ($('#uploadReqRecords').css('display') != 'none')
						exportTableToCSV.apply(this, [ 'uploadReqRecords',
								'UploadRequest.xls' ]);
				}
			});
</script>