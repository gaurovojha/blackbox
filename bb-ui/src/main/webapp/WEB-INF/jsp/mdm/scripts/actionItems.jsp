<script>
	var tblApplications = null;
	var tblFamilies = null;
	var applicationRecordsTable = null;
	var assigneeRecordsTable = null;
	var familyRecordsTable = null;
	var tblChangeRequests = null;
	var decreaseCount = false;

	$(document).ready(function() {
		bindControls();
		findActiveTab();

	});
	
	function countValue(lblSelector) {
		return parseInt($(lblSelector).val());
	}
	
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
					findActiveTab();
				});

		$('input[name="datefilter"]').on('cancel.daterangepicker',
				function(ev, picker) {
					$(this).val('');
					findActiveTab();
				});

	}
	function findActiveTab() {
		var activeTab = $('#tabGroupActionItems').find('.active').index();

		switch (activeTab) {
		case 0: {
			fetchCreateRequests();
			break;
		}
		case 1: {
			fetchUpdateRequests();
			break;
		}
		case 2: {
			fetchChangeRequests();
			break;
		}

		}
	}

	function fetchCreateRequests() {
		if ($('#createApplicationLink').parent().hasClass('active'))
			fetchCreateAppRecords()
		else if ($('#createFamilyLink').parent().hasClass('active'))
			fetchCreateFamilyRecords();
	}

	function fetchUpdateRequests() {
		if ($('#applicationReqLink').parent().hasClass('active'))
			fetchApplicationRecords();
		else if ($('#assigneeReqLink').parent().hasClass('active'))
			fetchAssigneeRecords();
		else if ($('#familyReqLink').parent().hasClass('active'))
			fetchFamilyRecords();
	}

	function fetchChangeRequests() {
		//if ($('#changeRequestLink').parent().hasClass('active')){}
		//if (tblChangeRequests == null) {}
		fetchChangeRequestRecords();

	}

	/*- ----------------------------------------------------------
	My Dashboard - Create Application Request Records - Application View
	-------------------------------------------------------------- */
	function fetchCreateAppRecords() {
		if (tblApplications != null) {
			tblApplications.destroy();
		}
		var filterData = {
			'iDateRange' : $('#dateRangeCreateReq').val(),
		}
		var colDef = {
				'col' : [  {
					"name" : "jurisdiction",
					"targets" : 0,
					"bSortable":false
				}, {
					"name" : "application",
					"targets" : 1,
					"bSortable":false
				},  {
					"name" : "sentby",
					"targets" : 2
				},
				{
					"name":"notifiedon",
					"targets": 3
				},
				{
					"name" : "actions",
					"targets" : 4,
					"bSortable":false
				} ],
				'defaultVal' : 'notifiedon',
				'order' : [3, 'desc'],
			};
		tblApplications = new $.blackbox.mdm.DataTable('#createAppRecords',
				'/mdm/actionItems/createRequestApp/view', filterData, true,fnCallbackCreateApp,colDef);

	}

	/*- ----------------------------------------------------------
	My Dashboard - Create Application Family Records - Application View
	-------------------------------------------------------------- */
	function fetchCreateFamilyRecords() {
		if (tblFamilies != null) {
			tblFamilies.destroy();
		}
		var filterData = {
			'iDateRange' : $('#dateRangeCreateReq').val(),
			'iMyRecords' : true,
		}
		var colDef = {
				'col' : [   {
					"name" : "jurisdiction",
					"targets" : 0,
					"bSortable":false
				},  
				{
					"name" : "application",
					"targets" : 1,
					"bSortable":false
				},
				{
					"name" : "linkedFamilyId",
					"targets" : 2,
					"bSortable":false
				},
				{
					"name":"linkedJurisdiction",
					"targets":3,
					"bSortable":false
				},
				{
					"name":"linkedApplication",
					"targets":4,
					"bSortable":false
				},
				{
					"name":"source",
					"targets":5,
					"bSortable":false
				},
				{
					"name":"notifiedon",
					"targets":6
				},
				{
					"name" : "actions",
					"targets" : 7,
					"bSortable":false
				} ],
				'defaultVal' : 'notifiedon',
				'order' : [6, 'desc'],
			};
		tblFamilies = new $.blackbox.mdm.DataTable('#createFamilyRecords',
				'/mdm/actionItems/createRequestFamily/view', filterData, true,fnCallbackCreateFamily,colDef);

	}

	/*- ----------------------------------------------------------
	 Update Request - Application Records
	 -------------------------------------------------------------- */
	function fetchApplicationRecords() {
		if (applicationRecordsTable != null) {
			applicationRecordsTable.destroy();
		}
		var filterData = {
			'iDateRange' : $('#dateRangeUpdateReq').val()
		};
		var colDef = {
				'col' : [  {
					"name" : "jurisdiction",
					"targets" : 0,
					"bSortable":false
				}, {
					"name" : "application",
					"targets" : 1,
					"bSortable":false
				},  {
					"name" : "discrepencies",
					"targets" : 2,
					"bSortable":false
				},
				{
					"name":"document",
					"targets":3,
					"bSortable":false
				},
				{
					"name":"notifiedon",
					"targets": 4
				},
				{
					"name" : "actions",
					"targets" : 5,
					"bSortable":false
				} ],
				'defaultVal' : 'notifiedon',
				'order' : [4, 'desc'],
			};
		applicationRecordsTable = new $.blackbox.mdm.DataTable(
				'#tblUpdateApplcation', '/mdm/actionItems/updateRequestApp/view', filterData,
				true, fnCallbackUpdateApp,colDef);

	}

	function fetchAssigneeRecords() {
		if (assigneeRecordsTable != null) {
			assigneeRecordsTable.destroy();
		}
		var filterData = {
			'iDateRange' : $('#dateRangeUpdateReq').val()
		};
		var colDef = {
				'col' : [  {
					"name" : "familyid",
					"targets" : 0,
					"bSortable":false
				}, {
					"name" : "jurisdiction",
					"targets" : 1,
					"bSortable":false
				},  {
					"name" : "application",
					"targets" : 2,
					"bSortable":false
				},
				{
					"name":"attorneydocket",
					"targets": 3
				},
				{
					"name":"notifiedon",
					"targets":4
				},
				{
					"name" : "actions",
					"targets" : 5,
					"bSortable":false
				} ],
				'defaultVal' : 'notifiedon',
				'order' : [4, 'desc'],
			};
		
		assigneeRecordsTable = new $.blackbox.mdm.DataTable(
				'#tblUpdateAssignee', '/mdm/actionItems/updateRequestAssignee/view',
				filterData, true, fnCallbackUpdateAssignee,colDef);

		//assigneeRecordsTable.column(4).visible(false);

	}
	function fetchFamilyRecords() {
		if (familyRecordsTable != null) {
			familyRecordsTable.destroy();
		}
		var filterData = {
			'iDateRange' : $('#dateRangeUpdateReq').val()
		};
		var colDef = {
				'col' : [   {
					"name" : "jurisdiction",
					"targets" : 0,
					"bSortable":false
				},  
				{
					"name" : "application",
					"targets" : 1,
					"bSortable":false
				},
				{
					"name":"createdby",
					"targets":2
				},
				{
					"name" : "linkedFamilyId",
					"targets" : 3,
					"bSortable":false
				},
				{
					"name":"linkedJurisdiction",
					"targets":4,
					"bSortable":false
				},
				{
					"name":"linkedApplication",
					"targets":5,
					"bSortable":false
				},
				{
					"name":"source",
					"targets":6,
					"bSortable":false
				},
				{
					"name":"notifiedon",
					"targets":7
				},
				{
					"name" : "actions",
					"targets" : 8,
					"bSortable":false
				} ],
				'defaultVal' : 'notifiedon',
				'order' : [7, 'desc'],
			};
		
		familyRecordsTable = new $.blackbox.mdm.DataTable('#tblUpdateFamily',
				'/mdm/actionItems/updateRequestFamilyLinkage/view', filterData, true,fnCallbackUpdateFamily,colDef);

	}

	/*- ----------------------------------------------------------
	My Dashboard - Change Request Records - Application View
	-------------------------------------------------------------- */
	function fetchChangeRequestRecords() {
		if (tblChangeRequests != null) {
			tblChangeRequests.destroy();
		}
		var filterData = {
			'iDateRange' : $('#dateRangeChangeReq').val(),
			'iMyRecords' : true,
		}
		var colDef = {
				'col' : [
				{
					"name" : "familyId",
					"targets" : 0
				},{
					"name" : "jurisdiction",
					"targets" : 1
				}, {
					"name" : "application",
					"targets" : 2
				}, {
					"name" : "currentstatus",
					"targets" : 3
				}, {
					"name" : "requestedfor",
					"targets" : 4
				},
				{
					"name" : "requestedby",
					"targets" : 5
				},
				{
					"name" : "actions",
					'bSortable' : false,
					"targets" : 6
				},{
					"name" : "notifiedon",
					"targets" : 7,
					"bVisible": false, 
				} ],
				'defaultVal' : 'notifiedon',
				'order' : [7, 'desc'],
			};
		tblChangeRequests = new $.blackbox.mdm.DataTable('#changeReqRecords',
				'/mdm/actionItems/changeRequest/view', filterData, true, fnCallbackChangeReq , colDef);

	}
	
	

	$('#createFamilyLink').on('click', function() {
		if (tblFamilies == null) {
			fetchCreateFamilyRecords();
		}
	})

	$('#createApplicationLink').on('click', function() {
		if (tblApplications == null) {
			fetchCreateAppRecords();
		}
	})

	$('#applicationReqLink').on('click', function() {
		if (applicationRecordsTable == null) {

			fetchApplicationRecords();
		}
	});
	$('#assigneeReqLink').on('click', function() {
		if (assigneeRecordsTable == null) {
			fetchAssigneeRecords();
		}
	});

	$('#familyReqLink').on('click', function() {
		if (familyRecordsTable == null) {
			fetchFamilyRecords();
		}
	});

	$('#createRequestLink').on('click', function() {
		fetchCreateRequests();
		tblFamilies = null;
		tblApplications = null;
		
	});
	$('#updateRequestLink').on('click', function() {
		fetchUpdateRequests();
		applicationRecordsTable =null;
		assigneeRecordsTable = null;
		familyRecordsTable = null;
		
	});
	$('#changeRequestLink').on('click', function() {
		fetchChangeRequests();
	})

	function fnCallbackCreateFamily()
	{
		acceptCreateAppReq();
		rejectCreateFamilyReq();
		viewFamily();
		if (decreaseCount) {
			var countCreateApp = parseInt($('#lblCreateReqCount').html().charAt(1)) - 1;
			$('#lblCreateReqCount').text('('+countCreateApp+')');
			var actionItemCount = parseInt($('#actionItemCount').html()) - 1;
			$('#actionItemCount').html(actionItemCount)
			decreaseCount = false;
		}
	}
	
	function fnCallbackUpdateApp() {
		bindPdfClick();
	}

	function fnCallbackUpdateAssignee() {
		viewFamily();
		$(".btnAddAssignee").on('click', function() {
			self = this;
			var record = [];
			var object = $(this).closest('tr').find('td');
			$.each(object, function(ind, value) {
				record.push(value.textContent.trim(" "));
			});
			record.pop();
			record.pop();
			/* var rownum = $(this).closest('tr').find('.rowId').val();
			var assignee = assigneeRecordsTable.cells({
				row : rownum,
				column : 4
			}).data()[0].trim(" ");
			record.splice(4, 0, assignee); */

			var modalRecord = $('.modalFormRecord').children().find('div');
			$.each(modalRecord, function(ind, value) {
				if($(value.firstChild).hasClass('familyId'))
				{
					$(value.firstChild).attr('data',record[ind])
				}
				value.firstChild.textContent = record[ind];
			});
			
			$("#txtBoxAssignee").val("");
			hideValidations();
			//$(".btnUpdateAssignee").addClass('disabled');
			//$("#txtBoxAssignee").val(record[record.length-1]);
		});

		$("#txtBoxAssignee").autocomplete({
			source : $("#contextPath").val() + "/mdm/actionItems/updateRequestAssignee/getAllAssignees",
			minLength : 2,
			select : function(event, ui) {
				console.log(ui);
				if(ui.item)
					{
						$(".btnUpdateAssignee").removeClass('disabled');
					}
			},
			/*
			change: function(event,ui){
			  $(this).val((ui.item ? ui.item.value : ""));
			  if(!ui.item)
				  {
				  $(".btnUpdateAssignee").addClass('disabled');
			}
			}*/
		});
		$("#txtBoxAssignee").keyup(function(){
			if($("#txtBoxAssignee").val())
				$(".btnUpdateAssignee").removeClass('disabled');
			
		});

		$(".btnUpdateAssignee").on(
				'click',
				function() {
					//$(this).attr('data',$(".rowId").val());
					if (!$(".btnUpdateAssignee").hasClass('disabled')) {
					if ($("#txtBoxAssignee").val()) {
						var requestUrl = $("#contextPath").val()
								+ "/mdm/actionItems/updateRequestAssignee/addAssignee"
						$.ajax({
							type : "POST",
							url : requestUrl,
							data : {
								'familyid' : $('#familyId').text(),
								'assignee' : $("#txtBoxAssignee").val(),
								'notificationid':$($(self).closest('tr')).find('.rowId').val()
							},
							success : function(result) {
								decreaseCount = true;
								if (result == 0) {
									showValidations();
								} else {
									$('#updateAssignee').modal('toggle');
									fetchUpdateRequests();
								}
							},
						});
						} else {
							showValidations();
					}
					}
				});

		$('.btnCancelAssignee').on('click',function(){
			
			$('#confirmationBox').find('.msg ').text('Your changes will not be saved. Do you want to Proceed?');
			showConfirmationBox();	
			});
		$(".btnYes").click(function(){
			$('#updateAssignee').modal('toggle');
		});
		
		if (decreaseCount) {
			var countUpdateAssignee = parseInt($('#lblupdateRequestCount').html().charAt(1)) - 1;
			$('#lblupdateRequestCount').text('('+countUpdateAssignee+')');
			var actionItemCount = parseInt($('#actionItemCount').html()) - 1;
			$('#actionItemCount').html(actionItemCount)
			decreaseCount = false;
		}
	}
	
	function fnCallbackCreateApp() {
		acceptCreateAppReq();
		rejectCreateAppReq();
		
		if (decreaseCount) {
			var countCreateApp = parseInt($('#lblCreateReqCount').html().charAt(1)) - 1;
			$('#lblCreateReqCount').text('('+countCreateApp+')');
			var actionItemCount = parseInt($('#actionItemCount').html()) - 1;
			$('#actionItemCount').html(actionItemCount)
			decreaseCount = false;
		}
	}
	
	function fnCallbackUpdateFamily()
	{
		viewFamily();	
		rejectUpdateFamilyReq();
		if (decreaseCount) {
			var countUpdateFamily = parseInt($('#lblupdateRequestCount').html().charAt(1)) - 1;
			$('#lblupdateRequestCount').text('('+countUpdateFamily+')');
			var actionItemCount = parseInt($('#actionItemCount').html()) - 1;
			$('#actionItemCount').html(actionItemCount)
			decreaseCount = false;
		}
	}
	
	function fnCallbackChangeReq() {
		//bindPdfClick();
		approveChange();
		rejectChange();
		viewFamily();
		
		if (decreaseCount) {
			var countChangeRequest = $('#changeRequestRecordsTotal').length === 0 ? 0 : countValue('#changeRequestRecordsTotal');
			$('#lblChangeRequestCount').text('('+countChangeRequest+')');
			var actionItemCount = parseInt($('#actionItemCount').html()) - 1;
			$('#actionItemCount').html(actionItemCount)
			decreaseCount = false;
		}
		
		//tooltip
	  	  $(function () {
	  	     $('[data-toggle="tooltip"]').tooltip();
	  	     $('.has-error .form-control').tooltip({trigger: 'manual'}).tooltip('show');
	  	     $('.has-error .form-control').on('focus',function(){$(this).tooltip('destroy');});
	  	  })
	}
	

	/*- ----------------------------------------------------------
							PDF popup 
	-------------------------------------------------------------- */

	function bindPdfClick() {
		$('.pdfPop').on(
				'click',
				function() {

					/*  var rowId  = $(this).attr('data');
					/*var  requestUrl = $("#contextPath").val()+"/mdm/createapp/getPdf/"+rowId;
					 $.ajax({
						  type: "GET",
						  url:requestUrl ,
						  contentType: "application/json",
						  //data: {'id':rowId},
						  success: function(result) {
							  if(result != "Error")
								  {
							  		//$('#pdfPopup').find('.contentArea').html(createPdf(result));
							  		$('#pdfPopup').find('.contentArea').attr('src',result);
							  		showPopup();
								  }
							  
						  },
						  error:function(result)
						  {
							  console.log(result);
						  }
						  
						});  */

					$('#pdfPopup').find('.contentArea').html(
							createPdf("../assets/images/Professionalism.pdf"));
					//$('#pdfPopup').find('.contentArea').attr('src',"../assets/images/Professionalism.pdf")
					showPopup();
				});
	}
	
	function acceptCreateAppReq()
	{
		var createAppForm = null;
		var node= null;
		$('.lnkCreate').on('click',function(){
			var familyId = null;
			var requestUrl = $("#contextPath").val() + "/mdm/createApp/createReq";
			var row = $(this).closest('tr');
			if($(row).hasClass('.familyId'))
			{
				  familyId = $(row).find('.familyId').text();
			}
		else
			{
				 familyId = "";
			}
			var data ={
					"jurisdiction" :$(row).find('.jurisdictionCode').text(),
					"applicationNo" :$(row).find('.appNo').text(),
					"familyId":familyId,
					"correspondenceId":$(row).find('.entityId').val(),
					"notificationId":$(row).find('.notificationId').val()
			}
			
			if(!createAppForm)
				{
				createAppForm = document.createElement("FORM");
				createAppForm.setAttribute('class','formCreateApp');
				node = document.createElement("input");
				}
			
			for(name in data)
			{
				node.name  = name;
				node.setAttribute('value',data[name].toString());
				createAppForm.appendChild(node.cloneNode());		
			}
			createAppForm.setAttribute('action',requestUrl);
			createAppForm.setAttribute('method',"POST");
			createAppForm.submit();
			
		});
	}
	
	/* function CreateAppAjaxRequest(requestUrl,data)
	{
		$.ajax({
			type:"POST",
			method:"POST",
			url : requestUrl,
			data :data,
			//contentType: "application/json; charset=utf-8",
            //dataType: "json",
			success: function(result){
				if (result === 'success') {
					window.location = $('#contextPath').val() + '/mdm/dashboard';
				} else {
						console.log(result);
				}
			}
			
		}); 
		
	} */
	
	function rejectCreateAppReq()
	{
		$('.lnkReject').on('click',function(){
			
			var requestUrl = $("#contextPath").val() + "/mdm/actionItems/createRequestApp/reject";
			$.ajax({
				type : "POST",
				url : requestUrl,
				data : {
					'notificationId' : $($(this).closest('tr')).find('.notificationId').val(),
					'entityId' : $($(this).closest('tr')).find('.entityId').val(),
					'entityName':$($(this).closest('tr')).find('.entityName').val(),
				},
				success: function(result){
					decreaseCount = true;
					if(result>0)
						{
						fetchCreateRequests();
						}
				}
				
			}); 
			
		});
		
	}
	
	function rejectCreateFamilyReq()
	{
		$('.lnkRejectApp').on('click',function(){
			
			var requestUrl = $("#contextPath").val() + "/mdm/actionItems/createRequestFamily/reject";
			$.ajax({
				type : "POST",
				url : requestUrl,
				data : {
					'notificationId' : $($(this).closest('tr')).find('.notificationId').val(),
					'entityId' : $($(this).closest('tr')).find('.entityId').val(),
					'entityName':$($(this).closest('tr')).find('.entityName').val(),
				},
				success: function(result){
					decreaseCount = true;
					if(result>0)
						{
						fetchCreateRequests();
						}
				}
				
			}); 
			
		});
	}
	
	function rejectUpdateFamilyReq()
	{
		$('.lnkReject').on('click',function(){
			
			var requestUrl = $("#contextPath").val() + "/mdm/actionItems/updateRequestFamilyLinkage/reject";
			$.ajax({
				type : "POST",
				url : requestUrl,
				data : {
					'notificationId' : $($(this).closest('tr')).find('.notificationId').val(),
					'entityId' : $($(this).closest('tr')).find('.entityId').val(),
					'entityName':$($(this).closest('tr')).find('.entityName').val(),
				},
				success: function(result){	
					decreaseCount = true;
					fetchUpdateRequests();
				}
				
			}); 
			
		});
		
	}

	function createPdf(fileName) {
		var object = "<object data=\"{FileName}\" type=\"application/pdf\" width=\"500px\" height=\"300px\">";
		object += "If you are unable to view file, you can download from <a href=\"{FileName}\">here</a>";
		object += "</object>";
		object = object.replace(/{FileName}/g, fileName);
		return object;
	}

	function showPopup() {
		$('#pdfPopup').show();
		$('#pdfPopup').wrap("<div class='overlay'>");
	}
	function hidePopup() {
		$('#pdfPopup').hide();
		$('#pdfPopup').unwrap("<div class='overlay'>");
	}

	function showValidations() {
		$('.modalAssignee').children('div:first').addClass('has-error');
		$('.modalAssignee').children("div:first").find('.error').removeClass(
				'hide');
		$('.modalAssignee').children('div:last').removeClass('hide');
		$(".btnUpdateAssignee").addClass('disabled');
	}

	function hideValidations() {
		$('.modalAssignee').children('div:first').removeClass('has-error');
		$('.modalAssignee').children("div:first").find('.error').addClass(
				'hide');
		$('.modalAssignee').children('div:last').addClass('hide');
	}
	
	function approveChange() {
		$('.approve').on('click', function(){
			var notificationId = $(this).attr('data-notificationId');
			var recordId = $(this).attr('data-eid');
			var target = $(this).attr('data-newStatus')=='DROPPED' ?  "actions/drop/approve" : "actions/deactivate/approve" ;  
			$.ajax({
				type : "POST",
				url : target,
				data : {
					'recordId' : recordId,
					'notificationId' : notificationId,
				},
				success : function(result) {
					decreaseCount = true;
					fetchChangeRequestRecords();
				},
			});
		})
	}
	
	function rejectChange() {
		$('.reject').on('click', function(){
			var notificationId = $(this).attr('data-notificationId');
			var recordId = $(this).attr('data-eid');
			var target = $(this).attr('data-newStatus')=='DROPPED' ?  "actions/drop/reject" : "actions/deactivate/reject" ;
			$.ajax({
				type : "POST",
				url : target,
				data : {
					'recordId' : recordId,
					'notificationId' : notificationId,
				},
				success : function(result) {
					decreaseCount = true;
					fetchChangeRequestRecords();
				},
			});
		})
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

</script>