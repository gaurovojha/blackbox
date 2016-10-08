<script>

	var tblInactiveDocuments = null;
	var tblActiveDocuments = null;

	$(document).ready(function() {
		bindControls();
		fetchDocuments();
		showMyDocuments();

	});

	$(document).tooltip({
		selector : '[data-toggle="tooltip"]'
	});

	function fetchDocuments(){
		if ($('#activeDocTab').hasClass('active')) {
			fetchActiveDocuments();
		}
		else{
			fetchInActiveDocuments();
		}
	}
	
	function showMyDocuments() {
		$('#uploadedMyDocs').on("click", function() {
			fetchActiveDocuments();
		});
	}

	function bindControls() {
		bindDateRangePicker();
		bindSearchControl();
	}

	//bind the date range picker and load the last six month data by default
	function bindDateRangePicker() {

		var initialStartDate = moment().subtract(180, 'days').startOf('days');
		var initialEndDate = moment();
		$(".daterange-picker .date").daterangepicker({
			opens : 'left',
			startDate : initialStartDate,
			endDate : initialEndDate,
			autoUpdateInput : true,
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
					if ($('#activeDocuments').parents().hasClass('active')) {
						fetchActiveDocuments();
					} else {
						fetchInActiveDocuments();
					}
				});

		$('input[name="datefilter"]').on('cancel.daterangepicker',
				function(ev, picker) {
					$(this).val('');
					if ($('#activeDocuments').parents().hasClass('active')) {
						fetchActiveDocuments();
					} else {
						fetchInActiveDocuments();
					}
				});
	}

	//bind search control
	function bindSearchControl() {
		
		$(".daterange-picker-search .date").daterangepicker({
			opens : 'left',
			autoUpdateInput : false,
			locale : {
				format : 'MMM DD, YYYY',
				cancelLabel : 'Clear'
			}
		});
		$('.daterange-picker-search i').click(function() {
			$(this).parents(".daterange-picker-search").find('input').click();
		});

		$('.daterange-picker-search .date').on('cancel.daterangepicker',
				function(ev, picker) {
					$(this).parents(".daterange-picker-search").find('input').val('');
				});
		
		$('input[name="datefilterSearch"]').on(
				'apply.daterangepicker',
				function(ev, picker) {
					$(this).val(
							picker.startDate.format('MMM DD, YYYY') + ' - '
									+ picker.endDate.format('MMM DD, YYYY'));
				});
		
		$("#gotoSearch").on("click", function(){
			if ($('#activeDocuments').parents().hasClass('active')) {
				fetchActiveDocuments();
			} else {
				fetchInActiveDocuments();
			}
		
			$(".search-dropdown").slideUp("hide");
		});		
		
		$("#hideSearch").on("click", function(){
			clearRecordsFilter();
			if ($('#activeDocuments').parents().hasClass('active')) {
				fetchActiveDocuments();
			} else {
				fetchInActiveDocuments();
			}
		
			$(".search-dropdown").slideUp("hide");
		});	
	}
	
	$(".search-control .icon-search").on("click", function() {
		$('.search-dropdown').slideToggle("show");
	});
	
/* 	$("#hideSearch").on("click", function(){
	
		$(".search-dropdown").slideUp("hide");
	}); */
	/*- ----------------------------------------------------------
			My Dashboard - Active Documents and Inactive Documents
	-------------------------------------------------------------- */
	function fetchActiveDocuments() {
		clearTables();
		$('.correspondenceTable').hide();
		$('#tblActiveDocuments').show();

		var colDef = {
			'col' : [ {
				"name" : "expand",
				"orderable" : false,
				"targets" : 0
			}, {
				"name" : "jurisdiction_code",
				"targets" : 1
			}, {
				"name" : "application_no",
				"targets" : 2
			}, {
				"name" : "mailing_date",
				"targets" : 3
			}, {
				"name" : "document_desc",
				"targets" : 4
			}, {
				"name" : "createdby",
				"targets" : 5
			}, {
				"name" : "actions",
				"orderable" : false,
				"targets" : 6
			} ],
			'defaultVal' : 'mailing_date',
			'order' : [ [ 3, 'desc' ] ],
		};

		tblActiveDocuments = new $.blackbox.correspondence.DataTable(
				'#tblActiveDocuments', '/correspondence/activeDocuments',
				recordFilters(), true, fbcallback, colDef);
	}

	function fetchInActiveDocuments() {
		clearTables();
		$('.correspondenceTable').hide();
		$('#tblInactiveDocuments').show();

		var colDef = {
			'col' : [ {
				"name" : "expand",
				"orderable" : false,
				"targets" : 0
			}, {
				"name" : "jurisdiction_code",
				"targets" : 1
			}, {
				"name" : "application_no",
				"targets" : 2
			}, {
				"name" : "mailing_date",
				"targets" : 3
			}, {
				"name" : "document_desc",
				"targets" : 4
			}, {
				"name" : "lastedited",
				"targets" : 5
			}, {
				"name" : "actions",
				"orderable" : false,
				"targets" : 6
			} ],
			'defaultVal' : 'mailing_date',
			'order' : [ [ 3, 'desc' ] ],
		};

		tblInactiveDocuments = new $.blackbox.correspondence.DataTable(
				'#tblInactiveDocuments', '/correspondence/inactiveDocuments',
				recordFilters(), true, fbcallback, colDef);
	}

	$(document).on('click', "#inactiveDocuments", function() {
		fetchInActiveDocuments();
	});

	$(document).on('click', "#activeDocuments", function() {
		fetchActiveDocuments();
	});

	$(document).on('click', "#uploadedMyDocs", function() {
		fetchActiveDocuments();
	});

	function recordFilters() {
		return {
			'iDateRange' : $('#tblDateRangeFilter').val(),
			'iMyRecords' : $('#uploadedMyDocs').is(':checked'),
			'iApplicationNumber' :$("#txtApplicationNo").val(),
			'iJurisdiction':$("#txtJurisdiction").val(),
			'iAttorneyDocketNumber':$("#txtAttorneyDocketNo").val(),
			'sFamilyId':$("#txtFamilyId").val(),
			'sDocumentDescription':$("#txtDescription").val(),
			'sUploadedBy':$("#txtUploadedBy").val(),
			'iUploadedDateRange':$("#txtUploadedOn").val()
		}
	}
	
	function clearRecordsFilter() {
		return {
			'iApplicationNumber' :$("#txtApplicationNo").val(''),
			'iJurisdiction':$("#txtJurisdiction").val(''),
			'iAttorneyDocketNumber':$("#txtAttorneyDocketNo").val(''),
			'sFamilyId':$("#txtFamilyId").val(''),
			'sDocumentDescription':$("#txtDescription").val(''),
			'sUploadedBy':$("#txtUploadedBy").val(''),
			'iUploadedDateRange':$("#txtUploadedOn").val('')
	}
	}
	function clearTables() {
		if (tblActiveDocuments != null) {
			tblActiveDocuments.destroy();
			tblActiveDocuments = null;
		}
		if (tblInactiveDocuments != null) {
			tblInactiveDocuments.destroy();
			tblInactiveDocuments = null;
		}
		$('tbodyActiveDocuments').html('');
		$('tbodyInactiveDocuments').html('');
	}
	
	function fbcallback() {
		if ($('#activeDocTab').hasClass('active')) {
			bindCorrespondences(tblActiveDocuments);	
		} else {
			bindCorrespondences(tblInactiveDocuments);
		}
		disableDroppedRecords();
	}

	function bindCorrespondences(tableName) {
		$('.expandCorrespondence')
				.on(
						'click',
						function() {
							if ($(this).parents("tr").hasClass("even")) {
								$(this).parents("tr").addClass(
										"white");
				
							}
							else if($(this).parents("tr").hasClass("odd")){
								$(this).parents("tr").addClass(
								"blue");
							}
							if ($(this).hasClass("icon-plus")) {
								$(this).parents(".has-hidden-row").addClass(
										"active");
								$(this).removeClass("icon-plus").addClass(
										"icon-minus");
								$(this).parents(".has-hidden-row").siblings(
										".hidden-row").toggle();
							} else if ($(this).hasClass("icon-minus")) {
								$(this).parents(".has-hidden-row").removeClass(
										"active");
								$(this).removeClass("icon-minus").addClass(
										"icon-plus");
								$(this).parents(".has-hidden-row").siblings(
										".hidden-row").toggle();
							}
							var tr = $(this).closest('tr');
							var row = tableName.row(tr);

							if (row.child.isShown()) {
								row.child.hide();
								tr.removeClass('shown');
							} else {
								loadChildRow(this, tableName);
							}
						});
	}

	function loadChildRow(element, tableName) {
		var data = recordFilters();
		var correspondenceId = $(element).closest('tr').find(
				'.correspondenceId').attr('data');

		if ($('#activeDocTab').hasClass('active')) {
			var target = "activeDocuments/getActiveCorrespondences/"
					+ correspondenceId;
			$.ajax({
				type : 'GET',
				data : data,
				url : target,
				contentType : "application/json",
				success : function(response) {
					var result = response.replace(/(\r\n|\n|\r)/gm, "");
					tableName.row($(element).parent().parent())
							.child($(result)).show();
				}
			});
		} else {
			var target = "inactiveDocuments/getInActiveCorrespondences/"
					+ correspondenceId;
			$.ajax({
				type : 'GET',
				data : data,
				url : target,
				contentType : "application/json",
				success : function(response) {
					var result = response.replace(/(\r\n|\n|\r)/gm, "");
					tableName.row($(element).parent().parent())
							.child($(result)).show();
				}
			});
		}
	}

	$(document)
			.on(
					'click',
					'.viewDocument',
					function() {
						var correspondenceId = $(this).attr('data');
						var target = "../correspondence/activeDocuments/viewDoc?correspondenceId="
								+ correspondenceId;
						$.ajax({
							type : "GET",
							url : target,
							success : function(data) {
								$('#myModal4').html(data).modal('show');
							}
						});
					});

	$(document)
			.on(
					'click',
					'.deleteDocument',
					function() {
						var correspondenceId = $(this).attr('data');
						showCommentsPopup();
						$("#userComment").val("");
						$('#commentsPopup').find('.msg').text('Do you want to drop the correspondence ?');
						$('#commentsPopup').find('.comments').text('Enter Comments:');
						$(".btnDeleteYes")
								.click(
										function() {
											var comments = {comments : $("#userComment").val()
											};
											var target = "../correspondence/activeDocuments/deleteCorrespondence?correspondenceId="
													+ correspondenceId;
											$.ajax({
												type : "GET",
												data : comments,
												url : target,
												success : function(data) {
													fetchActiveDocuments();
												}
											});
										});
					});

	$(document).on(
			'click',
			".export",
			function() {

				var hasActiveDocumentsActive = $('#activeDocTab').hasClass('active');
				
				if (hasActiveDocumentsActive) {
					if ($('#tblActiveDocuments').css('display') != 'none')
						exportTableToCSV.apply(this, [ 'tblActiveDocuments',
								'ActiveDocuments.xls' ]);
				} else {
					if ($('#tblInactiveDocuments').css('display') != 'none')
						exportTableToCSV.apply(this, [ 'tblInactiveDocuments',
								'InActiveDocuments.xls' ]);
				}
			});
	
	function disableDroppedRecords() {
		var disabledRows = $($.find('.disableRow')).parents('tr');
		if(disabledRows.length > 0) {
			disabledRows.each(function(){
				$(this).addClass('disabled');
			})
		}
	} 
</script>