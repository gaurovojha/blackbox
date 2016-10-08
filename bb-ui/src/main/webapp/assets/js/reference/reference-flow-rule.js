var referenceTableRecords = null;

$(document).ready(function() {
	//tooltip on hover
	$("body").tooltip({ selector: '[data-toggle=tooltip]' });
	
	$("#familyLinkageData, #attorneyLinkageData").hide();
	$("#familyLinkageSelect").on("change", function(){
		if($(this).val() == "application"){
			$("#applicationLinkageData").show();
			$("#attorneyLinkageData, #familyLinkageData").hide();
		}
		else if($(this).val() == "attdocket"){
			$("#attorneyLinkageData").show();
			$("#applicationLinkageData, #familyLinkageData").hide();
		}
		else if($(this).val() == "familyid"){
			$("#familyLinkageData").show();
			$("#applicationLinkageData, #attorneyLinkageData").hide();
		}
	});
	
	var clickedStatusElement = "";
	var toggleCheckbox = function(){
		clickedStatusElement = $(this);
		if($(this).is(":checked")){
			$('#confirmStatusChange').modal('show');
		}
		else{
			$('#confirmStatusChange').modal('show');
		}	
	}
	
	$(document).on("click",".on-off input", toggleCheckbox);
	
	$("#confirmStatusChange").on("click",".btn-cancel.toggleCheckbox, .close", changeToggle);
	$('#confirmStatusChange').on('hidden.bs.modal', changeToggle); 
	
	function changeToggle() {
		if(clickedStatusElement.prop("checked")){
			clickedStatusElement.prop("checked", false);
		}else{
			clickedStatusElement.prop("checked", true);
		}
		clickedStatusElement = "";
	} 
	
	
	$('#familyException').on('click',function(){
		url = $('#contextPath').val() + '/reference/flowRule/reviewFamily/fetchFamilyException';
		data = {
				"familyid": document.getElementById("familyid").value
		};
		
			$.ajax({
				type : "GET",
				url : url,
				data : data,
				success: function(result) {
					$('#familyExceptionTbl').html(result);
				}
			});
	});
	
	$(document).on('click', '#deleteFamilyLink', function(){
		url = $('#contextPath').val() + '/reference/flowRule/reviewFamily/deleteLink';
		data = {
				"sourceFamilyId": $("#familyid").val(),
				"targetFamilyId": $("#showFamilyLink").val()
		};
		
			$.ajax({
				type : "POST",
				url : url,
				data : data,
				success: function(result) {
					
				}
			});
	});
	
	
	$(document).on("change", "#showFamilyLink", function(){
		if(!($("#showFamilyLink").val()=="select")){
			$("#deleteFamilyLink").removeClass("disabled");
		}
		else{
			$("#deleteFamilyLink").addClass("disabled");		
		}		
	});
	
	$('#smlTab').on('click',function(){
		url = $('#contextPath').val() + '/reference/flowRule/reviewFamily/targetFamily';
		data = {
				"sourceFamilyId": $("#familyid").val()
		};
		
		$.ajax({
			type : "POST",
			url : url,
			data : data,
			success: function(result) {
				$('#subjectMatterLinkTab').html(result);
			}
		});
	});
	
	fetchAllRecords();
});


function fetchApplications() {
	
	var applicationSearchBy = $('#familyLinkageSelect :selected').val()
	var data = {};
	var target = "";
	
	if(applicationSearchBy === 'application') {
		data = {
			"jurisdictionCode" : 	$("input[name='jurisCode']").val(),
			"applicationNumber" : $("input[name='appNo']").val()
		}
		target = $('#contextPath').val() + '/reference/dashboard/appDetailsByApplication';
	}
	if(applicationSearchBy === 'attdocket') {
		data = {
			"attdocketNo" : $("input[name='attdocketNo']").val(),
		}
		target = $('#contextPath').val() + '/reference/dashboard/appDetailsByAttDocket';
	}
	if(applicationSearchBy === 'familyid') {
		data = {
			"familyValue" : $("input[name='familyNo']").val(),
		}
		target = $('#contextPath').val() + '/reference/dashboard/appDetailsByFamily';
	}
		
	$.ajax({
		type : "POST",
		url : target,
		data : data,
		success : function(result) {
			$('#divApplicationDetails').html(result);
		}

	});
}

function switchData(destFamilyId){
	var sourceAppId = jQuery("#selectedSourceAttr option:selected").attr('value');
	$("#sourceApp").val(sourceAppId);
	$("#targetApp").val($('#selectedSourceAttr').val());
	$("#targetFamilyId").val(destFamilyId);
	$("#sourceFamilyId").val($("#familyid").val());
}

function loadSmlData() {
	if(!($("#showFamilyLink").val()=="select")){
		data = {
				"familyid" : 	$("#familyid").val()
		};
		$.ajax({
			type : "GET",
			url : $('#contextPath').val() + '/reference/flowRule/reviewFamily/sml',
			data : data,
			success : function(result) {
				$('#referenceFlowSMLinkSrcTbl').html(result);
			},
			error : function(e){
			}
		});	
		
		loadSmlDestinationData();
	}else{
		$('#referenceFlowSMLinkTbl, #referenceFlowSMLinkSrcTbl').html("");
	}
}

function loadSmlDestinationData(){	
	data = {
			"familyid" : 	$("#familyid").val(),
			"sourceFamilyId": $("#showFamilyLink").val(),
			"applicationId" : $("#selectedSourceAttr").val()
	};
	$.ajax({
		type : "GET",
		url : $('#contextPath').val() + '/reference/flowRule/reviewFamily/dest/sml',
		data : data,
		success : function(result) {
			$('#referenceFlowSMLinkTbl').html(result);
			var destinationListSize = $('#smlDestListSize').val();
			$("#smlAppTable tbody tr:first-child").siblings("tr").remove();
			for(var i=1 ; i < destinationListSize ; i++) {
				var evenOdd = "odd";
				if(i % 2 !== 0) {
					evenOdd = "even";
				}
				var row = $("<tr class='height54 "+ evenOdd +"'><td colspan='3'></td></tr>");
				$("#smlAppTable").append(row);
			}
		},
		error : function(e){
		}
	});	
}

/* **********main page************ */


function fetchAllRecords() {
	data = {
			"familyid" : 	$("#familyid").val(),
			"applicationId" : 	$('#selectedSourceAttr').val()
	};
	
	$.ajax({
		type : "POST",
		url : $('#contextPath').val() + '/reference/flowRule/reviewFamily/dest',
		data : data,
		success : function(result) {
			$('#tbodyFamilyLinkRecords').html(result);
			var destinationListSize = $('#destListSize').val();
			$("#sourceAppTable tbody tr:first-child").siblings("tr").remove();
			for(var i=1 ; i < destinationListSize ; i++) {
				var evenOdd = "odd";
				if(i % 2 !== 0) {
					evenOdd = "even";
				}
				var row = $("<tr class='height54 "+ evenOdd +"'><td colspan='3'></td></tr>");
				$("#sourceAppTable").append(row);
			}
		}
	});
}

function checkComment(){
	  if(typeof comments.txtcomments.value === 'undefined' || comments.txtcomments.value.length==0)  {
	   		$("#comment").text("Please enter the comments").show();
	  } else {
		  $('#confirmStatusChange').modal('hide');
		  if($(".nav.nav-tabs.custom-tabs li:eq(0)").hasClass('active')){
			  submitFamilyPopupWindow();
		  }else{
			  submitSmlPopupWindow();
		  }
		  
	  }
}

function submitFamilyPopupWindow() {
	var data = {
			"sourceApp" : 	$("#sourceApp").val(),
			"targetApp" : 	$("#targetApp").val(),
			"txtcomments" :  $("#txtcomments").val(),
			"sourceFamilyId": $("#sourceFamilyId").val(),
	};
	var target = $('#contextPath').val() + '/reference/flowRule/reviewFamily/familyStatus';
	$.ajax({
		type : "POST",
		url : target,
		data : data,
		success : function(result) {
			//$('#destinationGrid').html(result);
			window.location.reload();
		}
	});
}

function submitSmlPopupWindow() {
	var data = {
			"sourceApp" : 	$("#sourceApp").val(),
			"targetApp" : 	$("#targetApp").val(),
			"txtcomments" :  $("#txtcomments").val(),
			"sourceFamilyId": $("#sourceFamilyId").val(),
			"targetFamilyId" : $("#targetFamilyId").val()
	};
	var target = $('#contextPath').val() + '/reference/flowRule/reviewFamily/smlStatus';
	$.ajax({
		type : "POST",
		url : target,
		data : data,
		success : function(result) {
			//$('#destinationGrid').html(result);
			loadSmlData();
		}
	});
}

/** ********************** */

function loadDestinationData(){
	var sourceAttrb = jQuery("#selectedSourceAttr option:selected").attr('value');
	var data = {};
	var target = "";
	
	data = {
			"dbId" : 	sourceAttrb				
	};
		target = $('#contextPath').val() + '/reference/flowRule/reviewFamily/dest';
	$.ajax({
		type : "POST",
		url : target,
		data : data,
		success : function(result) {
			$('#tbodyFamilyLinkRecords').html(result);
		}

	});
}

	function familySwitchData(destId){
		$("#sourceApp").val($('#selectedSourceAttr').val());
		$("#targetApp").val(destId);
		$("#sourceFamilyId").val(jQuery("#familyid").attr('value'));
	}
	
	function smlSwitchData(destId){
		$("#sourceApp").val($('#selectedSourceAttr').val());
		$("#targetApp").val(destId);
		$("#sourceFamilyId").val(jQuery("#familyid").attr('value'));
		$("#targetFamilyId").val($("#showFamilyLink").val());
	}
	
	var familyFlowRuleExceptionTable = $('#familyFlowRuleExceptionTable').dataTable({
		"responsive" : 	true,
		"ordering": 	false,
        "info":     	false,
        "bFilter": 		false,
        "paging":		true,
		"aLengthMenu": [[2, 20, 30, 40, -1], [2, 20, 30, 40, "All"]],
       "iDisplayLength":2
	});
