<script>
	tblInitiateIDSAllRecords = null;
	tblInitiateIDSUrgentRecords = null;
	tblPendingIDSAllRecords = null;
	tblPendingResponseAllRecords = null;
	tblFilingReadyRecords = null;
	tblFilingInProgressRecords = null;
	tblValidateRefStatusRecords = null;
	tblUploadManualRecords = null;
	tblIDSFilingPackageRecords = null;
	tbl1449PendingIDSRecords = null;
	var selectedRow = null;

	$(document).ready(function() {

		findActiveTab();
		
	});

	function findActiveTab() {
		var activeTab = $('#tabGroupActionItems').find('.active').index();

		switch (activeTab) {
		case 0: {
			viewInitiateIDSRecords();
			break;
		}
		}
	}
	function viewInitiateIDSRecords() {
		if ($('#lnkUrgentIDS').parent().hasClass('active'))
			fetchInitiateIDSUrgentRecords();
		if ($('#lnkAllIDS').parent().hasClass('active'))
			fetchInitiateIDSAllRecords();
	}
	$('#lnkAllIDS').on('click', function() {
		//if (tblInitiateIDSAllRecords == null) {
			fetchInitiateIDSAllRecords();
		//}
		
	})

	$('#lnkUrgentIDS').on('click', function() {
		//if (tblInitiateIDSUrgentRecords == null) {
			fetchInitiateIDSUrgentRecords();
		//}
		
	})

	$('#idsPendingApproval').on('click', function() {
		if (tblPendingIDSAllRecords == null) {
			fetchPendingResponseRecords();
		}
	})

	$('#lnkPendingIDS').on('click', function() {
		if (tblPendingIDSAllRecords == null) {
			fetchPendingIDSRecords();
		}
	})

	$('#lnkPendingResponse').on('click', function() {
		if (tblPendingResponseAllRecords == null) {
			fetchPendingResponseRecords();
		}
	})

	$('#idsFilingReady').on('click', function() {
		if (tblFilingReadyRecords == null) {
			fetchFilingReadyRecords();
		}
	})
	
	$('#lnkFilingInProgressIDS').on('click', function() {
		if (tblFilingInProgressRecords == null) {
			fetchFilingInProgressRecords();
		}
	})
	
	$('#lnkUploadManualIDS').on('click', function() {
		if (tblUploadManualRecords == null) {
			fetchUploadManualRecords();
		}
	})
	
	$('#lnkValidateRefStatusIDS').on('click', function() {
		if (tblValidateRefStatusRecords == null) {
			fetchValidateRefStatusRecords();
		}
	})
	

	function fetchInitiateIDSAllRecords() {
		if (tblInitiateIDSAllRecords != null) {
			tblInitiateIDSAllRecords.destroy();
		}

		var filterData = {
		//'iDateRange' : $('#dateRangeCreateReq').val(),
		'iRecordFlag':$('#switchRecordsView').is(':checked')
		}
		var colDef = {
			'col' : [],
			'defaultVal' : 'filingdate',
			'order' : [ 4, 'desc' ],
		};
		tblInitiateIDSAllRecords = new $.blackbox.mdm.DataTable(
				'#tblInitiateIDSAllRecords', '/ids/initiateIDSAllRecords/view',
				filterData, true, fnCallbackIDSAllRecords, colDef);

	}
	function fnCallbackIDSAllRecords() {
		showFamilyView(tblInitiateIDSAllRecords);
		bindFamilyClick(tblInitiateIDSAllRecords);
		showSpecificView();
	}

	function fetchInitiateIDSUrgentRecords() {
		if (tblInitiateIDSUrgentRecords != null) {
			tblInitiateIDSUrgentRecords.destroy();
		}
		var filterData = {
		//'iDateRange' : $('#dateRangeCreateReq').val(),
		
		}
		var colDef = {
			'col' : [],
			'defaultVal' : 'filingdate',
			'order' : [ 4, 'desc' ],
		};
		tblInitiateIDSUrgentRecords = new $.blackbox.mdm.DataTable(
				'#tblInitiateIDSUrgentRecords',
				'/ids/initiateIDSUrgentRecords/view', filterData, true,
				fnCallbackIDSUrgentRecords, colDef);

	}
	
	function fnCallbackIDSUrgentRecords() {
		showFamilyView(tblInitiateIDSUrgentRecords);
		bindFamilyClick(tblInitiateIDSUrgentRecords);
		showSpecificView();
	}

	function fetchPendingIDSRecords() {
		if (tblPendingIDSAllRecords  != null) {
			tblPendingIDSAllRecords .destroy();
		}

		var target =  $("#contextPath").val() + "/ids/pendingIDSRecords/view";
	
		$.ajax({
			type : "GET",
			url : target,
			success : function(response) {
				var result = response.split('<p id="splitter"/>');
				$('#tbodyPendingRecords').append(result[1]);
				var countHtml = $('<div/>').html(result[0]);
				
				pendingIDSCount(countHtml);
				
				if(tblPendingIDSAllRecords != null)
				{
					tblPendingIDSAllRecords.destroy();
				}
				if(response.trim()!="") {
					tblPendingIDSAllRecords = $("#tblPendingRecords").DataTable({
						"fnDrawCallback":function(){
						},
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
						'bSortable' : true,
						'aTargets' : [ 3 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 4 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 5 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 6 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 7 ]
					}
					]
					
				});
				}
				
		}
	});
	}

	function fetchPendingResponseRecords() {
		
		if (tblPendingResponseAllRecords  != null) {
			tblPendingResponseAllRecords .destroy();
		}

		var target =  $("#contextPath").val() + "/ids/pendingResponseRecords/view";
	
		$.ajax({
			type : "GET",
			url : target,
			success : function(response) {
				$('#tbodyPendingResponseRecords').append(response);
				
				var result = response.split('<p id="splitter"/>');
				$('#tbodyPendingResponseRecords').append(result[1]);
				var countHtml = $('<div/>').html(result[0]);
				
				pendingIDSCount(countHtml);
				if(tblPendingResponseAllRecords != null)
				{
					tblPendingResponseAllRecords.destroy();
				}
				if(response.trim()!="") {
					tblPendingResponseAllRecords = $("#tblPendingResponseRecords").DataTable({
						"fnDrawCallback":function(){
							emailResponseAction();
							submitEmailResponse();
							//setTabCount(parentTabId, tabId);
						},
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
						'bSortable' : true,
						'aTargets' : [ 3 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 4 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 5 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 6 ]
					},
					{
						'bSortable' : true,
						'aTargets' : [ 7 ]
					}]
					 
				});
				}
		}
	});
	}

	function fetchFilingReadyRecords() {
		if (tblFilingReadyRecords  != null) {
			tblFilingReadyRecords .destroy();
		}

		var target =  $("#contextPath").val() + "/ids/filingReadyRecords/view";
	
		$.ajax({
			type : "GET",
			url : target,
			success : function(response) {
				$('#tbodyFilingReadyRecords').append(response);
				if(tblFilingReadyRecords != null)
				{
					tblFilingReadyRecords.destroy();
				}
			
				tblFilingReadyRecords = $("#tblFilingReadyRecords").DataTable({
					"fnDrawCallback":function(){
						//fileReadyIds();
					},
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
					'bSortable' : true,
					'aTargets' : [ 3 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 4 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 5 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 6 ]
				}]
				
			});
	}
		});
	}
	
	
	function fetchFilingInProgressRecords() {
		if (tblFilingInProgressRecords  != null) {
			tblFilingInProgressRecords .destroy();
		}

		var target =  $("#contextPath").val() + "/ids/filingInProgressRecords/view";
	
		$.ajax({
			type : "GET",
			url : target,
			success : function(response) {
				$('#tbodyFilingInProgressRecords').append(response);
				if(tblFilingInProgressRecords != null)
				{
					tblFilingInProgressRecords.destroy();
				}
			
				tblFilingInProgressRecords = $("#tblFilingInProgressRecords").DataTable({
					"fnDrawCallback":function(){
						changeToStatusTransfer();
						fnCallbackIDSInProgressRecords();
					},
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
					'bSortable' : true,
					'aTargets' : [ 3 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 4 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 5 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 6 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 7 ]
				},
				{
					'bSortable' : false,
					'aTargets' : [ 8 ]
				}]
				
			});
		}
	});
}
	
	function fetchUploadManualRecords() {
		
		if (tblUploadManualRecords  != null) {
			tblUploadManualRecords .destroy();
		}
		var target =  $("#contextPath").val() + "/ids/filingUploadManualRecords/view";
	
		$.ajax({
			type : "GET",
			url : target,
			success : function(response) {
				$('#tbodyUpdloadManuallyRecords').append(response);
				if(tblUploadManualRecords != null)
				{
					tblUploadManualRecords.destroy();
				}
			
				tblUploadManualRecords = $("#tblUpdloadManuallyRecords").DataTable({
					"fnDrawCallback":function(){
					},
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
					'bSortable' : true,
					'aTargets' : [ 3 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 4 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 5 ]
				}]
				
			});
		}
	});
	}
	
	function fetchValidateRefStatusRecords() {
		
		if (tblValidateRefStatusRecords  != null) {
			tblValidateRefStatusRecords .destroy();
		}
		var target =  $("#contextPath").val() + "/ids/filingValidateRefStatusRecords/view";
	
		$.ajax({
			type : "GET",
			url : target,
			success : function(response) {
				$('#tbodyValidateRefStatusRecords').append(response);
				if(tblValidateRefStatusRecords != null)
				{
					tblValidateRefStatusRecords.destroy();
				}
			
				tblValidateRefStatusRecords = $("#tblValidateRefStatusRecords").DataTable({
					"fnDrawCallback":function(){
					},
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
					'bSortable' : true,
					'aTargets' : [ 3 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 4 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 5 ]
				}]
				
			});
		}
	});
	}
	
	
	function viewFiledAtUSPTORecords() {
		if ($('#lnkIDSFilingPackage').parent().hasClass('active'))
			fetchIDSFilingPackageRecords();
		if ($('#lnk1449').parent().hasClass('active'))
			fetch1449PendingIDSRecords();
	}
	
	$('#tabIDSFiledAtUSPTO').on('click',function(){
		viewFiledAtUSPTORecords();
	});

	function fetchIDSFilingPackageRecords()
	{
		var target =  $("#contextPath").val() + "/ids/idsFilingPackageRecords/view";
	
		$.ajax({
			type : "GET",
			url : target,
			success : function(response) {
				$('#tbodyIdsFiledRecords').append(response);
				if(tblIDSFilingPackageRecords != null)
				{
				tblIDSFilingPackageRecords.destroy();
				}
			
			tblIDSFilingPackageRecords = $("#tblIdsFiledRecords").DataTable({
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
					'bSortable' : true,
					'aTargets' : [ 3 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 4 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 5 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 6 ]
				},
				{
					'bSortable' : true,
					'aTargets' : [ 7 ]
				},
				{
					'bSortable' : false,
					'aTargets' : [ 8 ]
				}]
				
			});
		}
	});
		
		
	}
	
	$('#lnkIDSFilingPackage').on('click',function(){
		if(tblIDSFilingPackageRecords == null)
			{
			fetchIDSFilingPackageRecords();
			}
	});
	
	 function fetch1449PendingIDSRecords()
	 {
		 var target =  $("#contextPath").val() + "/ids/1449PendingIDSRecords/view";
			
			$.ajax({
				type : "GET",
				url : target,
				success : function(response) {
					$('#tbody1449NotificationRecords').append(response);
				create1449PendingIDSDataTable();
			}
		});
	 }
	 
	 function create1449PendingIDSDataTable()
	 {
			if(tbl1449PendingIDSRecords != null)
			{
				tbl1449PendingIDSRecords.destroy();
			}
		
			tbl1449PendingIDSRecords = $("#tbl1449NotificationRecords").DataTable({
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
				'bSortable' : true,
				'aTargets' : [ 3 ]
			},
			{
				'bSortable' : false,
				'aTargets' : [ 4 ]
			}]
			
		});
			//tbl1449PendingIDSRecords.draw();
	 }
	 
	 $('#lnk1449').on('click',function(){
			if(tbl1449PendingIDSRecords == null)
				{
				fetch1449PendingIDSRecords();
				}
		});

	/*- ----------------------------------------------------------
	 My Dashboard - Records View - Switch Button
	 -------------------------------------------------------------- */
	$(document).on(
			'change',
			'#switchRecordsView',
			function() {
				if ($(this).is(':checked')) {
					
					if ($('#lnkUrgentIDS').parent().hasClass('active')) {
						
						disableExpandedView($('.mdmTable').find('.icon-minus'),tblInitiateIDSUrgentRecords);
						
					} else if ($('#lnkAllIDS').parent().hasClass('active')) {
						disableExpandedView($('.mdmTable').find('.icon-minus'),tblInitiateIDSAllRecords);
						
					}
						$('.duplicateFamily').closest('tr').show();
						showApplicationView(tblInitiateIDSUrgentRecords);
						showApplicationView(tblInitiateIDSAllRecords);
				} else {
					$('.duplicateFamily').closest('tr').hide();
						showFamilyView(tblInitiateIDSUrgentRecords); 
						showFamilyView(tblInitiateIDSAllRecords);
					}
			});

	function showSpecificView() {
		if ($('#switchRecordsView').is(':checked')) {
			$('.duplicateFamily').closest('tr').show();
			if ($('#lnkUrgentIDS').parent().hasClass('active')) {
				showApplicationView(tblInitiateIDSUrgentRecords);
			} else if ($('#lnkAllIDS').parent().hasClass('active')) {
				showApplicationView(tblInitiateIDSAllRecords);
			}
		} 
		else 
			{
			$('.duplicateFamily').closest('tr').hide();
				if ($('#lnkUrgentIDS').parent().hasClass('active')) {
					showFamilyView(tblInitiateIDSUrgentRecords);	
				}else if ($('#lnkAllIDS').parent().hasClass('active')) {
					showFamilyView(tblInitiateIDSAllRecords);
			}
			}
	}

	function showApplicationView(tableName) {
		if(tableName != null){
		var column1 = tableName.column(0);
		column1.visible(false);
	}
	}

	function showFamilyView(tableName) {
		if(tableName != null){
		var column1 = tableName.column(0);
		column1.visible(true);
	}
	}
	
	 function disableExpandedView(element, tableName) {
		 if(tableName != null)
			 {
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
	} 
	
	function bindFamilyClick(tableName) {
		if (tableName!= null)
		{	
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
								$(this).removeClass("icon-plus").addClass(
										"icon-minus");
							} else if ($(this).hasClass("icon-minus")) {
								$(this).removeClass("icon-minus").addClass(
										"icon-plus");
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
								if ($('#lnkUrgentIDS').parent().hasClass('active')) {
									getFamilyMembers(this, tableName);
								} else {
									getFamilyMembers(this, tableName);
								}
							}
						});
	}
	}
	function getFamilyMembers(element,tableName) {
		var famId = $($(element).closest('tr')).find('.familyId').text();
		var appId = $($(element).closest('tr')).find('.appId').val();
		var target = $("#contextPath").val() + "/ids/initiateIDS/allFamilyDetails";
		var data = {"familyId" : famId,"applicationId" : appId};
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
			}
		});
	}
	
	function showConfirmationBox(text) {
		$('.changeIdsStatusConfirmationBox').find('.msg ').text(
				'Are you sure you want to select - ' + text  +'?');
		$('.changeIdsStatusConfirmationBox').show();
		$('.changeIdsStatusConfirmationBox').wrap("<div class='overlay'>");
	}
	
	function hideConfirmationBox() {
		$('.changeIdsStatusConfirmationBox').hide();
		$('.changeIdsStatusConfirmationBox').unwrap("<div class='overlay'>");
	}
	
	function changeToStatusTransfer() {
		//$('#transferRecord').html("");
		
		$(".selectedStatus").on('change', function() {
			selectedRow = $(this).parents('tr');
			var selectedVal = $(event.target).val();
			var idsId = $(this).parents('tr').attr('idsId');
			if(selectedVal=='filedIt') {
				$('.changeIdsStatusConfirmationBox').attr('idsId', idsId);
				$('.changeIdsStatusConfirmationBox').attr('selectedStatus', "FILED_IT");
				showConfirmationBox("I have filed it");
			}else if(selectedVal=='doNotFile') {
				$('.changeIdsStatusConfirmationBox').attr('idsId', idsId);
				$('.changeIdsStatusConfirmationBox').attr('selectedStatus', "DO_NOT_FILE");
				showConfirmationBox("I have decided not to file");
		}
			//showConfirmationBox();
			/* $('#transferRecord').on('show.bs.modal', function() {
				var target = "../mdm/application/transfer/record?recordId=" + recordId;

				$.get(target, function(data) {
					$('#transferRecord').html(data);
				});
			}); */
		});
	}
	
	function fnCallbackIDSInProgressRecords() {
		changeToStatusTransfer();
		submitIdsStatusChange();
	}
	
	
	function submitIdsStatusChange() {
		$('#submitIdsStatus').on('click', function () {
			$.ajax({
				type : "POST",
				url : "userAction/changeIdsStatus",
				data: {"idsId":$('.changeIdsStatusConfirmationBox').attr('idsId'),"selectedStatus" : $('.changeIdsStatusConfirmationBox').attr('selectedStatus')},
				success : function(response) {
					if(response=="WITHDRAWN") {
						selectedRow.html("");
					}else{
						selectedRow.find(".selectedStatus").parents('td').html("<td>" + response + " </td>");
					}
					console.log(response);
					hideConfirmationBox();
				}
			});
		});
	}
	
	function emailResponseAction() {
		$('.lnkEmailResponse').on('click', function(){
			$('#submitEmailResponse').attr('idsId', $(this).parents('tr').attr('idsId'));
			$('#submitEmailResponse').attr('notificationId', $(this).parents('tr').attr('notificationid'));
		});	
	}
	
	
	function submitEmailResponse() {
		$('#submitEmailResponse').on('click', function(){
			var idsId = $('#submitEmailResponse').attr('idsId');
			var notificationId =  $('#submitEmailResponse').attr('notificationId');
			var comment = $('#parallegalComments').val();
			$.ajax({
				type : "POST",
				url : "pendingApproval/emailResponse",
				data: {"idsID":idsId ,"notificationId" : notificationId, "comment" : comment},
				success : function(response) {
					console.log(response);
					$('.emailResponse').hide();
					$('.emailResponse').unwrap("<div class='overlay'>");
				}
			});
		});	
	}
	

	/* function fileReadyIds(){
		$(".fileReadyIds").on('click',function(){
			$.ajax({
				'url' : $('#contextPath').val() + '/ids/buildIDS/checkRefCount/'+$(this).attr("appId"),
				"data":{
					idsID:$(this).attr("idsId")
				},
				success : function(data) {
					bindShowRefMessages(data.US_PUBLICATION,data.FP,data.NPL);
				}
			});	
		})
	} */
	
	
	
	/*************************************** update ref status scripts copied from HTMLS *****************************************/
	/* $(function () {
		$('.updateRefStatus').on('click', function(){
			$.ajax({
				type : "POST",
				url : "userAction/updateRefStatus",
				data: {"filingInfoId":$('.updateRefStatus').attr('filingInfoId')},
				success : function(response) {
					console.log(response);
				}
			});
		})
	});
     */    
     
	
	
     $(function () {
    	 
            $(".hidden-row").hide();
            $(".has-hidden-row span").on("click", function () {

                if ($(this).hasClass("icon-plus")) {
                    $(this).parents(".has-hidden-row").addClass("active");
                    $(this).removeClass("icon-plus").addClass("icon-minus");
                    $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
                } else if ($(this).hasClass("icon-minus")) {
                    $(this).parents(".has-hidden-row").removeClass("active");
                    $(this).removeClass("icon-minus").addClass("icon-plus");
                    $(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
                }


            });

        });

        

        $(function () {

            $('.icon-calendar').click(function () {
                $(document).ready(function () {
                    $("#config-demo").daterangepicker({
                        opens: 'left'
                    }).focus();
                });
            });
            /////////////////////
            $(".header .search-input .form-control").on("focus", function () {
                $(this).parents(".search-input").addClass("active");
            });
            $(".header .search-input .form-control").on("focusout", function () {
                $(this).parents(".search-input").removeClass("active");
            });

            /////////////////////////
            $("#gotoSearch").on("click", function () {
                window.location.assign("search.html");
            });
            /////////////////////////

            var searchHeight = $('.search-dropdown').height();

            $(".search-control .icon-search").on("click", function () {
                $('.search-dropdown').slideToggle("show");
            });

            $("#hideSearch").on("click", function () {
                $(this).parents(".search-input").removeClass("active");
                $(".search-dropdown").slideUp("hide");
            });



            //tooltip
            $(function () {
                $('[data-toggle="tooltip"]').tooltip();
                $('.has-error .form-control').tooltip({
                    trigger: 'manual'
                }).tooltip('show');
                $('.has-error .form-control').on('focus', function () {
                    $(this).tooltip('destroy');
                });
            })

        });

        jQuery(document).ready(function ($) {
            $('.multiselect').multiselect();
        });


        $("#createNewAccess").on("click", function () {
            window.location.assign("create-new-access-profile.html");
        });

        $("#createNewUser").on("click", function () {
            window.location.assign("create-new-user.html");
        });

        $("#duplicateAcessProfile").on("click", function () {
            window.location.assign("user-management.html");
        });

        jQuery(document).ready(function ($) {
            $('#newRole').on("click", function () {
                if ($(this).is(":checked")) {
                    window.location.assign("create-new-role.html");
                }
            });
        });

        $(function () {
            $("#familyGrid, #familyGrid_AllRecord, #familyGrid_inactiveRecord").hide();
            $(".switch-control input[type='checkbox']").on("click", function () {
                if ($(".switch-control.mdm input[type='checkbox']").is(":checked")) {
                    $("#applicationGrid, #applicationGrid_AllRecord, #applicationGrid_inactiveRecord").show();
                    $("#familyGrid, #familyGrid_AllRecord, #familyGrid_inactiveRecord").hide();
                } else {
                    $("#applicationGrid, #applicationGrid_AllRecord, #applicationGrid_inactiveRecord").hide();
                    $("#familyGrid, #familyGrid_AllRecord, #familyGrid_inactiveRecord").show();
                }
            });

        });

        //date range picker js
        $(document).ready(function () {
            $(".daterange-picker .date").daterangepicker({
                opens: 'left',
                autoUpdateInput: false,
                locale: {
                    format: 'MMM DD, YYYY',
                    cancelLabel: 'Clear'
                }
            });
            $('.daterange-picker i').click(function () {
                $(this).parents(".daterange-picker").find('input').click();
            });

            $('.daterange-picker .date').on('cancel.daterangepicker', function (ev, picker) {
                $(this).parents(".daterange-picker").find('input').val('');
            });

            $('input[name="datefilter"]').on('apply.daterangepicker', function (ev, picker) {
                $(this).val(picker.startDate.format('MMM DD, YYYY') + ' - ' + picker.endDate.format('MMM DD, YYYY'));
            });

            $('input[name="datefilter"]').on('cancel.daterangepicker', function (ev, picker) {
                $(this).val('');
            });
        });
        
        
        
        
        /*********************** FILE IDS JSP CODE ***/
        
	$(function(){
		$(".hidden-row").hide();
		$(".has-hidden-row span").on("click", function(){

			if($(this).hasClass("icon-plus")){
				$(this).parents(".has-hidden-row").addClass("active");
				$(this).removeClass("icon-plus").addClass("icon-minus");
				$(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
			}
			else if($(this).hasClass("icon-minus")){
				$(this).parents(".has-hidden-row").removeClass("active");
				$(this).removeClass("icon-minus").addClass("icon-plus");
				$(this).parents(".has-hidden-row").siblings(".hidden-row").toggle();
			}

			
		});
	
	});



	$(function(){

		$('.icon-calendar').click(function(){
		    $(document).ready(function(){
		        $("#config-demo").daterangepicker({
		        	opens:'left'
		        }).focus();
		    });
		});
	/////////////////////
		$(".header .search-input .form-control").on("focus", function(){
			$(this).parents(".search-input").addClass("active");
		});
		$(".header .search-input .form-control").on("focusout", function(){
			$(this).parents(".search-input").removeClass("active");
		});

	/////////////////////////
		$("#gotoSearch").on("click", function(){
			window.location.assign("search.html");
		});		
	/////////////////////////

		var searchHeight = $('.search-dropdown').height();
	//	$('.search-dropdown').css("marginTop", -searchHeight);

		$(".header .search-input .form-control").on("focus", function(){
			//$(this).parents(".search-input").addClass("active");
			//$('.search-dropdown').animate({"marginTop": 0, "toggle": "height"}, 1500);
			$('.search-dropdown').slideToggle("show");
		});

		$("#hideSearch").on("click", function(){
			$(this).parents(".search-input").removeClass("active");
			$(".search-dropdown").slideUp("hide");
		});

		//tooltip
	  $(function () {
	     $('[data-toggle="tooltip"]').tooltip();
	     $('.has-error .form-control').tooltip({trigger: 'manual'}).tooltip('show');
	     $('.has-error .form-control').on('focus',function(){$(this).tooltip('destroy');});
	  })

	});

	
	    $(function () {
	        $('.datepickercontrol').datepicker();
	    });

//date range picker js
			$(document).ready(function(){
		        $(".daterange-picker .date").daterangepicker({
		        	opens:'left',
		        	 autoUpdateInput: false,
		        	locale: {
				      format: 'MMM DD, YYYY',
				      cancelLabel: 'Clear'
				    }
		        });
		         $('.daterange-picker i').click(function() {
			      $(this).parents(".daterange-picker").find('input').click();
			    });

		         $('.daterange-picker .date').on('cancel.daterangepicker', function(ev, picker) {
					  $(this).parents(".daterange-picker").find('input').val('');
				});

		         $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
				      $(this).val(picker.startDate.format('MMM DD, YYYY') + ' - ' + picker.endDate.format('MMM DD, YYYY'));
				  });

				  $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
				      $(this).val('');
				  });
		    });

		//simple alert popup js
			function simpleMsg(){
				$("#simpleMsg").removeClass("hide");
				$("#simpleMsg").show();
				$("#simpleMsg").wrap("<div class='overlay'>");
			}
			$(document).on("click", ".popup-msg a.close", function(){
				$(this).parents(".popup-msg").addClass("hide");
				$(this).parents(".popup-msg").unwrap("<div class='overlay'>");
			});
			
			function pendingIDSCount(countHtml) {
				var tab1Count  = countHtml.find('#pendingIDSCount').val();
				var tab2Count  = countHtml.find('#pendingResponseCount').val();
				$('#idsPendingApprovalCount').html(tab1Count);
				$('#idsPendingResponseCount').html(tab2Count);
				$('#idsPendingCount').html(tab1Count+tab2Count);
			}
			
			function wipFilingCount(countHtml) {
				var tab1Count  = countHtml.find('#pendingIDSCount').val();
				var tab2Count  = countHtml.find('#pendingResponseCount').val();
				var tab3Count  = countHtml.find('#pendingResponseCount').val();
				$('#idsPendingApprovalCount').html(tab1Count);
				$('#idsPendingResponseCount').html(tab2Count);
				$('#idsPendingResponseCount').html(tab3Count);
				$('#idsPendingCount').html(tab1Count+tab2Count);
			}
			
			function filedIDSCount(countHtml) {
				var tab1Count  = countHtml.find('#pendingIDSCount').val();
				var tab2Count  = countHtml.find('#pendingResponseCount').val();
				$('#idsPendingApprovalCount').html(tab1Count);
				$('#idsPendingResponseCount').html(tab2Count);
				$('#idsPendingCount').html(tab1Count+tab2Count);
			}
			
			function setParentTabCount(parentTabId) {
				switch (activeTab) {
				case 0: {
					break;
				}
				}
			}
        
</script>