
//date filter picker js

var tblAllRecordView = null;
var tblPendingReferenceView = null;

$(function () {

    $('#till-date-calender').click(function () {
        $(".filter-date-popup").slideToggle("slow");
    });

    var fil_date;
    $(".filter-date-picker").datepicker({
            todayHighlight: true
        })
        .on('changeDate', function (ev) {
            fil_date = ev.format('dd/mm/yyyy');
        });

    $("#subFilDateBtn").on("click", function () {
        if (typeof fil_date === "undefined") {
            alert("Plz Select a Value");
            return;
        }
        var radioBtnValue = $('input[name="filter-date"]:checked').val();
        console.log("fil_date", fil_date, radioBtnValue);
        $(".filter-date-popup").slideUp("slow");
    })

    $(".close-btn").on("click", function () {
        $(".filter-date-popup").slideUp("slow");
    })
    
    $("#nplData input[type='text']").focusout(function () {
        var populatedData = '';
        $("#nplData input[type='text']").each(function () {
            if ($(this).val() !== 'undefined') {
                populatedData += $(this).val() + " ";
                $("#autoPupulatedData").text(populatedData);
            }
        });
    });

    $("#referenceEntries").on('change', function () {
        if (this.value == 'npl') {
            $("#nplSelect").show();
        } else {
            $("#nplSelect").hide();
        }
    });

    $("#nplSelect").on('click', function () {
        if ($("#unpublishedApp").is(':checked') === true) {
            $(".USCheck").hide();
        } else {
            $(".USCheck").show();
        }
    });

    $("#searchNplBtn").on('click', function () {
        /*$("#RetrievalDate").show();
        $("#nplTitle").val('nplTitle2');
        if ($("#unpublishedApp").is(':checked') === false) {
            $("#nplPublicationDate").val('Dec 21, 2015');
            $("#nplAuthor").val('nplAuthor');
            $("#nplPublicationDetails").val('nplPublicationDetails');
            $("#nplRelevantPages").val('5');;
            $("#nplIssueNo").val('10');
            $("#nplURL").val('http://www.google.com');
            $("#nplRetrievalDate").val('Dec 20, 2015');
        };
        $("#nplData input[type='text']").trigger('focusout');*/
    	var data='';
    	var result;
    	if ($("#unpublishedApp").is(':checked') === false) {
          var nextStrinArray=[$("#nplTitle").val(),$("#nplPublicationDate").val(),$("#nplAuthor").val(),$("#nplPublicationDetails").val(),$("#nplRelevantPages").val(),$("#nplIssueNo").val(),$("#nplURL").val(),$("#nplRetrievalDate").val()];
          result=concatString(data,nextStrinArray);
        };
    	console.log(result);
    	$.ajax({
    		'url':'/',
    		'data':result,
    	});
    });
    function concatString(source, newString ){
    	for(var i=0;i<newString.length;i++){
    		//console.log((newString[i])==='');
	    	if((newString[i])!==''){
	    		if(i>0){
	    			source+=',';
	    		}
	    		source+=newString[i];
	    	}
    	}
    	return source;
    }
    $("#nplURL").focusout(function () {
        //need to be verify for various urls
        var urlRegExp = new RegExp('^(https?:\\/\\/)?' + // protocol
            '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
            '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
            '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
            '(\\?[,;&a-z\\d%_.~+=-]*)?' + // query string
            '(\\#[-a-z\\d_]*)?$'); // hash

        if (urlRegExp.test($("#nplURL").val())) {
            $("#RetrievalDateError").hide();
            $("#RetrievalDate").show();
        } else {
            $("#RetrievalDate").hide();
            $("#RetrievalDateError").show();
        }
    });

    $(".input-group-addon").on('click', function () {
        $(this).siblings('input').focus();
    });
    
    $("#usData, #nplData, #foreignData").hide();
    $("#referenceEntries").on("change", function () {
        if ($(this).val() == "us") {
            $("#usData").show();
            $("#nplData, #foreignData").hide();
        } else if ($(this).val() == "npl") {
            $("#nplData").show();
            $("#usData, #foreignData").hide();
        } else if ($(this).val() == "foreign") {
            $("#foreignData").show();
            $("#usData, #nplData").hide();
        }
    });
    
    $('.navigate').on('click',function(ev){
		referenceNavigation($(ev.target).attr('url'),$(ev.target).attr('targetId'));
	});
	
	function referenceNavigation(url, id) {
		$.ajax({
			type: "POST",
			url: url,
			contentType: "application/json",
			success: function(response) {
				$("#" + id).html(response);
				$('#dateRangeFilter').val('Showing till date');
				fetchRecords();
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
	
	autoCompleteJurisdiction();
});

function recordFilters() {
	return {
			'iDateRange': $("#dateRangeFilter").val(),
			'iMyRecords': $('#myDocumentsOnly').is(':checked'),
			'iJurisdiction': $('#select-jurisdiction :selected').val()
	}
}

function fetchAllRecords() {
	clearTables();
	$('.referenceTable').hide();
	$('#tblAllRecords').show();
	var colDef = {
			'disabledCols' : [ {
				'bSortable' : false,
				'aTargets' : [ 0 ]
			} ]
		};
	tblAllRecordView = new $.blackbox.reference.DataTable('#tblAllRecords', '/reference/management/allRecords', recordFilters(), true);
	tblAllRecordView.order([3, 'desc']).draw();
}

function fetchPendingRecords() {
	clearTables();
	$('.referenceTable').hide();
	$('#tblPendingRecords').show();
	var colDef = {
			'disabledCols' : [ {
				'bSortable' : false,
				'aTargets' : [ 0 ]
			} ]
		};
	tblPendingReferenceView = new $.blackbox.reference.DataTable('#tblPendingRecords', '/reference/management/pendingRecords', recordFilters(), true);
	tblPendingReferenceView.order([3, 'desc']).draw();
}

function clearTables() {
	if (tblAllRecordView != null) {
		tblAllRecordView.destroy();
		tblAllRecordView = null;
	}
	if (tblPendingReferenceView != null) {
		tblPendingReferenceView.destroy();
		tblPendingReferenceView = null;
	}
	$('#tbodyAllRecords').html('');
	$('#tbodyPendingRecords').html('');
}

$(document).ready(function() {
	fetchRecords();
});

$(document).on("click",'#myDocumentsOnly', function(){
	fetchRecords();
});

$(document).on("click",'#showPendingReferences', function(){
	fetchPendingRecords();
});

$(document).on("click",'#showAllRecords', function(){
	fetchAllRecords();
});

$(document).on("change",'#select-jurisdiction', function(){
	fetchRecords();
});

function fetchRecords() {
	
	var hasClass = $('#allRecordsTab').hasClass('active');
	if(hasClass) {
		fetchAllRecords();
	}
	
	hasClass = $('#pendingReferenceTab').hasClass('active');
	if(hasClass) {
		fetchPendingRecords();
	}
}
function format ( d ) {
	return '<table class="table custom-table"><tr><th></th><th>Mailing Date</th><th>Document Description</th><th>Reference Count</th><th>Reference Entered by</th><th>Action</th></tr>'+
	'<tr id="1" class="odd"> <td></td><td></td><td></td><td>${reference.mailingDate}</td>'+
	'<td><div class="table-data">${reference.documentDescription} <span class="icon icon-attach attachmenticon"></span></div></td>'+
	'<td>4</td>'+
	'<td>John Mayer<br/>Sep 01, 2015</td>'+
	'<td><div class="action-btns-grid"><a href="management/review"><span class="icon icon-edit"></span> Review</a></div></td></tr>'+
	'<tr id="1"> <td></td><td></td><td></td><td>${reference.mailingDate}</td>'+
	'<td><div class="table-data">${reference.documentDescription} <span class="icon icon-attach attachmenticon"></span></div></td>'+
	'<td>4</td>'+
	'<td>John Mayer<br/>Sep 01, 2015</td>'+
	'<td><div class="action-btns-grid"><a href="management/review"><span class="icon icon-edit"></span> Review</a></div></td></tr></table>'
}

function formatPending ( d ) {
	return '<tr class="odd">'+
    '<td class="text-center"></td>'+
    '<td></td>'+
    '<td>${reference.correspondenceDTO.applicationNumber}</td>'+
    '<td>${reference.correspondenceDTO.mailingDate}</td>'+
    '<td>'+
        '<div class="table-data">${reference.correspondenceDTO.documentDescription} <a href="management/download" target="_blank"><span  class="icon icon-attach"></span></a></div>'+
    '</td>'+
    
    '<td>${reference.correspondenceDTO.uploadedBy}'+
        '<br/>Sep 01, 2015</td>'+
    '<td>'+
        '<div class="action-btns-grid"><a href="management/addReference/${reference.id}"><span class="icon icon-edit"></span> Add References</a></div>'+
    '</td>'+
'</tr>'
}

function formatDetails() {
	return '<div id="detailsDiv"></div>';
}

function loadChildRow(tableName, element, row, data) {
    $.ajax({
        type: 'GET',
        url: $('#contextPath').val()+'/reference/management/getCorrespondence',
        data: data,
        success: function (response) {

        	result = response.replace(/>\s+</g,'><');
        	tableName.row($(element).parent().parent()).child(
					$(result)).show();
			
        	//$('#detailsDiv').html(response);              
        }
    });
}

//Add event listener for opening and closing details
$('#tblAllRecords tbody').on('click', 'span.expand', function () {

    var tr = $(this).closest('tr');
    var span = $(this).closest('span');
    var row = tblAllRecordView.row( tr );
    
	var data = {
			"correspondence" : tr.find('.corrId').attr('data'),
			"jurisdiction" : tr.find('td')[1].innerText,
			"application" : tr.find('td')[2].innerText,
			"view" : 'all-records-detail'
		}
	
    if ( row.child.isShown() ) {
        // This row is already open - close it
        row.child.hide();
        tr.removeClass('shown');
        span.removeClass('icon-minus');
        span.addClass('icon-plus');
    }
    else {
        // Open this row
        //row.child( formatDetails() ).show();
        tr.addClass('shown');
        span.addClass('icon-minus');
        span.removeClass('icon-plus');
        loadChildRow(tblAllRecordView,this,row,data);
    }
});

//Add event listener for opening and closing details
$('#tblPendingRecords tbody').on('click', 'span.expand', function () {
	

    var tr = $(this).closest('tr');
    var span = $(this).closest('span');
    var row = tblPendingReferenceView.row( tr );
	
	var data = {
			"correspondence" : tr.find('.corrId').attr('data'),
			"jurisdiction" : tr.find('td')[1].innerText,
			"application" : tr.find('td')[2].innerText,
			"view" : 'pending-reference-detail'
		}
	
    if ( row.child.isShown() ) {
        // This row is already open - close it
        row.child.hide();
        tr.removeClass('shown');
        span.removeClass('icon-minus');
        span.addClass('icon-plus');
    }
    else {
        // Open this row
    	//row.child( formatDetails() ).show();
        tr.addClass('shown');
        span.addClass('icon-minus');
        span.removeClass('icon-plus');
        loadChildRow(tblPendingReferenceView,this,row,data);
    }
});

/*//on delete action
$('.markDeleted').on('click', function () {
	console.log('hi del');
	var img = $(this).closest('img');
	img.removeClass('markDeleted');
    img.addClass('markReviewed');
    img.attr('src',$('#contextPath').val() + "/assets/images/svg/delete.svg");
});


// on delete action
$('.markReviewed').on('click', function () {
	console.log('hi rev');
	var img = $(this).closest('img');
	img.removeClass('markReviewed');
    img.addClass('markDeleted');
    img.attr('src',$('#contextPath').val() + "/assets/images/svg/approve.svg");
});
*/

$('img.icon20x').on('click',function(){
if($(event.target).hasClass('markReviewed')){
	var img = $(this).closest('img');
	img.removeClass('markReviewed');
    img.addClass('markFlagged');
    img.attr('src',$('#contextPath').val() + "/assets/images/svg/flag.svg");
    var reviewArray = [];
    $('.markReviewed').each(function(index, obj){
		reviewArray.push($(this).attr('id'));
	});
    if(reviewArray.length > 0) {
    	$('.updateReviewStatusButton').attr('disabled',false)
    }
    else {
    	$('.updateReviewStatusButton').attr('disabled',true)
    }
	
}else if($(event.target).hasClass('markFlagged')){
	var img = $(this).closest('img');
	img.removeClass('markFlagged');
    img.addClass('markReviewed');
    img.attr('src',$('#contextPath').val() + "/assets/images/svg/ok.svg");
    var reviewArray = [];
    $('.markReviewed').each(function(index, obj){
		reviewArray.push($(this).attr('id'));
	});
    if(reviewArray.length > 0) {
    	$('.updateReviewStatusButton').attr('disabled',false)
    }
    else {
    	$('.updateReviewStatusButton').attr('disabled',true)
    }
}
});

// on update action on review
function updateReviewAction() {
	
	var reviewArray = [];
	//var deleteArray = [];
	
	$('.markReviewed').each(function(index, obj){
		reviewArray.push($(this).attr('id'));
	});
	
	var correspondenceId = $('#correspondenceId').val();
	/*$('.markDeleted').each(function(index, obj){
		deleteArray.push($(this).attr('id'));
	});*/
	
	$.ajax({
        type: 'POST',
        url: $('#contextPath').val()+'/reference/management/reviewReference',
        // data: {'markReviewed[]':reviewArray, 'markDeleted[]':deleteArray},
        data: {'markReviewed[]':reviewArray,'correspondenceId': correspondenceId},
        success: function (response) {
        	window.location.href = $('#contextPath').val() +"/ids/editReference";
        }
    });
	
}

var refrenceID;
function reviewDeleteAction() {
	hideReviewPopUp();
	$.ajax({
        type: 'POST',       
        url: $('#contextPath').val()+'/reference/management/reviewUpdate',
        data: {'id': refrenceID, 'correspondence' : $('#correspondenceId').val()},
        success:function(data){
        	$('body').html(data);
        }
    });
	
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

function popupMsgForReview(current){
	
	/** For passing the reference ID **/
	refrenceID=($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
	
	$("#popupMsgReview").removeClass("hide");
	$("#popupMsgReview").show();
	$("#popupMsgReview").wrap("<div class='overlay'>");
	$("#popupMsgReview").closest('a .close').bind('click', function() {
		hideReviewPopUp()
	});
}

function hideReviewPopUp(){
	$("#popupMsgReview").addClass("hide");
	$("#popupMsgReview").unwrap("<div class='overlay'>");
}

function reviewAction() {
	var refId = ($(event.target).attr('id')==='undefined'?'':$(event.target).attr('id'));
	
	$.ajax({
        type: 'POST',       
        url: $('#contextPath').val()+'/reference/management/reviewReference',
        data: {'id': refId, 'correspondenceId' : $('#correspondenceId').val()},
        success:function(data){
        	///$('body').html(data);
        }
    });
}

function updateComments() {
	var refId = $("#refId").val();
	var comments = $("#commentText").val();
	
	$.ajax({
        type: 'POST',       
        url: $('#contextPath').val()+'/reference/management/updateComment',
        data: {'id': refId,'comments': comments},
        success:function(){
        	console.log('refId : ',refId);
        	$('#comments'+refId).html(comments);
        	if(comments === "") {
        		$("#deleteReferenceLink"+refId).hide();
        	} else {
        		$("#deleteReferenceLink"+refId).show();
        	}
        	
        }
    });
}

function exportTableToCSV(tab, filename) {

	var tab_text = "<table border='2px'>";
	var textRange;
	var j = 0;
	tab = document.getElementById(tab); // id of table

	for (j = 0; j < tab.rows.length; j++) {

		//only formatting of upper row
		if (j == 0) {
			tab_text = tab_text + "<tr bgcolor='#87AFC6'>";
		} else {
			tab_text = tab_text + "<tr>";
		}

		for (k = 1; k < tab.rows[j].cells.length; k++) {

			tab_text = tab_text + "<td>"
					+ tab.rows[j].cells[k].innerHTML
					+ "</td>";

		}
		tab_text = tab_text + "</tr>";

	}

	tab_text = tab_text + "</table>";
	tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
	tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
	tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi,""); // reomves input params

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

//This must be a hyperlink
$("#exportData").on('click',function(event) {
	// CSV
	
	var hasClass = $('#allRecordsTab').hasClass('active');
	if(hasClass) {
		exportTableToCSV.apply(this, ['tblAllRecords', 'AllRecords.xls']);
	}
	
	hasClass = false;
	hasClass = $('#pendingReferenceTab').hasClass('active');
	if(hasClass) {
		exportTableToCSV.apply(this, ['tblPendingRecords', 'PendingReferences.xls']);
	}
	
	// IF CSV, don't do event.preventDefault() or return false
	// We actually need this to be a typical hyperlink
});

$(document).on('click', '.moreUsers', function() {
	var $parent = $(this).parents('td');
	$parent.find('.moreUsers').hide();
	$parent.find('.spanMoreUsers').show();
});

function autoCompleteJurisdiction() {
	$(".jurisdiction").autocomplete({
			source : $('#jurisdictionList').val() === undefined ? [] : $('#jurisdictionList').val().split(";"),
		minLength : 1,
			change : function(event, ui) {
				$(this).val((ui.item ? ui.item.value : ""));
			}
	});
}

$(function () {
    $(".hidden-fields").hide();
    $(".showMore").on("click", function(){
        $(this).parents(".clearfix").next(".hidden-fields").toggle();
         $(this).text(function(i, text){
              return text === "More..." ? "Less..." : "More...";
          })
        
    });
});

function updateTranslationFlag(refrenceID) {

	$.ajax({
        type: 'POST',       
        url: $('#contextPath').val()+'/reference/management/updateTranslationFlag',
        data: {'id': refrenceID, 'englishTranslationFlag' : $('#translation'+refrenceID).is(':checked')},
        success:function(data){
        	//$('body').html(data);
        }
    });
} 

function removeAttachment(current, refId) {
	
	$.ajax({
        type: 'POST',       
        url: $('#contextPath').val()+'/reference/management/deleteAttachment',
        data: {'id': refId, 'referenceType' : $('#referenceType'+refId).val()},
        success:function(data){
        	$(current).parents(".add-remove-pdf").find("a[title='Add PDF']").show();
            $(current).parents(".add-remove-pdf").find("a[title='PDF']").hide();
            $(current).hide();
        }
    });
}

function uploadAttachment(current,refId) {
	
	var formData = $("#uploadFileForm"+refId);
	$.ajax({
		 url: $('#contextPath').val()+'/reference/management/uploadAttachment',
	     type: 'POST',
	     data: formData,
	     //async: false,
	     //cache: false,
	     //contentType: false,
	     //enctype: 'multipart/form-data',
	     //processData: false,
	     success: function (response) {
	    	 $(current).parents(".add-remove-pdf").find("a[title='Add PDF']").hide();
	         $(current).parents(".add-remove-pdf").find("a[title='PDF']").show();
	         $(current).hide();
	     }
   });
	
}