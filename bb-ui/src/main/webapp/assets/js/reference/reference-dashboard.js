var referenceTableRecords = null;

$(document).ready(function() {

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
	
	var searchHeight = $('.search-dropdown').height();

	$(".search-control .search-btn").on("click", function(){
	
		$('.search-dropdown').slideToggle("show");
	});

	$("#hideSearch").on("click", function(){
		$(this).parents(".search-input").removeClass("active");
		$(".search-dropdown").slideUp("hide");
	});

	$('.navigate').on('click',function(ev){
		refDashboardNavigation($(ev.target).attr('url'),$(ev.target).attr('targetId'));
	});
	
	function refDashboardNavigation(url, id) {
		$.ajax({
			type: "POST",
			url: url,
			contentType: "application/json",
			success: function(response) {
				$("#" + id).html(response);
				$('#dateRangeFilter').val('Showing till date');
				fetchRecords();
				//prepareDataTable();
			}	
		});
	}
	
	 //date range picker js
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

	$('.daterange-picker .date').on('cancel.daterangepicker', function(ev, picker) {
		$(this).parents(".daterange-picker").find('input').val('');
	});

	$('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
		$(this).val(picker.startDate.format('MMM DD, YYYY') + ' - ' + picker.endDate.format('MMM DD, YYYY'));
		fetchRecords();
	});

	$('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
		fetchRecords();
		$(this).val('Showing till date');
	});
	
	if(($('#referenceEntryTable').length>0) || ($('#updateReferenceTable').length>0) || ($('#duplicateCheckReferenceTable').length>0)){
		fetchRecords();
	}
	
	$(document).on("click",'#showMyRecordsFilter', function() {
		fetchRecords();
	});

	$(document).on("change",'#jurisdictionFilter', function() {
		fetchRecords();
	});
	
	function fetchRecords() {
		clearTables();
		
		var dashbordSubMenu = $('#dashbordSubMenu').val();
		
		if (dashbordSubMenu == 'updateReference') {
			referenceTableRecords = new $.blackbox.reference.DataTable('#updateReferenceTable', '/reference/dashboard/updateReference/records', recordFilters(), true);
		} else if (dashbordSubMenu == 'duplicateCheck') {
			referenceTableRecords = new $.blackbox.reference.DataTable('#duplicateCheckReferenceTable', '/reference/dashboard/duplicateCheck/records', recordFilters(), true);
		} else if (dashbordSubMenu = 'referenceEntry') {
			referenceTableRecords = new $.blackbox.reference.DataTable('#referenceEntryTable', '/reference/dashboard/referenceEntry/records', recordFilters(), true);
		}
		
		//referenceTableRecords.order([0, 'desc']).draw();
	}
	
	function clearTables() {
		if (referenceTableRecords != null) {
			referenceTableRecords.destroy();
			referenceTableRecords = null;
		}
		
		$('#updateRefTableBody').html('');
		$('#duplicateCheckRefTableBody').html('');
		$('#referenceEntryTableBody').html('');
	}
	
	function recordFilters() {
		return {
			'iDateRange': $("#dateRangeFilter").val(),
			'iMyRecords': $('#showMyRecordsFilter').is(':checked'),
			'iJurisdiction': $('#jurisdictionFilter :selected').val()
		}
	}
	
	function exportTableToCSV(tableId, filename) {
        var tab_text="<table border='2px'>";
		var textRange; 
		var j=0;
		tab = document.getElementById(tableId); // id of table
		        
		for(j = 0 ; j < tab.rows.length ; j++) {   
			 //only formatting of upper row
            if (j == 0) {
                tab_text = tab_text + "<tr bgcolor='#87AFC6'>";
            } else {
                tab_text = tab_text + "<tr>";
            }
		    for(k = 0 ; k < (tab.rows[j].cells.length-1) ; k++) {
		    	tab_text=tab_text+"<td>"+tab.rows[j].cells[k].innerHTML+"</td>";
		    }                
            tab_text=tab_text+"</tr>";
		}

		tab_text=tab_text+"</table>";
		tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
		tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
		tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

        // Data URI
        //csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);

        //Save file for IE 10+
        if(window.navigator.msSaveOrOpenBlob) {
        	 csvData = decodeURIComponent(tab_text);

        	 if(window.navigator.msSaveBlob){
        		 var blob = new Blob([csvData],{ type: "application/csv;charset=utf-8;"});
        		 navigator.msSaveBlob(blob, filename);
        	 }
         }
         //Other browsers
         else{
        	 csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tab_text);
        	 $(this).attr({
        		 "href": csvData,
        		 "target": "_blank",
        		 "download": filename
        	 });
         }
	}

	// This must be a hyperlink
	$("#exportData").on('click', function (event) {
		var dashbordSubMenu = $('#dashbordSubMenu').val();
		 
		var tableId = '';
		
		if (dashbordSubMenu == 'referenceEntry') {
			tableId = 'referenceEntryTable';
		} else if (dashbordSubMenu == 'updateReference') {
			tableId = 'updateReferenceTable';
		} else if (dashbordSubMenu = 'duplicateCheck') {
			tableId = 'duplicateCheckReferenceTable';
		}
		// CSV
		exportTableToCSV.apply(this, [tableId, 'exportData.xls']);
  
		 // IF CSV, don't do event.preventDefault() or return false
		 // We actually need this to be a typical hyperlink
	});
	
	autoCompleteJurisdiction();
	//reference ocr done
	$("#action").on('click',function(){
		refAddAction('../dashboard/addReference',"add-ref-btn");
	});

	function refAddAction(url, id) {
		var data = {
				"id" : $("#notificationProcessId").attr("value"),
				"ocrid" : $("#ocrId").attr("value"),
			};
		
		$.ajax({
			type: "GET",
			url: url,
			data: data,
			contentType: "application/json",
			success: function(response) {
				$('#'+id).html(response);
				autoCompleteJurisdiction();
			}	
		});
	}
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


function popupMsgForNoReference(current){
	
	$("#popupMsgForNoReference").removeClass("hide");
	$("#popupMsgForNoReference").show();
	$("#popupMsgForNoReference").wrap("<div class='overlay'>");
	var $href = $(current).attr("data-href");
	$("#popupMsgForNoReference .btn-submit").attr("onclick",$href);
}

function hidepopupMsgForNoReference(){
	$("#popupMsgForNoReference").addClass("hide");
	$("#popupMsgForNoReference").unwrap("<div class='overlay'>");
}

function popupMsgForAddReference(current){
	
	$("#popupMsgForAddReference").removeClass("hide");
	$("#popupMsgForAddReference").show();
	$("#popupMsgForAddReference").wrap("<div class='overlay'>");
	var $href = $(current).attr("data-href");
	$("#popupMsgForAddReference .btn-submit").attr("href",$href);
}

function hidepopupMsgForAddReference(){
	$("#popupMsgForAddReference").addClass("hide");
	$("#popupMsgForAddReference").unwrap("<div class='overlay'>");
}

function autoCompleteJurisdiction() {
	$(".jurisdiction").autocomplete({
			source : $('#jurisdictionList').val() === undefined ? [] : $('#jurisdictionList').val().split(";"),
		minLength : 1,
			change : function(event, ui) {
				$(this).val((ui.item ? ui.item.value : ""));
			}
	});
}
//value initialise
$('#familyLinkageSelect').val('application');
$('input[type=text]').val('');



$('#PdfFileNPLSelf').on('change', function (ev) {
	   // console.log(ev.target.files[0].name);
	    $('#filenameNPLSelf').html(ev.target.files[0].name);
	})