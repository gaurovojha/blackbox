<script>
	var tblActiveRecord = null;
	var tblInactiveRecord = null;
	var tblFamilyView = null;
	var tblInactiveApplicationView = null;
	var tblInactiveFamilyView = null;

	$(document).ready(function() {

		bindControls();
		if ($('#activeRecordsLink').parents().hasClass('active')) {
			fetchActiveRecords();
		} else {
			fetchInActiveRecords();
		}
		showMyRecords();
		highlightNavigation();
	});

	function bindControls() {
		//$('.dtPicker').datepicker();
		$(".daterange-picker .date").daterangepicker({
			opens : 'left',
			autoUpdateInput : false,
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
					if ($('#activeRecordsLink').parents().hasClass('active')) {
						fetchActiveRecords();
					} else {
						fetchInActiveRecords();
					}
				});
		
		$('input[name="datefilterSearch"]').on(
				'apply.daterangepicker',
				function(ev, picker) {
					$(this).val(
							picker.startDate.format('MMM DD, YYYY') + ' - '
									+ picker.endDate.format('MMM DD, YYYY'));
				});
		$('input[name="datefilter"]').on('cancel.daterangepicker',
				function(ev, picker) {
					$(this).val('');
					if ($('#activeRecordsLink').parents().hasClass('active')) {
						fetchActiveRecords();
					} else {
						fetchInActiveRecords();
					}
				});
		
		$("#gotoSearch").on("click", function(){
			if ($('#activeRecordsLink').parents().hasClass('active')) {
				fetchActiveRecords();
			} else {
				fetchInActiveRecords();
			}
		
			$(".search-dropdown").slideUp("hide");
		});		
	}

	/*- ----------------------------------------------------------
			My Dashboard - Records View - Switch Button
	-------------------------------------------------------------- */
	$(document).on('change', '#switchRecordsView', function() {
		if ($(this).is(':checked')) {
			if ($('#tabActiveRecords').hasClass('active')) {
				disableExpandedView($('.mdmTable').find('.icon-minus'), tblActiveRecord);
				showApplicationView(tblActiveRecord);
			} else {
				disableExpandedView($('.mdmTable').find('.icon-minus'), tblInactiveRecord);
				showApplicationView(tblInactiveRecord);
			}
		} else {
			if ($('#tabActiveRecords').hasClass('active')) {
				showFamilyView(tblActiveRecord);
			} else {
				showFamilyView(tblInactiveRecord);
			}
		}
	});

	function showSpecificView() {
		if ($('#switchRecordsView').is(':checked')) {
			if ($('#tabActiveRecords').hasClass('active')) {
				showApplicationView(tblActiveRecord);
			} else {
				showApplicationView(tblInactiveRecord);
			}
		}/* else {
											if($('#tabActiveRecords').hasClass('active')) {
												showFamilyView(tblActiveRecord);	
											}else {
												showFamilyView(tblInactiveRecord);
											}
										} */
	}

	function showApplicationView(tableName) {
		var column1 = tableName.column(0);
		var column2 = tableName.column(1);
		column1.visible(false);
		column2.visible(false);
	}

	function showFamilyView(tableName) {
		var column1 = tableName.column(0);
		var column2 = tableName.column(1);
		column1.visible(true);
		column2.visible(true);
	}

	function format(d) {
		// `d` is the original data object for the row
		return '<table class="table inner-table"><tr class="hidden-row"><td></td><td></td> <td>US</td> <td>123456781234567</td> <td>AK34567891-345678</td> <td>Sept 10 , 2014</td> <td>Microsoft</td> <td>CIP</td>'
				+ '<td>John Mayer<br>Oct 10, 2014</td> <td> <div class="action-btns-grid"><a href="javascript:void(0)"><i><img src="images/svg/edit.svg" class="icon16x"></i> Edit</a> '
				+ '<div class="dropdown grid-dropdown"> <a id="drop4" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><img src="images/svg/change-status.svg" class="icon20x"> Change Status <span class="caret"></span> </a>'
				+ '<ul id="menu1" class="dropdown-menu" aria-labelledby="drop4"> <li><a href="#">Transfer Record</a></li>	<li><a href="#">Allowed to Abandon</a></li>	<li><a href="#">Switch Off</a></li>'
				+ '<li><a href="#">Delete</a></li></ul></div>	</div></td></tr></table>';
	}

	function bindFamilyClick(tableName) {
		// use delegated for the newly created rows
		$('.expandFamily')
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
								// This row is already open - close it
								row.child.hide();
								tr.removeClass('shown');
							} else {
								// Open this row
								/* row.child(format(row.data())).show();
								tr.addClass('shown'); */
								if ($('#tabActiveRecords').hasClass('active')) {
									getFamilyMembers(this, tableName, "ACTIVE");
								} else {
									getFamilyMembers(this, tableName, "INACTIVE");
								}
							}
						});
	}

	function getFamilyMembers(element,tableName, view) {
		var famId = $(element).closest('tr').find('.familyId').html();
		var appId = $(element).closest('tr').find('.appId').attr('data');
		var target = "activeRecords/allFamilyDetails/" + famId + "/" + appId;
		var data = $.extend(recordFilters(),{"familyId" : famId},{"applicationId" : appId},{"view" : view});
		$.ajax({
			type : "POST",
			url : target,
			data: data,
			success : function(response) {
				//var result = response.replace(/(\r\n|\n|\r)/gm, "");
				//result = result.trim("\t");
				//result= result.replace(/>\s+</g,'><'); 
				result = response.replace(/>\s+</g,'><');
				tableName.row($(element).parent().parent()).child(
						$(result)).show();
				changeToStatusTransfer();
				changeToStatusAabndon();
				changeToStatusActivate();
		        changeToStatusDeactivate();
		        changeToStatusDelete();
			}
		});
	}

	/*- ----------------------------------------------------------
			My Dashboard - Active Records - Application View
	-------------------------------------------------------------- */
	function fetchActiveRecords() {
		clearTables();
		$('.mdmTable').hide();
		$('#tblActiveRecords').show();
		var colDef = {
			'col' : [ {
				"name" : "",
				'bSortable' : false,
				"targets" : 0
			},{
				"name" : "familyid",
				"targets" : 1
			}, {
				"name" : "jurisdiction",
				"targets" : 2
			}, {
				"name" : "application",
				"targets" : 3
			}, {
				"name" : "attorneydocket",
				"targets" : 4
			}, {
				"name" : "filingdate",
				"targets" : 5
			}, {
				"name" : "assignee",
				"targets" : 6
			}, {
				"name" : "applicationtype",
				"targets" : 7
			}, {
				"name" : "createdby",
				"targets" : 8
			},
			{
				"name" : "actions",
				"targets" : 9,
				"bSortable":false
			} ],
			'defaultVal' : 'filingdate',
			'order' : [5, 'desc'],
		};
		tblActiveRecord = new $.blackbox.mdm.DataTable('#tblActiveRecords',
				'/mdm/activeRecords/view', recordFilters(), true,
				fnCallbackFamilyView, colDef);
	}

	function fetchInActiveRecords() {
		clearTables();
		$('.mdmTable').hide();
		$('#tblInActiveRecords').show();
		var colDef = {
			'col' : [{
				"name" : "",
				'bSortable' : false,
				"targets" : 0
			},{
				"name" : "familyid",
				"targets" : 1
			}, {
				"name" : "jurisdiction",
				"targets" : 2
			}, {
				"name" : "application",
				"targets" : 3
			}, {
				"name" : "attorneydocket",
				"targets" : 4
			}, {
				"name" : "filingdate",
				"targets" : 5
			}, {
				"name" : "assignee",
				"targets" : 6
			}, {
				"name" : "applicationtype",
				"targets" : 7
			}, {
				"name" : "lastedited",
				"targets" : 8
			},{
				"name" : "actions",
				'bSortable' : false,
				"targets" : 9
			}],
			'defaultVal' : 'filingdate',
			'order' : [5, 'desc'],
		};
		tblInactiveRecord = new $.blackbox.mdm.DataTable('#tblInActiveRecords',
				'/mdm/inactiveRecords/view', recordFilters(), true,
				fnCallbackFamilyView, colDef);
	}

	function fnCallbackFamilyView() {
		//bindControls();
		if ($('#tabActiveRecords').hasClass('active')) {
			showFamilyView(tblActiveRecord);
			bindFamilyClick(tblActiveRecord);
			viewFamily();
			} else {
				showFamilyView(tblInactiveRecord);
				bindFamilyClick(tblInactiveRecord);
				viewFamily();
			}
		
		showSpecificView();
		//showMyRecords();
		changeToStatusTransfer();
		changeToStatusAabndon();
		changeToStatusActivate();
        changeToStatusDeactivate();
        changeToStatusDelete();
        disableDroppedRecords();
        
        
      //tooltip
  	  $(function () {
  	     $('[data-toggle="tooltip"]').tooltip();
  	     $('.has-error .form-control').tooltip({trigger: 'manual'}).tooltip('show');
  	     $('.has-error .form-control').on('focus',function(){$(this).tooltip('destroy');});
  	  })
		
	}

	function recordFilters() {
		return {
			'iDateRange' : $('#tblDateRangeFilter').val(),
			'iMyRecords' : $('#myRecordsOnly').is(':checked'),
			'iApplicationNumber' :$("#txtApplicationNo").val(),
			'iJurisdiction':$("#txtJurisdiction").val(),
			'iAttorneyDocketNumber':$("#txtAttorneyDocketNo").val(),
			'sFamilyId':$("#txtFamilyId").val(),
			'sDocumentDescription':$("#txtDescription").val(),
			'sUploadedBy':$("#txtUploadedBy").val(),
			'iUploadedDateRange':$("#txtUploadedOn").val()
		}
	}

	function clearTables() {
		if (tblActiveRecord != null) {
			tblActiveRecord.destroy();
			tblActiveRecord = null;
		}
		if (tblFamilyView != null) {
			tblFamilyView.destroy();
			tblFamilyView = null;
		}
		if (tblInactiveRecord != null) {
			tblInactiveRecord.destroy();
			tblInactiveRecord = null;
		}
		if (tblInactiveFamilyView != null) {
			tblInactiveFamilyView.destroy();
			tblInactiveFamilyView = null;
		}
		$('tbody').html('');
	}

	$(".search-control .icon-search").on("click", function() {
		$('.search-dropdown').slideToggle("show");
	});
	
	$("#hideSearch").on("click", function(){
	
		$(".search-dropdown").slideUp("hide");
	});

	function showMyRecords() {
		$('#myRecordsOnly').on("click", function() {
			fetchActiveRecords();
			/*if($('#activeRecordsLink').parents().hasClass('active')) {
				fetchActiveRecords();
			}/* else {
				fetchInActiveRecords();
			} */
		});
	}

	function changeToStatusTransfer() {
		$('#transferRecord').html("");
		$(".statusTransfer").on('click', function() {
			var recordId = $(this).attr('data');

			$('#transferRecord').on('show.bs.modal', function() {
				var target = "../mdm/application/transfer/record?recordId=" + recordId;

				$.get(target, function(data) {
					$('#transferRecord').html(data);
				});
			});
		});
	}

	function changeToStatusAabndon() {
		$('#abandonRecord').html("");
		$(".statusAbandon").on('click', function() {
			var recordId = $(this).attr('data');

			$('#abandonRecord').on('show.bs.modal', function() {
				var target = "../mdm/application/abandon/record?recordId=" + recordId;

				$.get(target, function(data) {
					$('#abandonRecord').html(data);
				});
			});
		});
	}

	function changeToStatusDelete() {
		$('#deleteRecord').html("");
		$(".statusDelete").off('click').on('click', function() {
			var recordId = $(this).attr('data-id');
			var familyId = $(this).attr('data-familyId');
			$('#deleteRecord').off('show.bs.modal').on('show.bs.modal', function() {
				var target = "../mdm/application/delete/record?recordId=" + recordId + "&familyId=" + familyId;

				$.get(target, function(data) {
					$('#deleteRecord').html(data);
				});
			});
		});
	}

	function changeToStatusDeactivate() {
		$('#deactivateRecord').html("");
		$(".statusDeactivate").off('click').on('click', function() {
			var recordId = $(this).attr('data');

			$('#deactivateRecord').off('show.bs.modal').on('show.bs.modal', function() {
				var target = "../mdm/application/deactivate/record?recordId=" + recordId;

				$.get(target, function(data) {
					$('#deactivateRecord').html(data);
				});
			});
		})
	}

	function changeToStatusActivate(){
		$('#reactivateRecord').html("");
		$(".statusActivate").on('click', function() {
			var recordId = $(this).attr('data');

			$('#reactivateRecord').on('show.bs.modal', function() {
				var target = "../mdm/application/reactivate/record?recordId=" + recordId;

				$.get(target, function(data) {
					$('#reactivateRecord').html(data);
				});
			});
		});
		
	}
	
	function viewFamily() {
		$('.familyId').on('click', function() {
			var familyId = $(this).attr('data');

				var target = "viewFamily/" + familyId;

				$.get(target, function(data) {
					//alert(data);
					$('#viewFamilyBody').html(data);
					//$('#viewFamily').modal('toggle');
				});

			/* $('#transferRecord').on('show.bs.modal', function() {
				var target = "../mdm/application/transfer/record?recordId=" + recordId;

				$.get(target, function(data) {
					$('#transferRecord').html(data);
				});
			}); */
		});
	}

	function showConfirmationBox() {
		$('.confirmationBox').find('.msg ').text(
				'Are you sure you want to switch off this record  ?');
		$('.confirmationBox').show();
		$('.confirmationBox').wrap("<div class='overlay'>");
	}

	function hideConfirmationBox() {
		$('.confirmationBox').hide();
		$('.confirmationBox').unwrap("<div class='overlay'>");
	}


	$('#activeRecordsLink').on('click', function() {
			clearSearch();
		if (tblActiveRecord == null) {
			fetchActiveRecords();
		}
	})

	$('#inactiveRecordsLink').on('click', function() {
			clearSearch();
		if (tblInactiveRecord == null) {
			fetchInActiveRecords();
		}
	})


	function disableExpandedView(element, tableName) {
		$(element).each(function(){
			if ($(this).hasClass("icon-minus")) {
				$(this).parents(".has-hidden-row").removeClass("active");
				$(this).removeClass("icon-minus").addClass("icon-plus");
				$(this).parents(".has-hidden-row").siblings(".hidden-row")
						.toggle();
			}
			var tr = $(this).closest('tr');
			var row = tableName.row(tr);

			if (row.child.isShown()) {
				// This row is already open - close it
				row.child.hide();
				tr.removeClass('shown');
			}
		})
	}
	function highlightNavigation() {
		$('.navButton').on('click', function(){
			if('#navDashboard') {
				
			} else if('#navAcionItems') {
				
			} else if('#navDrafts'){
				
			}
		}) 
			
	}
	
	function disableDroppedRecords() {
		var disabledRows = $($.find('.disableRow')).parents('tr');
		if(disabledRows.length > 0) {
			disabledRows.each(function(){
				$(this).addClass('disabled');
			})
		}
	} 
	
	function clearSearch()
	{
		$(".searchVariable").val('');
	}
</script>
